package com.example.pas.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pas.models.Recipe;
import com.example.pas.models.RecipeResponse;
import com.example.pas.network.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ViewModel para o ecrã principal.
 * Gere os dados das receitas vindas da API.
 */
public class RecipeViewModel extends ViewModel {

    private final MutableLiveData<List<Recipe>> recipes = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * Busca as receitas da API.
     */
    public void fetchRecipes() {
        isLoading.setValue(true);
        errorMessage.setValue(null);

        ApiClient.getApiService().getRecipes().enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    recipes.setValue(response.body().getMeals());
                } else {
                    errorMessage.setValue("Erro ao carregar receitas");
                }
            }

            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Erro de conexão: " + t.getMessage());
            }
        });
    }
}
