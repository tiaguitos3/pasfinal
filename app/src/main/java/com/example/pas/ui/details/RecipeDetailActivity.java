package com.example.pas.ui.details;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.pas.R;
import com.google.android.material.appbar.MaterialToolbar;

/**
 * Activity que mostra os detalhes completos de uma receita.
 */
public class RecipeDetailActivity extends AppCompatActivity {

    private RecipeDetailViewModel viewModel;
    private ImageView recipeImage;
    private TextView recipeName;
    private TextView recipeIngredients;
    private TextView recipeInstructions;
    private TextView recipeInfo;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // Obter dados do Intent
        String recipeId = getIntent().getStringExtra("RECIPE_ID");
        String recipeTitleExtra = getIntent().getStringExtra("RECIPE_NAME");

        // Inicializar views
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        recipeImage = findViewById(R.id.recipeImage);
        recipeName = findViewById(R.id.recipeName);
        recipeIngredients = findViewById(R.id.recipeIngredients);
        recipeInstructions = findViewById(R.id.recipeInstructions);
        recipeInfo = findViewById(R.id.recipeInfo);
        progressBar = findViewById(R.id.progressBar);

        // Configurar toolbar
        toolbar.setTitle(recipeTitleExtra != null ? recipeTitleExtra : "Detalhes");
        toolbar.setNavigationOnClickListener(v -> finish());

        // Inicializar ViewModel
        viewModel = new ViewModelProvider(this).get(RecipeDetailViewModel.class);

        // Observar dados
        viewModel.getRecipeDetail().observe(this, detail -> {
            if (detail != null) {
                recipeName.setText(detail.getName());
                recipeIngredients.setText(detail.getFormattedIngredients());
                recipeInstructions.setText(detail.getInstructions());

                // Info adicional
                StringBuilder info = new StringBuilder();
                if (detail.getDifficulty() != null) {
                    info.append("📊 ").append(detail.getDifficulty());
                }
                if (detail.getPrepTime() != null) {
                    info.append("  ⏱️ ").append(detail.getPrepTime()).append(" min prep");
                }
                if (detail.getCookTime() != null) {
                    info.append(" + ").append(detail.getCookTime()).append(" min");
                }
                if (detail.getServings() != null) {
                    info.append("  👥 ").append(detail.getServings()).append(" porções");
                }
                recipeInfo.setText(info.toString());

                // Carregar imagem
                Glide.with(this)
                        .load(detail.getImageUrl())
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.placeholder_image)
                                .error(R.drawable.placeholder_image)
                                .transform(new RoundedCorners(24)))
                        .into(recipeImage);
            }
        });

        viewModel.getIsLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        // Carregar detalhes
        if (recipeId != null) {
            viewModel.fetchRecipeDetails(recipeId);
        }
    }
}
