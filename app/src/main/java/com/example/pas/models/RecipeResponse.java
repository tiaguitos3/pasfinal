package com.example.pas.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Wrapper para a resposta da API que contém lista de receitas.
 * A API retorna: { "meals": [...] }
 */
public class RecipeResponse {

    @SerializedName("meals")
    private List<Recipe> meals;

    public List<Recipe> getMeals() {
        return meals;
    }

    public void setMeals(List<Recipe> meals) {
        this.meals = meals;
    }
}
