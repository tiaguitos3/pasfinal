package com.example.pas.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Base de dados Room para armazenamento local.
 * Usa o padrão Singleton para garantir uma única instância.
 */
@Database(entities = { UserRecipeEntity.class }, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "recipes_db";
    private static volatile AppDatabase INSTANCE;

    /**
     * Obtém o DAO para operações com receitas do utilizador.
     */
    public abstract UserRecipeDao userRecipeDao();

    /**
     * Obtém a instância da base de dados (Singleton thread-safe).
     */
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            DATABASE_NAME).build();
                }
            }
        }
        return INSTANCE;
    }
}
