package com.example.numerology_prm392_group2.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String fullName;
    public String email;
    public String password;

    // constructor, getter, setter
    public User() {

    }
    public User(String fullName, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }
}