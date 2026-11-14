package com.recipeplatform.social_recipe_grocery_platform.dtos;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeDetailsResponse {

    private Long recipeId;
    private String title;
    private String imageUrl;
    private String description;

    private Long ownerId;
    private String ownerName;

    private List<RecipeProductResponse> products;
}