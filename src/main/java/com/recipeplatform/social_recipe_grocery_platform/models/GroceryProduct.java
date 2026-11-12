package com.recipeplatform.social_recipe_grocery_platform.models;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * GroceryProduct
 */
@Entity
@Table(name = "grocery_products")
@Getter 
@Setter 
public class GroceryProduct extends BaseModel {
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false, length = 100)
    private String brand;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @Enumerated(EnumType.STRING)
    private DietaryType dietaryType;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal packSize;
    @Column(nullable = false, length = 20)
    private String packUnit;

    private String imageUrl;

    @Column(nullable = false)
    private boolean available;

    private int stockQuantity;

}
