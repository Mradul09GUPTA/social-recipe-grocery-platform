package com.recipeplatform.social_recipe_grocery_platform.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeStepRequest {

    @NotNull
    @Positive
    private Integer stepNumber;

    @NotBlank
    private String instruction;

    private String imageUrl;
}