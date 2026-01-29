package com.example.pas.models;

import com.google.gson.annotations.SerializedName;

/**
 * Modelo de dados para uma receita na lista (resumo).
 * Corresponde ao formato retornado pela API no endpoint /api/recipes.
 */
public class Recipe {

    @SerializedName("idMeal")
    private String id;

    @SerializedName("strMeal")
    private String name;

    @SerializedName("strMealThumb")
    private String imageUrl;

    @SerializedName("strCategory")
    private String category;

    @SerializedName("strArea")
    private String area;

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public String getArea() {
        return area;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
