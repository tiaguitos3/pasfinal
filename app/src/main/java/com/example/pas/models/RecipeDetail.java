package com.example.pas.models;

import com.google.gson.annotations.SerializedName;

/**
 * Modelo de dados para detalhes completos de uma receita.
 * Corresponde ao formato retornado pela API no endpoint /api/recipes/{id}.
 */
public class RecipeDetail {

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

    @SerializedName("strInstructions")
    private String instructions;

    @SerializedName("strPrepTime")
    private Integer prepTime;

    @SerializedName("strCookTime")
    private Integer cookTime;

    @SerializedName("strServings")
    private Integer servings;

    @SerializedName("strDifficulty")
    private String difficulty;

    // Ingredientes (formato TheMealDB)
    @SerializedName("strIngredient1")
    private String ingredient1;
    @SerializedName("strIngredient2")
    private String ingredient2;
    @SerializedName("strIngredient3")
    private String ingredient3;
    @SerializedName("strIngredient4")
    private String ingredient4;
    @SerializedName("strIngredient5")
    private String ingredient5;
    @SerializedName("strIngredient6")
    private String ingredient6;
    @SerializedName("strIngredient7")
    private String ingredient7;
    @SerializedName("strIngredient8")
    private String ingredient8;
    @SerializedName("strIngredient9")
    private String ingredient9;
    @SerializedName("strIngredient10")
    private String ingredient10;

    // Medidas
    @SerializedName("strMeasure1")
    private String measure1;
    @SerializedName("strMeasure2")
    private String measure2;
    @SerializedName("strMeasure3")
    private String measure3;
    @SerializedName("strMeasure4")
    private String measure4;
    @SerializedName("strMeasure5")
    private String measure5;
    @SerializedName("strMeasure6")
    private String measure6;
    @SerializedName("strMeasure7")
    private String measure7;
    @SerializedName("strMeasure8")
    private String measure8;
    @SerializedName("strMeasure9")
    private String measure9;
    @SerializedName("strMeasure10")
    private String measure10;

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

    public String getInstructions() {
        return instructions;
    }

    public Integer getPrepTime() {
        return prepTime;
    }

    public Integer getCookTime() {
        return cookTime;
    }

    public Integer getServings() {
        return servings;
    }

    public String getDifficulty() {
        return difficulty;
    }

    /**
     * Constrói uma string formatada com todos os ingredientes.
     */
    public String getFormattedIngredients() {
        StringBuilder sb = new StringBuilder();
        addIngredient(sb, ingredient1, measure1);
        addIngredient(sb, ingredient2, measure2);
        addIngredient(sb, ingredient3, measure3);
        addIngredient(sb, ingredient4, measure4);
        addIngredient(sb, ingredient5, measure5);
        addIngredient(sb, ingredient6, measure6);
        addIngredient(sb, ingredient7, measure7);
        addIngredient(sb, ingredient8, measure8);
        addIngredient(sb, ingredient9, measure9);
        addIngredient(sb, ingredient10, measure10);
        return sb.toString().trim();
    }

    private void addIngredient(StringBuilder sb, String ingredient, String measure) {
        if (ingredient != null && !ingredient.isEmpty()) {
            sb.append("• ");
            if (measure != null && !measure.isEmpty()) {
                sb.append(measure).append(" ");
            }
            sb.append(ingredient).append("\n");
        }
    }
}
