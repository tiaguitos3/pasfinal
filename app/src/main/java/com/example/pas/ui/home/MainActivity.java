package com.example.pas.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pas.R;
import com.example.pas.ui.user.UserRecipesActivity;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

/**
 * Activity principal da aplicação.
 * Mostra uma lista de receitas vindas da API em formato de grelha.
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecipeAdapter adapter;
    private RecipeViewModel viewModel;
    private ProgressBar progressBar;
    private TextView errorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar views
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        errorTextView = findViewById(R.id.errorText);
        MaterialButton btnMyRecipes = findViewById(R.id.btnMyRecipes);

        // Configurar RecyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new RecipeAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Inicializar ViewModel
        viewModel = new ViewModelProvider(this).get(RecipeViewModel.class);

        // Observar dados
        viewModel.getRecipes().observe(this, recipes -> {
            if (recipes != null) {
                adapter.setRecipes(recipes);
            }
        });

        viewModel.getIsLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                errorTextView.setText(error);
                errorTextView.setVisibility(View.VISIBLE);
            } else {
                errorTextView.setVisibility(View.GONE);
            }
        });

        // Botão para ir às receitas do utilizador
        btnMyRecipes.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserRecipesActivity.class);
            startActivity(intent);
        });

        // Carregar receitas
        viewModel.fetchRecipes();
    }
}
