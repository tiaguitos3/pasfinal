package com.example.pas.data;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.pas.data.local.AppDatabase;
import com.example.pas.data.local.UserRecipeDao;
import com.example.pas.data.local.UserRecipeEntity;
import com.example.pas.models.RecipeMutationResponse;
import com.example.pas.network.ApiClient;
import com.example.pas.network.ApiService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository que sincroniza receitas locais com a API Laravel.
 */
public class UserRecipeRepository {

    public interface OperationCallback {
        void onSuccess();

        void onError(String message);
    }

    private final UserRecipeDao userRecipeDao;
    private final LiveData<List<UserRecipeEntity>> allRecipes;
    private final ExecutorService executorService;
    private final ApiService apiService;
    private final Handler mainHandler;

    public UserRecipeRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        userRecipeDao = database.userRecipeDao();
        allRecipes = userRecipeDao.getAllRecipes();
        executorService = Executors.newSingleThreadExecutor();
        apiService = ApiClient.getApiService();
        mainHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * Obtém todas as receitas como LiveData.
     */
    public LiveData<List<UserRecipeEntity>> getAllRecipes() {
        return allRecipes;
    }

    public void insert(UserRecipeEntity recipe, @Nullable OperationCallback callback) {
        Map<String, Object> payload = buildCreatePayload(recipe);

        apiService.createRecipe(payload).enqueue(new Callback<RecipeMutationResponse>() {
            @Override
            public void onResponse(Call<RecipeMutationResponse> call, Response<RecipeMutationResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getRecipe() != null) {
                    long remoteId = response.body().getRecipe().getId();
                    long now = System.currentTimeMillis();

                    executorService.execute(() -> {
                        recipe.setId(remoteId);
                        recipe.setCreatedAt(now);
                        recipe.setUpdatedAt(now);
                        userRecipeDao.insert(recipe);
                    });

                    notifySuccess(callback);
                } else {
                    notifyError(callback, "Erro ao criar receita no servidor (" + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<RecipeMutationResponse> call, Throwable t) {
                notifyError(callback, "Erro de conexão: " + t.getMessage());
            }
        });
    }

    public void update(UserRecipeEntity recipe, @Nullable OperationCallback callback) {
        Map<String, Object> payload = buildUpdatePayload(recipe);

        apiService.updateRecipe(recipe.getId(), payload).enqueue(new Callback<RecipeMutationResponse>() {
            @Override
            public void onResponse(Call<RecipeMutationResponse> call, Response<RecipeMutationResponse> response) {
                if (response.isSuccessful()) {
                    executorService.execute(() -> {
                        UserRecipeEntity existing = userRecipeDao.getRecipeById(recipe.getId());
                        if (existing != null) {
                            recipe.setCreatedAt(existing.getCreatedAt());
                        }
                        recipe.setUpdatedAt(System.currentTimeMillis());
                        userRecipeDao.update(recipe);
                    });

                    notifySuccess(callback);
                } else {
                    notifyError(callback, "Erro ao atualizar receita no servidor (" + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<RecipeMutationResponse> call, Throwable t) {
                notifyError(callback, "Erro de conexão: " + t.getMessage());
            }
        });
    }

    public void delete(UserRecipeEntity recipe, @Nullable OperationCallback callback) {
        apiService.deleteRecipe(recipe.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    executorService.execute(() -> userRecipeDao.delete(recipe));
                    notifySuccess(callback);
                } else {
                    notifyError(callback, "Erro ao eliminar receita no servidor (" + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                notifyError(callback, "Erro de conexão: " + t.getMessage());
            }
        });
    }

    /**
     * Elimina uma receita pelo ID em background.
     */
    public void deleteById(long id) {
        executorService.execute(() -> userRecipeDao.deleteById(id));
    }

    private Map<String, Object> buildCreatePayload(UserRecipeEntity recipe) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", recipe.getTitle());
        payload.put("instructions", recipe.getInstructions());
        payload.put("image", emptyToNull(recipe.getImagePath()));
        payload.put("category_id", recipe.getCategoryId());
        payload.put("ingredients", parseIngredients(recipe.getIngredientsText()));
        payload.put("prep_time", recipe.getPrepTime());
        payload.put("cook_time", recipe.getCookTime());
        payload.put("servings", recipe.getServings());
        payload.put("difficulty", emptyToNull(recipe.getDifficulty()));
        payload.put("area", emptyToNull(recipe.getArea()));
        return payload;
    }

    private Map<String, Object> buildUpdatePayload(UserRecipeEntity recipe) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", recipe.getTitle());
        payload.put("instructions", recipe.getInstructions());
        payload.put("image", emptyToNull(recipe.getImagePath()));
        payload.put("category_id", recipe.getCategoryId());
        payload.put("ingredients", parseIngredients(recipe.getIngredientsText()));
        payload.put("prep_time", recipe.getPrepTime());
        payload.put("cook_time", recipe.getCookTime());
        payload.put("servings", recipe.getServings());
        payload.put("difficulty", emptyToNull(recipe.getDifficulty()));
        payload.put("area", emptyToNull(recipe.getArea()));
        return payload;
    }

    private List<Map<String, String>> parseIngredients(String ingredientsText) {
        List<Map<String, String>> ingredients = new ArrayList<>();
        if (ingredientsText == null || ingredientsText.trim().isEmpty()) {
            return ingredients;
        }

        String[] lines = ingredientsText.split("\\r?\\n");
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            Map<String, String> ingredient = new HashMap<>();
            ingredient.put("name", trimmed);
            ingredient.put("measure", "");
            ingredients.add(ingredient);
        }
        return ingredients;
    }

    @Nullable
    private String emptyToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private void notifySuccess(@Nullable OperationCallback callback) {
        if (callback == null) {
            return;
        }
        mainHandler.post(callback::onSuccess);
    }

    private void notifyError(@Nullable OperationCallback callback, String message) {
        if (callback == null) {
            return;
        }
        mainHandler.post(() -> callback.onError(message));
    }
}
