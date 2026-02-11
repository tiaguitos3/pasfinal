package com.example.pas.network;

import com.example.pas.models.CategoryResponse;
import com.example.pas.models.RecipeDetailResponse;
import com.example.pas.models.RecipeMutationResponse;
import com.example.pas.models.RecipeResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.POST;
import retrofit2.http.PUT;

import java.util.Map;

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
     * Obtém a lista de categorias.
     * Endpoint: GET /api/categories
     */
    @GET("api/categories")
    Call<CategoryResponse> getCategories();

    /**
     * Obtém os detalhes de uma receita específica.
     * Endpoint: GET /api/recipes/{id}
     * 
     * @param id O ID da receita a consultar
     */
    @GET("api/recipes/{id}")
    Call<RecipeDetailResponse> getRecipeDetails(@Path("id") String id);

    /**
     * Cria uma nova receita.
     * Endpoint: POST /api/recipes
     */
    @POST("api/recipes")
    Call<RecipeMutationResponse> createRecipe(@Body Map<String, Object> recipeData);

    /**
     * Atualiza uma receita existente.
     * Endpoint: PUT /api/recipes/{id}
     */
    @PUT("api/recipes/{id}")
    Call<RecipeMutationResponse> updateRecipe(@Path("id") long id, @Body Map<String, Object> recipeData);

    /**
     * Elimina uma receita.
     * Endpoint: DELETE /api/recipes/{id}
     */
    @DELETE("api/recipes/{id}")
    Call<Void> deleteRecipe(@Path("id") long id);
}
