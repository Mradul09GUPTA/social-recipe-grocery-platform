package com.recipeplatform.social_recipe_grocery_platform.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

/**
 * RecipeProductRequest
 */
@Getter 
@Setter 
public class RecipeProductRequest {
    @NotNull 
    private Long groceryProductId;
    @Positive 
    private BigDecimal quantity;
    @NotBlank 
    private String unit;
    private boolean optional;

}
