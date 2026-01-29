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
    private String imagePath; // Caminho local da imagem (opcional)
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

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
