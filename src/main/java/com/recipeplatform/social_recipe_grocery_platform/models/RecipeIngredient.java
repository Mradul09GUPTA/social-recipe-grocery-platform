package com.recipeplatform.social_recipe_grocery_platform.models;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
@Entity 
@Table(name="recipe_ingredient")
public class RecipeIngredient extends  BaseModel{
       @Column (nullable = false,length = 100)
       private String ingredientName;
       @Column (nullable = false,precision = 10, scale = 2)
       private BigDecimal quantity;
       @Column (nullable = false)
       private String unit;
       @ManyToOne 
       private Recipe recipe;
       @Column (nullable = false)
       private  boolean optional;
}
