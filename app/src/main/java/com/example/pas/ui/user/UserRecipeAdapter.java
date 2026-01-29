package com.example.pas.ui.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pas.R;
import com.example.pas.data.local.UserRecipeEntity;

import java.util.List;

/**
 * Adapter para a lista de receitas do utilizador.
 */
public class UserRecipeAdapter extends RecyclerView.Adapter<UserRecipeAdapter.ViewHolder> {

    public interface OnRecipeClickListener {
        void onEditClick(UserRecipeEntity recipe);

        void onDeleteClick(UserRecipeEntity recipe);
    }

    private final Context context;
    private List<UserRecipeEntity> recipes;
    private final OnRecipeClickListener listener;

    public UserRecipeAdapter(Context context, List<UserRecipeEntity> recipes, OnRecipeClickListener listener) {
        this.context = context;
        this.recipes = recipes;
        this.listener = listener;
    }

    public void setRecipes(List<UserRecipeEntity> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserRecipeEntity recipe = recipes.get(position);
        holder.bind(recipe);
    }

    @Override
    public int getItemCount() {
        return recipes != null ? recipes.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleTextView;
        private final TextView instructionsTextView;
        private final ImageButton editButton;
        private final ImageButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.recipeTitle);
            instructionsTextView = itemView.findViewById(R.id.recipeInstructions);
            editButton = itemView.findViewById(R.id.btnEdit);
            deleteButton = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(UserRecipeEntity recipe) {
            titleTextView.setText(recipe.getTitle());

            // Mostrar primeira linha das instruções
            String instructions = recipe.getInstructions();
            if (instructions != null && instructions.length() > 100) {
                instructions = instructions.substring(0, 100) + "...";
            }
            instructionsTextView.setText(instructions);

            editButton.setOnClickListener(v -> listener.onEditClick(recipe));
            deleteButton.setOnClickListener(v -> listener.onDeleteClick(recipe));
        }
    }
}
