package com.example.pas.ui.details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pas.models.RecipeDetail;
import com.example.pas.models.RecipeDetailResponse;
import com.example.pas.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ViewModel para o ecrã de detalhes da receita.
 */
public class RecipeDetailViewModel extends ViewModel {

    private final MutableLiveData<RecipeDetail> recipeDetail = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<RecipeDetail> getRecipeDetail() {
        return recipeDetail;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * Busca os detalhes de uma receita específica.
     */
    public void fetchRecipeDetails(String recipeId) {
        isLoading.setValue(true);
        errorMessage.setValue(null);

        ApiClient.getApiService().getRecipeDetails(recipeId).enqueue(new Callback<RecipeDetailResponse>() {
            @Override
            public void onResponse(Call<RecipeDetailResponse> call, Response<RecipeDetailResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    recipeDetail.setValue(response.body().getRecipeDetail());
                } else {
                    errorMessage.setValue("Erro ao carregar detalhes");
                }
            }

            @Override
            public void onFailure(Call<RecipeDetailResponse> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Erro de conexão: " + t.getMessage());
            }
        });
    }
}
