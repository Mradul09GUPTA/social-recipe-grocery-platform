package com.recipeplatform.social_recipe_grocery_platform.models;

import java.util.Date;

import jakarta.persistence.Column;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@MappedSuperclass
public abstract class BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    
    @Column(nullable = false, updatable = false)
    @jakarta.persistence.Temporal(jakarta.persistence.TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column
    private Date lastUpdated;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
        this.lastUpdated = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastUpdated = new Date();
    }

}
