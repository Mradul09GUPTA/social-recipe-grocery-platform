package com.recipeplatform.social_recipe_grocery_platform.dtos;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeProductResponse {

    private Long groceryProductId;
    private String productName;
    private String brand;

    private BigDecimal quantity;
    private String unit;

    private boolean optional;
}