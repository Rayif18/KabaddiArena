package com.kabaddiarena.app.data.dao

import androidx.room.*
import com.kabaddiarena.app.data.model.ActionLog

@Dao
interface ActionLogDao {

    @Insert
    suspend fun insertAction(action: ActionLog)

    @Query("SELECT * FROM action_logs WHERE matchId = :matchId ORDER BY timestamp DESC")
    suspend fun getActionsForMatch(matchId: Int): List<ActionLog>

    @Query("DELETE FROM action_logs WHERE id = (SELECT id FROM action_logs WHERE matchId = :matchId ORDER BY timestamp DESC LIMIT 1)")
    suspend fun deleteLastAction(matchId: Int)
}