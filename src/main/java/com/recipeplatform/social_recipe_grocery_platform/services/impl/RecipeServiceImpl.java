package com.recipeplatform.social_recipe_grocery_platform.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.recipeplatform.social_recipe_grocery_platform.dtos.CreateRecipeRequest;
import com.recipeplatform.social_recipe_grocery_platform.dtos.RecipeDetailsResponse;
import com.recipeplatform.social_recipe_grocery_platform.dtos.RecipeIngredientRequest;
import com.recipeplatform.social_recipe_grocery_platform.dtos.RecipeIngredientResponse;
import com.recipeplatform.social_recipe_grocery_platform.dtos.RecipeProductRequest;
import com.recipeplatform.social_recipe_grocery_platform.dtos.RecipeProductResponse;
import com.recipeplatform.social_recipe_grocery_platform.dtos.RecipeResponse;
import com.recipeplatform.social_recipe_grocery_platform.dtos.RecipeStepRequest;
import com.recipeplatform.social_recipe_grocery_platform.dtos.RecipeStepResponse;
import com.recipeplatform.social_recipe_grocery_platform.dtos.RecipeSummaryResponse;
import com.recipeplatform.social_recipe_grocery_platform.exceptions.GroceryProductNotFound;
import com.recipeplatform.social_recipe_grocery_platform.exceptions.RecipeNotFoundException;
import com.recipeplatform.social_recipe_grocery_platform.exceptions.UserNotFoundException;
import com.recipeplatform.social_recipe_grocery_platform.models.GroceryProduct;
import com.recipeplatform.social_recipe_grocery_platform.models.Recipe;
import com.recipeplatform.social_recipe_grocery_platform.models.RecipeIngredient;
import com.recipeplatform.social_recipe_grocery_platform.models.RecipeProduct;
import com.recipeplatform.social_recipe_grocery_platform.models.RecipeStep;
import com.recipeplatform.social_recipe_grocery_platform.models.User;
import com.recipeplatform.social_recipe_grocery_platform.repositories.GroceryProductRepository;
import com.recipeplatform.social_recipe_grocery_platform.repositories.RecipeIngredientRepository;
import com.recipeplatform.social_recipe_grocery_platform.repositories.RecipeProductRepository;
import com.recipeplatform.social_recipe_grocery_platform.repositories.RecipeRepository;
import com.recipeplatform.social_recipe_grocery_platform.repositories.RecipeStepRepository;
import com.recipeplatform.social_recipe_grocery_platform.repositories.UserRepository;
import com.recipeplatform.social_recipe_grocery_platform.services.RecipeService;

@Service
@Transactional
public class RecipeServiceImpl implements RecipeService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private GroceryProductRepository groceryProductRepository;
    @Autowired
    private RecipeProductRepository recipeProductRepository;
    @Autowired
    private RecipeIngredientRepository recipeIngredientRepository;
    @Autowired
    private RecipeStepRepository recipeStepRepository;

    @Override
    @Transactional
    public RecipeResponse createRecipe(CreateRecipeRequest request)
            throws UserNotFoundException, GroceryProductNotFound {

        // 1. Validate owner
        User owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new UserNotFoundException("User does not exist"));

        // 2. Validate all grocery products before saving anything
        List<GroceryProduct> groceryProducts = new ArrayList<>();

        for (RecipeProductRequest productRequest : request.getProducts()) {

            GroceryProduct product = groceryProductRepository
                    .findById(productRequest.getGroceryProductId())
                    .orElseThrow(() -> new GroceryProductNotFound("Product not found"));

            groceryProducts.add(product);
        }

        // 3. Create and save Recipe
        Recipe recipe = new Recipe();
        recipe.setImageUrl(request.getImageUrl());
        recipe.setDescription(request.getDescription());
        recipe.setRecipeName(request.getRecipeName());
        recipe.setOwner(owner);

        Recipe savedRecipe = recipeRepository.save(recipe);

        // 4. Create RecipeProducts
        List<RecipeProduct> recipeProducts = new ArrayList<>();

        for (int i = 0; i < request.getProducts().size(); i++) {

            RecipeProductRequest productRequest = request.getProducts().get(i);

            RecipeProduct recipeProduct = new RecipeProduct();
            recipeProduct.setRecipe(savedRecipe);
            recipeProduct.setGroceryProduct(groceryProducts.get(i));
            recipeProduct.setQuantity(productRequest.getQuantity());
            recipeProduct.setUnit(productRequest.getUnit());
            recipeProduct.setOptional(productRequest.isOptional());

            recipeProducts.add(recipeProduct);
        }

        recipeProductRepository.saveAll(recipeProducts);

        // 5. Create RecipeIngredients
        List<RecipeIngredient> ingredients = new ArrayList<>();

        for (RecipeIngredientRequest ingredientRequest : request.getIngredients()) {

            RecipeIngredient ingredient = new RecipeIngredient();
            ingredient.setRecipe(savedRecipe);
            ingredient.setIngredientName(ingredientRequest.getIngredientName());
            ingredient.setQuantity(ingredientRequest.getQuantity());
            ingredient.setUnit(ingredientRequest.getUnit());
            ingredient.setOptional(ingredientRequest.isOptional());

            ingredients.add(ingredient);
        }

        recipeIngredientRepository.saveAll(ingredients);

        // 6. Create RecipeSteps
        List<RecipeStep> steps = new ArrayList<>();

        for (RecipeStepRequest stepRequest : request.getSteps()) {

            RecipeStep step = new RecipeStep();
            step.setRecipe(savedRecipe);
            step.setStepNumber(stepRequest.getStepNumber());
            step.setInstruction(stepRequest.getInstruction());
            step.setImageUrl(stepRequest.getImageUrl());

            steps.add(step);
        }

        recipeStepRepository.saveAll(steps);

        // 7. Return response
        RecipeResponse response = new RecipeResponse();
        response.setCreated(true);
        response.setRecipeId(savedRecipe.getId());
        response.setMessage("Recipe created successfully");

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public RecipeDetailsResponse getRecipeById(Long recipeId)
            throws RecipeNotFoundException {

        // 1. Find recipe
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeNotFoundException("Recipe does not exist in the system"));

        RecipeDetailsResponse response = new RecipeDetailsResponse();

        response.setRecipeId(recipe.getId());
        response.setTitle(recipe.getRecipeName());
        response.setImageUrl(recipe.getImageUrl());
        response.setDescription(recipe.getDescription());

        response.setOwnerId(recipe.getOwner().getId());
        response.setOwnerName(
                recipe.getOwner().getFirstName() + " " +
                        recipe.getOwner().getLastName());

        // 2. Map grocery products
        List<RecipeProductResponse> productResponses = new ArrayList<>();

        for (RecipeProduct recipeProduct : recipe.getProducts()) {

            RecipeProductResponse productResponse = new RecipeProductResponse();

            GroceryProduct groceryProduct = recipeProduct.getGroceryProduct();

            productResponse.setGroceryProductId(groceryProduct.getId());
            productResponse.setProductName(groceryProduct.getName());
            productResponse.setBrand(groceryProduct.getBrand());
            productResponse.setQuantity(recipeProduct.getQuantity());
            productResponse.setUnit(recipeProduct.getUnit());
            productResponse.setOptional(recipeProduct.isOptional());

            productResponses.add(productResponse);
        }

        response.setProducts(productResponses);

        // 3. Map recipe ingredients
        List<RecipeIngredientResponse> ingredientResponses = new ArrayList<>();

        for (RecipeIngredient ingredient : recipe.getRecipeIngredients()) {

            RecipeIngredientResponse ingredientResponse = new RecipeIngredientResponse();

            ingredientResponse.setIngredientId(ingredient.getId());
            ingredientResponse.setIngredientName(ingredient.getIngredientName());
            ingredientResponse.setQuantity(ingredient.getQuantity());
            ingredientResponse.setUnit(ingredient.getUnit());
            ingredientResponse.setOptional(ingredient.isOptional());

            ingredientResponses.add(ingredientResponse);
        }

        response.setIngredients(ingredientResponses);

        // 4. Map cooking steps
        List<RecipeStepResponse> stepResponses = new ArrayList<>();

        for (RecipeStep step : recipe.getSteps()) {

            RecipeStepResponse stepResponse = new RecipeStepResponse();

            stepResponse.setStepId(step.getId());
            stepResponse.setStepNumber(step.getStepNumber());
            stepResponse.setInstruction(step.getInstruction());
            stepResponse.setImageUrl(step.getImageUrl());

            stepResponses.add(stepResponse);
        }
        // Sort cooking steps by step number
        stepResponses.sort(
                (step1, step2) -> step1.getStepNumber().compareTo(step2.getStepNumber()));
                response.setSteps(stepResponses);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RecipeSummaryResponse> getAllRecipes(int page, int size) {

        List<RecipeSummaryResponse> recipeSummaryResponses = new ArrayList<>();
        Sort sort = Sort.by("likeCount").descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Recipe> reciPage = recipeRepository.findAll(pageable);

        for (Recipe recipe : reciPage.getContent()) {
            RecipeSummaryResponse recipeSummaryResponse = new RecipeSummaryResponse();
            recipeSummaryResponse.setRecipeId(recipe.getId());
            recipeSummaryResponse.setTitle(recipe.getRecipeName());
            recipeSummaryResponse.setImageUrl(recipe.getImageUrl());
            recipeSummaryResponse.setDescription(recipe.getDescription());
            User owner = recipe.getOwner();
            recipeSummaryResponse.setOwnerName(owner.getFirstName() + " " + owner.getLastName());
            recipeSummaryResponse.setLikeCount(recipe.getLikeCount());
            recipeSummaryResponse.setCommentCount(recipe.getCommentCount());
            recipeSummaryResponses.add(recipeSummaryResponse);
        }

        return new PageImpl<>(recipeSummaryResponses);

    }

}
