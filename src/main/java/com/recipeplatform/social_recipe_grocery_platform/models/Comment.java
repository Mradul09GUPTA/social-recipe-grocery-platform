package com.recipeplatform.social_recipe_grocery_platform.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
@Entity 
@Table (name="comment")
public class Comment extends BaseModel {
    @ManyToOne
    private User owner;
    @Column (length = 2000)
    private String body;
    @ManyToOne (fetch = FetchType.LAZY)
    private Recipe recipe;

    
}
