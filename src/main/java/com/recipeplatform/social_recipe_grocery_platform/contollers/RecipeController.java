package com.recipeplatform.social_recipe_grocery_platform.contollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RestController;

import com.recipeplatform.social_recipe_grocery_platform.dtos.CreateRecipeRequest;
import com.recipeplatform.social_recipe_grocery_platform.dtos.RecipeResponse;
import com.recipeplatform.social_recipe_grocery_platform.exceptions.GroceryProductNotFound;
import com.recipeplatform.social_recipe_grocery_platform.exceptions.UserNotFoundException;
import com.recipeplatform.social_recipe_grocery_platform.services.RecipeService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1/recipes")
public class RecipeController {
    @Autowired
    private RecipeService recipeService;

    @PostMapping
    public ResponseEntity<?> createRecipe(@Valid @RequestBody CreateRecipeRequest createRecipeRequest)
            throws UserNotFoundException, GroceryProductNotFound {
        // TODO: process POST request

        RecipeResponse recipeResponse = recipeService.creatRecipe(createRecipeRequest);
        return new ResponseEntity<>(recipeResponse, HttpStatus.CREATED);

    }

}
