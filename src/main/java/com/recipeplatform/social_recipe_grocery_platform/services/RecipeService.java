package com.recipeplatform.social_recipe_grocery_platform.services;

import com.recipeplatform.social_recipe_grocery_platform.dtos.CreateRecipeRequest;

import com.recipeplatform.social_recipe_grocery_platform.dtos.RecipeResponse;
import com.recipeplatform.social_recipe_grocery_platform.exceptions.GroceryProductNotFound;
import com.recipeplatform.social_recipe_grocery_platform.exceptions.UserNotFoundException;

public interface RecipeService {
    public RecipeResponse creatRecipe(CreateRecipeRequest createRecipeRequest) throws UserNotFoundException, GroceryProductNotFound;
    
}
