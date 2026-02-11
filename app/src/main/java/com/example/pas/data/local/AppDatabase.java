package com.example.pas.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * Base de dados Room para armazenamento local.
 * Usa o padrão Singleton para garantir uma única instância.
 */
@Database(entities = { UserRecipeEntity.class }, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "recipes_db";
    private static volatile AppDatabase INSTANCE;
    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE user_recipes ADD COLUMN categoryId INTEGER");
            database.execSQL("ALTER TABLE user_recipes ADD COLUMN categoryName TEXT");
            database.execSQL("ALTER TABLE user_recipes ADD COLUMN difficulty TEXT");
            database.execSQL("ALTER TABLE user_recipes ADD COLUMN prepTime INTEGER");
            database.execSQL("ALTER TABLE user_recipes ADD COLUMN cookTime INTEGER");
            database.execSQL("ALTER TABLE user_recipes ADD COLUMN servings INTEGER");
            database.execSQL("ALTER TABLE user_recipes ADD COLUMN area TEXT");
            database.execSQL("ALTER TABLE user_recipes ADD COLUMN ingredientsText TEXT");
        }
    };

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
                            DATABASE_NAME)
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
