package com.example.pas.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * DAO (Data Access Object) para operações CRUD nas receitas do utilizador.
 * Room gera automaticamente a implementação destes métodos.
 */
@Dao
public interface UserRecipeDao {

    /**
     * Obtém todas as receitas ordenadas por data de criação (mais recentes
     * primeiro).
     */
    @Query("SELECT * FROM user_recipes ORDER BY createdAt DESC")
    LiveData<List<UserRecipeEntity>> getAllRecipes();

    /**
     * Obtém uma receita específica pelo ID.
     */
    @Query("SELECT * FROM user_recipes WHERE id = :id")
    UserRecipeEntity getRecipeById(long id);

    /**
     * Insere uma nova receita na base de dados.
     * 
     * @return O ID da receita inserida
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(UserRecipeEntity recipe);

    /**
     * Atualiza uma receita existente.
     */
    @Update
    void update(UserRecipeEntity recipe);

    /**
     * Elimina uma receita.
     */
    @Delete
    void delete(UserRecipeEntity recipe);

    /**
     * Elimina uma receita pelo ID.
     */
    @Query("DELETE FROM user_recipes WHERE id = :id")
    void deleteById(long id);
}
