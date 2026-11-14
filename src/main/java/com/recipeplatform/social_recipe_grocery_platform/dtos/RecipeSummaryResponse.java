
package com.recipeplatform.social_recipe_grocery_platform.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeSummaryResponse {

    private Long recipeId;

    private String title;

    private String imageUrl;

    private String description;

    private String ownerName;

    private int likeCount;

    private int commentCount;
}
