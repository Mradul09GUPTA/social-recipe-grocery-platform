package com.recipeplatform.social_recipe_grocery_platform.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.recipeplatform.social_recipe_grocery_platform.models.Recipe;

@Repository 
public interface RecipeRepository extends JpaRepository<Recipe,Long> {
    
    
}
