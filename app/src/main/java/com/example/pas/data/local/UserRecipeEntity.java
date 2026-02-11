package com.example.pas.data.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entidade que representa uma receita criada pelo utilizador.
 * Esta classe é mapeada para uma tabela na base de dados Room.
 * Cada propriedade corresponde a uma coluna na tabela.
 */
@Entity(tableName = "user_recipes")
public class UserRecipeEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String title;
    private String instructions;
    private String imagePath; // URL da imagem (opcional)
    private Long categoryId;
    private String categoryName;
    private String difficulty;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String area;
    private String ingredientsText;
    private long createdAt;
    private long updatedAt;

    // Getters
    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public Integer getPrepTime() {
        return prepTime;
    }

    public Integer getCookTime() {
        return cookTime;
    }

    public Integer getServings() {
        return servings;
    }

    public String getArea() {
        return area;
    }

    public String getIngredientsText() {
        return ingredientsText;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setPrepTime(Integer prepTime) {
        this.prepTime = prepTime;
    }

    public void setCookTime(Integer cookTime) {
        this.cookTime = cookTime;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setIngredientsText(String ingredientsText) {
        this.ingredientsText = ingredientsText;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
