package com.kabaddiarena.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "action_logs")
data class ActionLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val matchId: Int,
    val actionType: String, // "EMPTY_RAID", "TOUCH_POINT", "BONUS", "SUPER_TACKLE", "TACKLE_SUCCESS"
    val timestamp: Long = System.currentTimeMillis()
)