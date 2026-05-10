package com.kabaddiarena.app.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kabaddiarena.app.data.model.Player

@Dao
interface PlayerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayer(player: Player)

    @Update
    suspend fun updatePlayer(player: Player)

    @Query("SELECT * FROM players LIMIT 1")
    fun getPlayer(): LiveData<Player?>

    @Query("SELECT * FROM players LIMIT 1")
    suspend fun getPlayerOnce(): Player?
}