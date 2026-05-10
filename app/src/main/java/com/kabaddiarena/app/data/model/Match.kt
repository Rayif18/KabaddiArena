package com.kabaddiarena.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "matches")
data class Match(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val playerId: Int,
    val opponentTeam: String,
    val matchDate: String,
    val emptyRaids: Int = 0,
    val touchPoints: Int = 0,
    val bonusPoints: Int = 0,
    val superTackles: Int = 0,
    val tackleSuccess: Int = 0,
    val totalRaidAttempts: Int = 0,
    val totalTackleAttempts: Int = 0,
    val videoLink: String = "",
    val timestamp: Long = System.currentTimeMillis()
)