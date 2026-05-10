package com.kabaddiarena.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.kabaddiarena.app.KabaddiApp
import com.kabaddiarena.app.data.model.ActionLog
import com.kabaddiarena.app.data.model.Match
import com.kabaddiarena.app.data.model.Player
import kotlinx.coroutines.launch

class KabaddiViewModel(application: Application) : AndroidViewModel(application) {

    private val db = (application as KabaddiApp).database
    private val playerDao = db.playerDao()
    private val matchDao = db.matchDao()
    private val actionLogDao = db.actionLogDao()

    // --- Player ---
    val player: LiveData<Player?> = playerDao.getPlayer()

    fun savePlayer(player: Player) = viewModelScope.launch {
        playerDao.insertPlayer(player)
    }

    // --- Match ---
    val allMatches: LiveData<List<Match>> = matchDao.getAllMatches()

    suspend fun insertMatch(match: Match): Long {
        return matchDao.insertMatch(match)
    }

    fun updateMatch(match: Match) = viewModelScope.launch {
        matchDao.updateMatch(match)
    }

    suspend fun getMatchById(matchId: Int): Match? {
        return matchDao.getMatchById(matchId)
    }

    suspend fun getAllMatchesOnce(): List<Match> {
        return matchDao.getAllMatchesOnce()
    }

    // --- Action Log (for Undo) ---
    fun logAction(action: ActionLog) = viewModelScope.launch {
        actionLogDao.insertAction(action)
    }

    suspend fun undoLastAction(matchId: Int) {
        actionLogDao.deleteLastAction(matchId)
    }

    suspend fun getActionsForMatch(matchId: Int): List<ActionLog> {
        return actionLogDao.getActionsForMatch(matchId)
    }
}