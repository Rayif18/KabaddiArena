package com.kabaddiarena.app.ui.logger

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.kabaddiarena.app.data.model.ActionLog
import com.kabaddiarena.app.data.model.Match
import com.kabaddiarena.app.databinding.ActivityLiveLoggerBinding
import com.kabaddiarena.app.ui.card.PerformanceCardActivity
import com.kabaddiarena.app.viewmodel.KabaddiViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LiveLoggerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLiveLoggerBinding
    private lateinit var viewModel: KabaddiViewModel

    private var matchId: Int = -1
    private var opponent: String = ""
    private var date: String = ""

    private var emptyRaids = 0
    private var touchPoints = 0
    private var bonusPoints = 0
    private var superTackles = 0
    private var tackleSuccess = 0
    private var totalRaidAttempts = 0
    private var totalTackleAttempts = 0

    private val actionStack = ArrayDeque<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLiveLoggerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[KabaddiViewModel::class.java]

        matchId = intent.getIntExtra("MATCH_ID", -1)
        opponent = intent.getStringExtra("OPPONENT") ?: ""
        date = intent.getStringExtra("DATE") ?: ""

        binding.tvOpponent.text = "vs $opponent"
        binding.tvDate.text = date

        updateUI()

        binding.btnEmptyRaid.setOnClickListener {
            animateButton(binding.btnEmptyRaid)
            emptyRaids++
            totalRaidAttempts++
            actionStack.addLast("EMPTY_RAID")
            logAction("EMPTY_RAID")
            updateUI()
        }

        binding.btnTouchPoint.setOnClickListener {
            animateButton(binding.btnTouchPoint)
            touchPoints++
            totalRaidAttempts++
            actionStack.addLast("TOUCH_POINT")
            logAction("TOUCH_POINT")
            updateUI()
        }

        binding.btnBonusPoint.setOnClickListener {
            animateButton(binding.btnBonusPoint)
            bonusPoints++
            totalRaidAttempts++
            actionStack.addLast("BONUS_POINT")
            logAction("BONUS_POINT")
            updateUI()
        }

        binding.btnSuperTackle.setOnClickListener {
            animateButton(binding.btnSuperTackle)
            superTackles++
            totalTackleAttempts++
            actionStack.addLast("SUPER_TACKLE")
            logAction("SUPER_TACKLE")
            updateUI()
        }

        binding.btnTackleSuccess.setOnClickListener {
            animateButton(binding.btnTackleSuccess)
            tackleSuccess++
            totalTackleAttempts++
            actionStack.addLast("TACKLE_SUCCESS")
            logAction("TACKLE_SUCCESS")
            updateUI()
        }

        binding.btnUndo.setOnClickListener {
            if (actionStack.isEmpty()) {
                Toast.makeText(this, "Nothing to undo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val last = actionStack.removeLast()
            undoAction(last)
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.undoLastAction(matchId)
            }
            updateUI()
            Toast.makeText(this, "Undone: $last", Toast.LENGTH_SHORT).show()
        }

        binding.btnEndMatch.setOnClickListener {
            saveMatchAndProceed()
        }
    }

    private fun logAction(type: String) {
        viewModel.logAction(ActionLog(matchId = matchId, actionType = type))
    }

    private fun undoAction(type: String) {
        when (type) {
            "EMPTY_RAID" -> { emptyRaids--; totalRaidAttempts-- }
            "TOUCH_POINT" -> { touchPoints--; totalRaidAttempts-- }
            "BONUS_POINT" -> { bonusPoints--; totalRaidAttempts-- }
            "SUPER_TACKLE" -> { superTackles--; totalTackleAttempts-- }
            "TACKLE_SUCCESS" -> { tackleSuccess--; totalTackleAttempts-- }
        }
    }

    private fun updateUI() {
        val totalPoints = touchPoints + bonusPoints + superTackles + tackleSuccess
        binding.tvTotalPoints.text = "Total Points: $totalPoints"

        val raidSuccess = if (totalRaidAttempts > 0)
            ((touchPoints + bonusPoints).toFloat() / totalRaidAttempts * 100).toInt()
        else -1

        // Super Tackle is also a successful tackle — counted in numerator
        val tackleSuccessRate = if (totalTackleAttempts > 0)
            ((tackleSuccess + superTackles).toFloat() / totalTackleAttempts * 100).toInt()
        else -1

        binding.tvRaidSuccess.text = "Raid Success: ${if (raidSuccess >= 0) "$raidSuccess%" else "N/A"}"
        binding.tvTackleSuccess.text = "Tackle Success: ${if (tackleSuccessRate >= 0) "$tackleSuccessRate%" else "N/A"}"

        binding.tvEmptyRaids.text = "Empty Raids: $emptyRaids"
        binding.tvTouchPoints.text = "Touch Points: $touchPoints"
        binding.tvBonusPoints.text = "Bonus Points: $bonusPoints"
        binding.tvSuperTackles.text = "Super Tackles: $superTackles"
        binding.tvTackleSuccessCount.text = "Tackle Success: $tackleSuccess"
    }

    private fun saveMatchAndProceed() {
        CoroutineScope(Dispatchers.IO).launch {
            val match = Match(
                id = matchId,
                playerId = 1,
                opponentTeam = opponent,
                matchDate = date,
                emptyRaids = emptyRaids,
                touchPoints = touchPoints,
                bonusPoints = bonusPoints,
                superTackles = superTackles,
                tackleSuccess = tackleSuccess,
                totalRaidAttempts = totalRaidAttempts,
                totalTackleAttempts = totalTackleAttempts
            )
            viewModel.updateMatch(match)
            withContext(Dispatchers.Main) {
                val intent = Intent(this@LiveLoggerActivity, PerformanceCardActivity::class.java)
                intent.putExtra("MATCH_ID", matchId)
                startActivity(intent)
            }
        }
    }

    private fun animateButton(button: Button) {
        button.animate()
            .scaleX(0.92f)
            .scaleY(0.92f)
            .setDuration(80)
            .withEndAction {
                button.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(80)
                    .start()
            }.start()
    }
}