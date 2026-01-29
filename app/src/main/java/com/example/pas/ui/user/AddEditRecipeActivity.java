package com.example.pas.ui.user;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.pas.R;
import com.example.pas.data.local.UserRecipeEntity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

/**
 * Activity para adicionar ou editar uma receita do utilizador.
 */
public class AddEditRecipeActivity extends AppCompatActivity {

    private UserRecipeViewModel viewModel;
    private TextInputEditText titleInput;
    private TextInputEditText instructionsInput;

    private long recipeId = -1;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_recipe);

        // Inicializar views
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        titleInput = findViewById(R.id.titleInput);
        instructionsInput = findViewById(R.id.instructionsInput);
        MaterialButton saveButton = findViewById(R.id.btnSave);

        // Verificar se é modo de edição
        if (getIntent().hasExtra("RECIPE_ID")) {
            isEditMode = true;
            recipeId = getIntent().getLongExtra("RECIPE_ID", -1);
            String title = getIntent().getStringExtra("RECIPE_TITLE");
            String instructions = getIntent().getStringExtra("RECIPE_INSTRUCTIONS");

            titleInput.setText(title);
            instructionsInput.setText(instructions);
            toolbar.setTitle("Editar Receita");
        } else {
            toolbar.setTitle("Nova Receita");
        }

        // Configurar toolbar
        toolbar.setNavigationOnClickListener(v -> finish());

        // Inicializar ViewModel
        viewModel = new ViewModelProvider(this).get(UserRecipeViewModel.class);

        // Configurar botão de guardar
        saveButton.setOnClickListener(v -> saveRecipe());
    }

    private void saveRecipe() {
        String title = titleInput.getText() != null ? titleInput.getText().toString().trim() : "";
        String instructions = instructionsInput.getText() != null ? instructionsInput.getText().toString().trim() : "";

        // Validação
        if (title.isEmpty()) {
            titleInput.setError("O título é obrigatório");
            return;
        }

        if (instructions.isEmpty()) {
            instructionsInput.setError("As instruções são obrigatórias");
            return;
        }

        UserRecipeEntity recipe = new UserRecipeEntity();
        recipe.setTitle(title);
        recipe.setInstructions(instructions);

        if (isEditMode) {
            recipe.setId(recipeId);
            viewModel.update(recipe);
            Toast.makeText(this, "Receita atualizada!", Toast.LENGTH_SHORT).show();
        } else {
            viewModel.insert(recipe);
            Toast.makeText(this, "Receita criada!", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}
