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
import com.recipeplatform.social_recipe_grocery_platform.dtos.RecipeProductRequest;
import com.recipeplatform.social_recipe_grocery_platform.dtos.RecipeProductResponse;
import com.recipeplatform.social_recipe_grocery_platform.dtos.RecipeResponse;
import com.recipeplatform.social_recipe_grocery_platform.dtos.RecipeSummaryResponse;
import com.recipeplatform.social_recipe_grocery_platform.exceptions.GroceryProductNotFound;
import com.recipeplatform.social_recipe_grocery_platform.exceptions.RecipeNotFoundException;
import com.recipeplatform.social_recipe_grocery_platform.exceptions.UserNotFoundException;
import com.recipeplatform.social_recipe_grocery_platform.models.GroceryProduct;
import com.recipeplatform.social_recipe_grocery_platform.models.Recipe;
import com.recipeplatform.social_recipe_grocery_platform.models.RecipeProduct;
import com.recipeplatform.social_recipe_grocery_platform.models.User;
import com.recipeplatform.social_recipe_grocery_platform.repositories.GroceryProductRepository;
import com.recipeplatform.social_recipe_grocery_platform.repositories.RecipeProductRepository;
import com.recipeplatform.social_recipe_grocery_platform.repositories.RecipeRepository;
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

    @Override
    public RecipeResponse createRecipe(CreateRecipeRequest createRecipeRequest)
            throws UserNotFoundException, GroceryProductNotFound {
        // TODO Auto-generated method stub
        Optional<User> optionalownUser = userRepository.findById(createRecipeRequest.getOwnerId());
        if (optionalownUser.isEmpty()) {
            throw new UserNotFoundException("Please login before creating recipe");
        }
        Recipe newRecipe = new Recipe();
        newRecipe.setImageUrl(createRecipeRequest.getImageUrl());
        newRecipe.setDescription(createRecipeRequest.getDescription());
        newRecipe.setOwner(optionalownUser.get());
        newRecipe.setRecipeName(createRecipeRequest.getRecipeName());
        List<RecipeProduct> recipeProducts = new ArrayList<>();
        List<RecipeProductRequest> requestsProducts = createRecipeRequest.getProducts();
        for (RecipeProductRequest requestsProduct : requestsProducts) {
            Optional<GroceryProduct> optionalGroceryProduct = groceryProductRepository
                    .findById(requestsProduct.getGroceryProductId());
            if (optionalGroceryProduct.isEmpty()) {
                throw new GroceryProductNotFound("Product not found ");
            }
            RecipeProduct recipeProduct = new RecipeProduct();
            recipeProduct.setUnit(requestsProduct.getUnit());
            recipeProduct.setQuantity(requestsProduct.getQuantity());
            recipeProduct.setRecipe(newRecipe);
            recipeProduct.setGroceryProduct(optionalGroceryProduct.get());
            recipeProduct.setOptional(requestsProduct.isOptional());

            recipeProducts.add(recipeProduct);
        }
        newRecipe.setProducts(recipeProducts);
        recipeRepository.save(newRecipe);
        recipeProductRepository.saveAll(recipeProducts);

        RecipeResponse response = new RecipeResponse();
        response.setCreated(true);
        response.setRecipeId(newRecipe.getId());
        response.setMessage("Receipe Created");

        return response;

    }

    @Override
    @Transactional(readOnly = true)
    public RecipeDetailsResponse getRecipeById(Long recipeId) throws RecipeNotFoundException {
        // TODO Auto-generated method stub
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);
        if (optionalRecipe.isEmpty()) {
            throw new RecipeNotFoundException("Recipe is not exist in the system");
        }
        Recipe recipe = optionalRecipe.get();
        RecipeDetailsResponse recipeDetailsResponse = new RecipeDetailsResponse();
        recipeDetailsResponse.setDescription(recipe.getDescription());
        recipeDetailsResponse.setImageUrl(recipe.getImageUrl());
        recipeDetailsResponse.setOwnerId(recipe.getOwner().getId());
        recipeDetailsResponse.setOwnerName(recipe.getOwner().getFirstName() + ' ' + recipe.getOwner().getLastName());
        recipeDetailsResponse.setRecipeId(recipe.getId());
        recipeDetailsResponse.setTitle(recipe.getRecipeName());
        List<RecipeProduct> recipeProducts = recipe.getProducts();
        List<RecipeProductResponse> recipeProductResponses = new ArrayList<>();
        for (RecipeProduct recipeProduct : recipeProducts) {
            RecipeProductResponse recipeProductResponse = new RecipeProductResponse();
            recipeProductResponse.setQuantity(recipeProduct.getQuantity());
            GroceryProduct groceryProduct = recipeProduct.getGroceryProduct();
            recipeProductResponse.setProductName(groceryProduct.getName());
            recipeProductResponse.setBrand(groceryProduct.getBrand());
            recipeProductResponse.setGroceryProductId(groceryProduct.getId());
            recipeProductResponse.setOptional(recipeProduct.isOptional());
            recipeProductResponse.setUnit(recipeProduct.getUnit());
            recipeProductResponses.add(recipeProductResponse);
        }
        recipeDetailsResponse.setProducts(recipeProductResponses);

        return recipeDetailsResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RecipeSummaryResponse> getAllRecipes(int page ,int size) {

        List<RecipeSummaryResponse> recipeSummaryResponses = new ArrayList<>();
        Sort sort = Sort.by("likeCount").descending();

        Pageable pageable= PageRequest.of(page, size,sort);
        
        Page<Recipe> reciPage= recipeRepository.findAll(pageable);

        for (Recipe recipe : reciPage.getContent() ) {
            RecipeSummaryResponse recipeSummaryResponse = new RecipeSummaryResponse();
            recipeSummaryResponse.setRecipeId(recipe.getId());
            recipeSummaryResponse.setTitle(recipe.getRecipeName());
            recipeSummaryResponse.setImageUrl(recipe.getImageUrl());
            recipeSummaryResponse.setDescription(recipe.getDescription());
            User owner = recipe.getOwner();
            recipeSummaryResponse.setOwnerName(owner.getFirstName()+" " +owner.getLastName());
            recipeSummaryResponse.setLikeCount(recipe.getLikeCount());
            recipeSummaryResponse.setCommentCount(recipe.getCommentCount());
            recipeSummaryResponses.add(recipeSummaryResponse);
        }

        return new PageImpl<>(recipeSummaryResponses);

    }

}
