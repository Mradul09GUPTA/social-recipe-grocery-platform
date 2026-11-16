package com.recipeplatform.social_recipe_grocery_platform.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeStepResponse {

    private Long stepId;
    private Integer stepNumber;
    private String instruction;
    private String imageUrl;
}