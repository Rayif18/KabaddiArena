package com.kabaddiarena.app.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kabaddiarena.app.data.model.Match

@Dao
interface MatchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatch(match: Match): Long

    @Update
    suspend fun updateMatch(match: Match)

    @Query("SELECT * FROM matches ORDER BY timestamp DESC")
    fun getAllMatches(): LiveData<List<Match>>

    @Query("SELECT * FROM matches ORDER BY timestamp DESC")
    suspend fun getAllMatchesOnce(): List<Match>

    @Query("SELECT * FROM matches WHERE id = :matchId")
    suspend fun getMatchById(matchId: Int): Match?
}