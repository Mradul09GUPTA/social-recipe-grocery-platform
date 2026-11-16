package com.recipeplatform.social_recipe_grocery_platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.recipeplatform.social_recipe_grocery_platform.models.RecipeStep;

public interface RecipeStepRepository extends JpaRepository<RecipeStep,Long> {
    
}
