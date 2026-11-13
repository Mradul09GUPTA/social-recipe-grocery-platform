package com.recipeplatform.social_recipe_grocery_platform.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.recipeplatform.social_recipe_grocery_platform.dtos.CreateRecipeRequest;
import com.recipeplatform.social_recipe_grocery_platform.dtos.RecipeProductRequest;
import com.recipeplatform.social_recipe_grocery_platform.dtos.RecipeResponse;
import com.recipeplatform.social_recipe_grocery_platform.exceptions.GroceryProductNotFound;
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
    public RecipeResponse creatRecipe(CreateRecipeRequest createRecipeRequest)
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

}
