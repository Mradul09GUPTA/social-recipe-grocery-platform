package com.recipeplatform.social_recipe_grocery_platform.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "recipes")
@Setter 
@Getter 
public class Recipe extends BaseModel {
    @Column(nullable = false)
    private String imageUrl;
    @Column(nullable = false)
    private String description;
    @ManyToOne(fetch = FetchType.LAZY )
    private User owner;
    @Column(nullable = false)
    private int likeCount;
    @Column(nullable = false)
    private int commentCount;
    @OneToMany(mappedBy = "recipe",fetch = FetchType.LAZY)
    private List<RecipeProduct> products;

    @PrePersist
    private void onCreateRecipe() {
        likeCount = 0;
        commentCount = 0;
    }

}
