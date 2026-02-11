package com.example.pas.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pas.R;
import com.example.pas.data.UserRecipeRepository;
import com.example.pas.data.local.UserRecipeEntity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * Activity que mostra as receitas criadas pelo utilizador.
 */
public class UserRecipesActivity extends AppCompatActivity implements UserRecipeAdapter.OnRecipeClickListener {

    private UserRecipeViewModel viewModel;
    private UserRecipeAdapter adapter;
    private TextView emptyTextView;

    private final ActivityResultLauncher<Intent> addEditLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // Dados são observados automaticamente via LiveData
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_recipes);

        // Inicializar views
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton fab = findViewById(R.id.fab);
        emptyTextView = findViewById(R.id.emptyText);

        // Configurar toolbar
        toolbar.setNavigationOnClickListener(v -> finish());

        // Configurar RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserRecipeAdapter(this, new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        // Inicializar ViewModel
        viewModel = new ViewModelProvider(this).get(UserRecipeViewModel.class);

        // Observar dados
        viewModel.getAllRecipes().observe(this, recipes -> {
            adapter.setRecipes(recipes);
            emptyTextView.setVisibility(recipes.isEmpty() ? View.VISIBLE : View.GONE);
        });

        // FAB para adicionar nova receita
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditRecipeActivity.class);
            addEditLauncher.launch(intent);
        });
    }

    @Override
    public void onEditClick(UserRecipeEntity recipe) {
        Intent intent = new Intent(this, AddEditRecipeActivity.class);
        intent.putExtra("RECIPE_ID", recipe.getId());
        intent.putExtra("RECIPE_TITLE", recipe.getTitle());
        intent.putExtra("RECIPE_INSTRUCTIONS", recipe.getInstructions());
        if (recipe.getImagePath() != null) {
            intent.putExtra("RECIPE_IMAGE", recipe.getImagePath());
        }
        if (recipe.getCategoryId() != null) {
            intent.putExtra("RECIPE_CATEGORY_ID", recipe.getCategoryId());
        }
        if (recipe.getCategoryName() != null) {
            intent.putExtra("RECIPE_CATEGORY_NAME", recipe.getCategoryName());
        }
        if (recipe.getDifficulty() != null) {
            intent.putExtra("RECIPE_DIFFICULTY", recipe.getDifficulty());
        }
        if (recipe.getPrepTime() != null) {
            intent.putExtra("RECIPE_PREP_TIME", recipe.getPrepTime());
        }
        if (recipe.getCookTime() != null) {
            intent.putExtra("RECIPE_COOK_TIME", recipe.getCookTime());
        }
        if (recipe.getServings() != null) {
            intent.putExtra("RECIPE_SERVINGS", recipe.getServings());
        }
        if (recipe.getArea() != null) {
            intent.putExtra("RECIPE_AREA", recipe.getArea());
        }
        if (recipe.getIngredientsText() != null) {
            intent.putExtra("RECIPE_INGREDIENTS", recipe.getIngredientsText());
        }
        addEditLauncher.launch(intent);
    }

    @Override
    public void onDeleteClick(UserRecipeEntity recipe) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Receita")
                .setMessage("Tens a certeza que queres eliminar \"" + recipe.getTitle() + "\"?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    viewModel.delete(recipe, new UserRecipeRepository.OperationCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(UserRecipesActivity.this, "Receita eliminada e sincronizada!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(UserRecipesActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    });
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
