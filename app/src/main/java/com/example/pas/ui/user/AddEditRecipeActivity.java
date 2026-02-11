package com.example.pas.ui.user;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.pas.R;
import com.example.pas.data.UserRecipeRepository;
import com.example.pas.data.local.UserRecipeEntity;
import com.example.pas.models.Category;
import com.example.pas.models.CategoryResponse;
import com.example.pas.network.ApiClient;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity para adicionar ou editar uma receita do utilizador.
 */
public class AddEditRecipeActivity extends AppCompatActivity {

    private UserRecipeViewModel viewModel;
    private TextInputLayout titleLayout;
    private TextInputLayout imageLayout;
    private TextInputLayout categoryLayout;
    private TextInputLayout instructionsLayout;
    private TextInputEditText titleInput;
    private TextInputEditText imageUrlInput;
    private MaterialAutoCompleteTextView categoryInput;
    private MaterialAutoCompleteTextView difficultyInput;
    private TextInputEditText prepTimeInput;
    private TextInputEditText cookTimeInput;
    private TextInputEditText servingsInput;
    private TextInputEditText areaInput;
    private TextInputEditText ingredientsInput;
    private TextInputEditText instructionsInput;
    private MaterialButton saveButton;

    private final List<Category> categories = new ArrayList<>();
    private final String[] difficultyOptions = {"Fácil", "Médio", "Difícil"};

    private Long selectedCategoryId;
    private String pendingCategoryName;
    private Long pendingCategoryId;

    private long recipeId = -1;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_recipe);

        // Inicializar views
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        titleLayout = findViewById(R.id.titleLayout);
        imageLayout = findViewById(R.id.imageLayout);
        categoryLayout = findViewById(R.id.categoryLayout);
        instructionsLayout = findViewById(R.id.instructionsLayout);
        titleInput = findViewById(R.id.titleInput);
        imageUrlInput = findViewById(R.id.imageUrlInput);
        categoryInput = findViewById(R.id.categoryInput);
        difficultyInput = findViewById(R.id.difficultyInput);
        prepTimeInput = findViewById(R.id.prepTimeInput);
        cookTimeInput = findViewById(R.id.cookTimeInput);
        servingsInput = findViewById(R.id.servingsInput);
        areaInput = findViewById(R.id.areaInput);
        ingredientsInput = findViewById(R.id.ingredientsInput);
        instructionsInput = findViewById(R.id.instructionsInput);
        saveButton = findViewById(R.id.btnSave);

        setupDifficultyDropdown();
        loadCategories();

        // Verificar se é modo de edição
        if (getIntent().hasExtra("RECIPE_ID")) {
            isEditMode = true;
            recipeId = getIntent().getLongExtra("RECIPE_ID", -1);
            String title = getIntent().getStringExtra("RECIPE_TITLE");
            String instructions = getIntent().getStringExtra("RECIPE_INSTRUCTIONS");
            String image = getIntent().getStringExtra("RECIPE_IMAGE");

            if (getIntent().hasExtra("RECIPE_CATEGORY_ID")) {
                long categoryId = getIntent().getLongExtra("RECIPE_CATEGORY_ID", -1L);
                pendingCategoryId = categoryId > 0 ? categoryId : null;
                selectedCategoryId = pendingCategoryId;
            }
            pendingCategoryName = getIntent().getStringExtra("RECIPE_CATEGORY_NAME");

            titleInput.setText(title);
            instructionsInput.setText(instructions);
            imageUrlInput.setText(image);
            difficultyInput.setText(getIntent().getStringExtra("RECIPE_DIFFICULTY"), false);
            areaInput.setText(getIntent().getStringExtra("RECIPE_AREA"));
            ingredientsInput.setText(getIntent().getStringExtra("RECIPE_INGREDIENTS"));

            if (getIntent().hasExtra("RECIPE_PREP_TIME")) {
                prepTimeInput.setText(String.valueOf(getIntent().getIntExtra("RECIPE_PREP_TIME", 0)));
            }
            if (getIntent().hasExtra("RECIPE_COOK_TIME")) {
                cookTimeInput.setText(String.valueOf(getIntent().getIntExtra("RECIPE_COOK_TIME", 0)));
            }
            if (getIntent().hasExtra("RECIPE_SERVINGS")) {
                servingsInput.setText(String.valueOf(getIntent().getIntExtra("RECIPE_SERVINGS", 0)));
            }
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
        clearErrors();

        String title = getText(titleInput);
        String imageUrl = getText(imageUrlInput);
        String instructions = getText(instructionsInput);
        String area = getText(areaInput);
        String difficulty = getText(difficultyInput);
        String ingredientsText = getText(ingredientsInput);

        Integer prepTime;
        Integer cookTime;
        Integer servings;

        try {
            prepTime = parseOptionalPositiveInteger(prepTimeInput);
            cookTime = parseOptionalPositiveInteger(cookTimeInput);
            servings = parseOptionalPositiveInteger(servingsInput);
        } catch (NumberFormatException ex) {
            Toast.makeText(this, "Use apenas números positivos nos campos de tempo/porções.", Toast.LENGTH_LONG).show();
            return;
        }

        // Validação
        if (title.isEmpty()) {
            titleLayout.setError("O nome da receita é obrigatório");
            return;
        }

        if (imageUrl.isEmpty()) {
            imageLayout.setError("A URL da imagem é obrigatória");
            return;
        }

        if (selectedCategoryId == null) {
            categoryLayout.setError("A categoria é obrigatória");
            return;
        }

        if (instructions.isEmpty()) {
            instructionsLayout.setError("As instruções são obrigatórias");
            return;
        }

        UserRecipeEntity recipe = new UserRecipeEntity();
        recipe.setTitle(title);
        recipe.setInstructions(instructions);
        recipe.setImagePath(imageUrl);
        recipe.setCategoryId(selectedCategoryId);
        recipe.setCategoryName(getText(categoryInput));
        recipe.setDifficulty(difficulty.isEmpty() ? null : difficulty);
        recipe.setPrepTime(prepTime);
        recipe.setCookTime(cookTime);
        recipe.setServings(servings);
        recipe.setArea(area.isEmpty() ? null : area);
        recipe.setIngredientsText(ingredientsText.isEmpty() ? null : ingredientsText);

        saveButton.setEnabled(false);

        if (isEditMode) {
            recipe.setId(recipeId);
            viewModel.update(recipe, new UserRecipeRepository.OperationCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(AddEditRecipeActivity.this, "Receita atualizada e sincronizada!", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onError(String message) {
                    saveButton.setEnabled(true);
                    Toast.makeText(AddEditRecipeActivity.this, message, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            viewModel.insert(recipe, new UserRecipeRepository.OperationCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(AddEditRecipeActivity.this, "Receita criada e sincronizada!", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onError(String message) {
                    saveButton.setEnabled(true);
                    Toast.makeText(AddEditRecipeActivity.this, message, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void setupDifficultyDropdown() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, difficultyOptions);
        difficultyInput.setAdapter(adapter);
        difficultyInput.setOnClickListener(v -> difficultyInput.showDropDown());
    }

    private void loadCategories() {
        ApiClient.getApiService().getCategories().enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (!response.isSuccessful() || response.body() == null || response.body().getCategories() == null) {
                    Toast.makeText(AddEditRecipeActivity.this, "Não foi possível carregar categorias.", Toast.LENGTH_LONG).show();
                    return;
                }

                categories.clear();
                categories.addAll(response.body().getCategories());
                List<String> categoryNames = new ArrayList<>();
                for (Category category : categories) {
                    categoryNames.add(category.getName());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddEditRecipeActivity.this, android.R.layout.simple_list_item_1, categoryNames);
                categoryInput.setAdapter(adapter);
                categoryInput.setOnClickListener(v -> categoryInput.showDropDown());
                categoryInput.setOnItemClickListener((parent, view, position, id) -> {
                    Category selected = categories.get(position);
                    selectedCategoryId = selected.getId();
                    categoryLayout.setError(null);
                });

                applyPendingCategorySelection();
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                Toast.makeText(AddEditRecipeActivity.this, "Erro ao carregar categorias: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void applyPendingCategorySelection() {
        if (pendingCategoryId != null) {
            for (Category category : categories) {
                if (category.getId() == pendingCategoryId) {
                    selectedCategoryId = category.getId();
                    categoryInput.setText(category.getName(), false);
                    return;
                }
            }
        }

        if (pendingCategoryName != null && !pendingCategoryName.trim().isEmpty()) {
            categoryInput.setText(pendingCategoryName, false);
        }
    }

    private void clearErrors() {
        titleLayout.setError(null);
        imageLayout.setError(null);
        categoryLayout.setError(null);
        instructionsLayout.setError(null);
    }

    private String getText(TextInputEditText input) {
        return input.getText() != null ? input.getText().toString().trim() : "";
    }

    private String getText(MaterialAutoCompleteTextView input) {
        return input.getText() != null ? input.getText().toString().trim() : "";
    }

    @Nullable
    private Integer parseOptionalPositiveInteger(TextInputEditText input) throws NumberFormatException {
        String value = getText(input);
        if (value.isEmpty()) {
            return null;
        }
        int parsed = Integer.parseInt(value);
        if (parsed < 0) {
            throw new NumberFormatException("negative value");
        }
        return parsed;
    }
}
