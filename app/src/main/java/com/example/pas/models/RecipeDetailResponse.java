package com.example.pas.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Wrapper para a resposta da API que contém detalhes de uma receita.
 * A API retorna: { "meals": [{ ... detalhes ... }] }
 */
public class RecipeDetailResponse {

    @SerializedName("meals")
    private List<RecipeDetail> meals;

    public List<RecipeDetail> getMeals() {
        return meals;
    }

    public void setMeals(List<RecipeDetail> meals) {
        this.meals = meals;
    }

    /**
     * Obtém o primeiro (e único) detalhe da receita.
     */
    public RecipeDetail getRecipeDetail() {
        if (meals != null && !meals.isEmpty()) {
            return meals.get(0);
        }
        return null;
    }
}
