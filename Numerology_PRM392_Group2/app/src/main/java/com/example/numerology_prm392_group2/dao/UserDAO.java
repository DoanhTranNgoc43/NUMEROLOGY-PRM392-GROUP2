package com.example.numerology_prm392_group2.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.numerology_prm392_group2.models.User;

@Dao
public interface UserDAO {
    @Insert
    void insert(User user);
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);
}
