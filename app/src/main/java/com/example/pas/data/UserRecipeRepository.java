package com.example.pas.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.pas.data.local.AppDatabase;
import com.example.pas.data.local.UserRecipeDao;
import com.example.pas.data.local.UserRecipeEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Repository que abstrai o acesso à base de dados local.
 * Executa operações de escrita em background thread.
 */
public class UserRecipeRepository {

    private final UserRecipeDao userRecipeDao;
    private final LiveData<List<UserRecipeEntity>> allRecipes;
    private final ExecutorService executorService;

    public UserRecipeRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        userRecipeDao = database.userRecipeDao();
        allRecipes = userRecipeDao.getAllRecipes();
        executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Obtém todas as receitas como LiveData.
     */
    public LiveData<List<UserRecipeEntity>> getAllRecipes() {
        return allRecipes;
    }

    /**
     * Insere uma nova receita em background.
     */
    public void insert(UserRecipeEntity recipe) {
        executorService.execute(() -> {
            recipe.setCreatedAt(System.currentTimeMillis());
            recipe.setUpdatedAt(System.currentTimeMillis());
            userRecipeDao.insert(recipe);
        });
    }

    /**
     * Atualiza uma receita existente em background.
     */
    public void update(UserRecipeEntity recipe) {
        executorService.execute(() -> {
            recipe.setUpdatedAt(System.currentTimeMillis());
            userRecipeDao.update(recipe);
        });
    }

    /**
     * Elimina uma receita em background.
     */
    public void delete(UserRecipeEntity recipe) {
        executorService.execute(() -> userRecipeDao.delete(recipe));
    }

    /**
     * Elimina uma receita pelo ID em background.
     */
    public void deleteById(long id) {
        executorService.execute(() -> userRecipeDao.deleteById(id));
    }
}
