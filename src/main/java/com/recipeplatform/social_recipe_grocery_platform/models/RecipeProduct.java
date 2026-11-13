package com.recipeplatform.social_recipe_grocery_platform.models;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * RecipeProduct
 */
@Entity 
@Table (name = "recipe_products")
@Setter 
@Getter 
public class RecipeProduct extends BaseModel {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private  Recipe recipe ;
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "grocery_product_id", nullable = false)
    private GroceryProduct groceryProduct;
    private String unit;
    private BigDecimal quantity;
    private boolean optional=false;


}
