package com.kabaddiarena.app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kabaddiarena.app.data.dao.ActionLogDao
import com.kabaddiarena.app.data.dao.MatchDao
import com.kabaddiarena.app.data.dao.PlayerDao
import com.kabaddiarena.app.data.model.ActionLog
import com.kabaddiarena.app.data.model.Match
import com.kabaddiarena.app.data.model.Player

@Database(
    entities = [Player::class, Match::class, ActionLog::class],
    version = 1,
    exportSchema = false
)
abstract class KabaddiDatabase : RoomDatabase() {

    abstract fun playerDao(): PlayerDao
    abstract fun matchDao(): MatchDao
    abstract fun actionLogDao(): ActionLogDao

    companion object {
        @Volatile
        private var INSTANCE: KabaddiDatabase? = null

        fun getDatabase(context: Context): KabaddiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KabaddiDatabase::class.java,
                    "kabaddi_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}