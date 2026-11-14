
package com.recipeplatform.social_recipe_grocery_platform.services;

import org.springframework.data.domain.Page;

import com.recipeplatform.social_recipe_grocery_platform.dtos.CreateRecipeRequest;
import com.recipeplatform.social_recipe_grocery_platform.dtos.RecipeDetailsResponse;
import com.recipeplatform.social_recipe_grocery_platform.dtos.RecipeResponse;
import com.recipeplatform.social_recipe_grocery_platform.dtos.RecipeSummaryResponse;
import com.recipeplatform.social_recipe_grocery_platform.exceptions.GroceryProductNotFound;
import com.recipeplatform.social_recipe_grocery_platform.exceptions.RecipeNotFoundException;
import com.recipeplatform.social_recipe_grocery_platform.exceptions.UserNotFoundException;

public interface RecipeService {

    RecipeResponse createRecipe(CreateRecipeRequest createRecipeRequest)
            throws UserNotFoundException, GroceryProductNotFound;

    RecipeDetailsResponse getRecipeById(Long recipeId)
            throws RecipeNotFoundException;

    Page<RecipeSummaryResponse> getAllRecipes(int page, int size);
}
