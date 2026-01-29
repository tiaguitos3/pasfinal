package com.example.pas.network;

import com.example.pas.models.RecipeDetailResponse;
import com.example.pas.models.RecipeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Interface que define os endpoints da API de receitas.
 * O Retrofit usa esta interface para gerar automaticamente
 * o código que faz os pedidos HTTP.
 */
public interface ApiService {

    /**
     * Obtém a lista de todas as receitas.
     * Endpoint: GET /api/recipes
     */
    @GET("api/recipes")
    Call<RecipeResponse> getRecipes();

    /**
     * Obtém os detalhes de uma receita específica.
     * Endpoint: GET /api/recipes/{id}
     * 
     * @param id O ID da receita a consultar
     */
    @GET("api/recipes/{id}")
    Call<RecipeDetailResponse> getRecipeDetails(@Path("id") String id);
}
