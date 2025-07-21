package com.example.numerology_prm392_group2.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(tableName = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String fullName;
    public String email;
    public String password;
}