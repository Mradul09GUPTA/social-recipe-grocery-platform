package com.recipeplatform.social_recipe_grocery_platform.dtos;


import java.util.List;



import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class CreateRecipeRequest {
    @NotBlank 
    private String imageUrl;
    @NotBlank 
    private String description;
    @NotNull 
    private Long ownerId;
    @NotBlank 
    private String recipeName;
    @Valid 
    private List<RecipeProductRequest>products;
}
