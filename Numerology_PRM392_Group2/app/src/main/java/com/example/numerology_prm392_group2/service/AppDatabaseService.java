package com.example.numerology_prm392_group2.service;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.numerology_prm392_group2.dao.UserDAO;
import com.example.numerology_prm392_group2.models.User;

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class AppDatabaseService extends RoomDatabase {
    private static volatile AppDatabaseService INSTANCE;

    public abstract UserDAO userDao();

    public static AppDatabaseService getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabaseService.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabaseService.class, "NUMEROLOGY"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }

}
