package com.recipeplatform.social_recipe_grocery_platform.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Setter 
@Getter 
public class User extends BaseModel {

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    private String username;

    private String profileImageUrl;

    @Column(length = 500)
    private String bio;

    private String password;
}