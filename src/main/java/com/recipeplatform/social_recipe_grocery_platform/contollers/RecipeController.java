package com.recipeplatform.social_recipe_grocery_platform.contollers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RestController;

import com.recipeplatform.social_recipe_grocery_platform.dtos.CreateRecipeRequest;
import com.recipeplatform.social_recipe_grocery_platform.dtos.RecipeDetailsResponse;
import com.recipeplatform.social_recipe_grocery_platform.dtos.RecipeResponse;
import com.recipeplatform.social_recipe_grocery_platform.dtos.RecipeSummaryResponse;
import com.recipeplatform.social_recipe_grocery_platform.exceptions.GroceryProductNotFound;
import com.recipeplatform.social_recipe_grocery_platform.exceptions.RecipeNotFoundException;
import com.recipeplatform.social_recipe_grocery_platform.exceptions.UserNotFoundException;
import com.recipeplatform.social_recipe_grocery_platform.services.RecipeService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/api/v1/recipes")
public class RecipeController {
    @Autowired
    private RecipeService recipeService;

    @PostMapping
    public ResponseEntity<RecipeResponse> createRecipe(@Valid @RequestBody CreateRecipeRequest createRecipeRequest)
            throws UserNotFoundException, GroceryProductNotFound {
       

        RecipeResponse recipeResponse = recipeService.createRecipe(createRecipeRequest);
        return new ResponseEntity<>(recipeResponse, HttpStatus.CREATED);

    }
    
    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeDetailsResponse> getRecipeById(@PathVariable  Long recipeId) throws RecipeNotFoundException {
        RecipeDetailsResponse recipeDetailsResponse = recipeService.getRecipeById(recipeId);
        return new ResponseEntity<>(recipeDetailsResponse,HttpStatus.FOUND);
    }

    @GetMapping
    public ResponseEntity<Page<RecipeSummaryResponse>> getAllRecipes(@RequestParam(defaultValue = "0")  int page  , @RequestParam(defaultValue = "5")  int size ) {
          Page<RecipeSummaryResponse> recipeSummaryResponses= recipeService.getAllRecipes(page, size);
        return new ResponseEntity<>(recipeSummaryResponses,HttpStatus.OK);
    }
    
    

}
