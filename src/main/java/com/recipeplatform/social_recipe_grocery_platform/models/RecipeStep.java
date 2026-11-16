package com.recipeplatform.social_recipe_grocery_platform.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * RecipeStep
 */
@Getter 
@Setter 
@Entity 
@Table (name = "recipe_step")
public class RecipeStep  extends  BaseModel{
    @Column (nullable = false)
   private String instruction;
   @ManyToOne 
   private  Recipe recipe;
   private String imageUrl;
   @Column(nullable = false)
    private int stepNumber;
    

}
