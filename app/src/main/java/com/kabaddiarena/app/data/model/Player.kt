package com.kabaddiarena.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "players")
data class Player(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val position: String, // "Raider", "Defender", "All-Rounder"
    val teamName: String
)