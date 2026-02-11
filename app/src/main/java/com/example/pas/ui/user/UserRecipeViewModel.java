package com.example.pas.ui.user;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.pas.data.UserRecipeRepository;
import com.example.pas.data.local.UserRecipeEntity;

import java.util.List;

/**
 * ViewModel para receitas do utilizador (locais).
 * Usa AndroidViewModel para ter acesso ao Application context.
 */
public class UserRecipeViewModel extends AndroidViewModel {

    private final UserRecipeRepository repository;
    private final LiveData<List<UserRecipeEntity>> allRecipes;

    public UserRecipeViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRecipeRepository(application);
        allRecipes = repository.getAllRecipes();
    }

    public LiveData<List<UserRecipeEntity>> getAllRecipes() {
        return allRecipes;
    }

    public void insert(UserRecipeEntity recipe, UserRecipeRepository.OperationCallback callback) {
        repository.insert(recipe, callback);
    }

    public void update(UserRecipeEntity recipe, UserRecipeRepository.OperationCallback callback) {
        repository.update(recipe, callback);
    }

    public void delete(UserRecipeEntity recipe, UserRecipeRepository.OperationCallback callback) {
        repository.delete(recipe, callback);
    }

    public void deleteById(long id) {
        repository.deleteById(id);
    }
}
