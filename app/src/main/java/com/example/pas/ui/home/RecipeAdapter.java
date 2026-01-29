package com.example.pas.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.pas.R;
import com.example.pas.models.Recipe;
import com.example.pas.ui.details.RecipeDetailActivity;

import java.util.List;

/**
 * Adapter para a RecyclerView que mostra a lista de receitas.
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private final Context context;
    private List<Recipe> recipes;

    public RecipeAdapter(Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.bind(recipe);
    }

    @Override
    public int getItemCount() {
        return recipes != null ? recipes.size() : 0;
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final TextView nameTextView;
        private final TextView categoryTextView;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recipeImage);
            nameTextView = itemView.findViewById(R.id.recipeName);
            categoryTextView = itemView.findViewById(R.id.recipeCategory);
        }

        public void bind(Recipe recipe) {
            nameTextView.setText(recipe.getName());

            String subtitle = recipe.getCategory();
            if (recipe.getArea() != null && !recipe.getArea().isEmpty()) {
                subtitle = recipe.getArea();
            }
            categoryTextView.setText(subtitle);

            // Carregar imagem com Glide
            Glide.with(context)
                    .load(recipe.getImageUrl())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.placeholder_image)
                            .error(R.drawable.placeholder_image)
                            .transform(new RoundedCorners(16)))
                    .into(imageView);

            // Click para ver detalhes
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, RecipeDetailActivity.class);
                intent.putExtra("RECIPE_ID", recipe.getId());
                intent.putExtra("RECIPE_NAME", recipe.getName());
                context.startActivity(intent);
            });
        }
    }
}
