package com.kabaddiarena.app.ui.card

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.kabaddiarena.app.databinding.ActivityPerformanceCardBinding
import com.kabaddiarena.app.ui.splash.SplashActivity
import com.kabaddiarena.app.viewmodel.KabaddiViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class PerformanceCardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPerformanceCardBinding
    private lateinit var viewModel: KabaddiViewModel
    private var matchId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerformanceCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[KabaddiViewModel::class.java]
        matchId = intent.getIntExtra("MATCH_ID", -1)

        loadMatchData()

        binding.btnShareCard.setOnClickListener {
            shareCardAsImage()
        }

        binding.btnSaveVideo.setOnClickListener {
            val link = binding.etVideoLink.text.toString().trim()
            if (link.isEmpty()) {
                Toast.makeText(this, "Enter a video link first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            CoroutineScope(Dispatchers.IO).launch {
                val match = viewModel.getMatchById(matchId)
                if (match != null) {
                    viewModel.updateMatch(match.copy(videoLink = link))
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@PerformanceCardActivity, "Video link saved!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.btnGoHome.setOnClickListener {
            val intent = Intent(this, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }

    private fun loadMatchData() {
        CoroutineScope(Dispatchers.IO).launch {
            val match = viewModel.getMatchById(matchId)
            val player = viewModel.getAllMatchesOnce()

            withContext(Dispatchers.Main) {
                if (match != null) {
                    val totalPoints = match.touchPoints + match.bonusPoints +
                            match.superTackles + match.tackleSuccess

                    val raidSuccess = if (match.totalRaidAttempts > 0)
                        ((match.touchPoints + match.bonusPoints).toFloat() /
                                match.totalRaidAttempts * 100).toInt()
                    else -1

                    val tackleRate = if (match.totalTackleAttempts > 0)
                        ((match.tackleSuccess + match.superTackles).toFloat() /
                                match.totalTackleAttempts * 100).toInt()
                    else -1

                    binding.tvCardOpponent.text = "vs ${match.opponentTeam}"
                    binding.tvCardDate.text = match.matchDate
                    binding.tvCardTotalPoints.text = "$totalPoints"
                    binding.tvCardRaidSuccess.text =
                        if (raidSuccess >= 0) "$raidSuccess%" else "N/A"
                    binding.tvCardTackleSuccess.text =
                        if (tackleRate >= 0) "$tackleRate%" else "N/A"

                    // Badge logic
                    val badge = when {
                        raidSuccess >= 60 -> "⭐ TOP RAIDER"
                        tackleRate >= 60 -> "🛡️ TOP DEFENDER"
                        totalPoints >= 10 -> "🏅 MATCH PLAYER"
                        else -> "🏉 KABADDI PLAYER"
                    }
                    binding.tvBadge.text = badge
                }
            }
        }
    }

    private fun shareCardAsImage() {
        val card = binding.cardPerformance
        val bitmap = Bitmap.createBitmap(card.width, card.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        card.draw(canvas)

        try {
            val filename = "KabaddiArena_${System.currentTimeMillis()}.png"
            val file = File(cacheDir, filename)
            file.outputStream().use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }

            val uri = FileProvider.getUriForFile(
                this,
                "${packageName}.fileprovider",
                file
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_TEXT, "Check out my Kabaddi performance! 🏉 #KabaddiArena")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(Intent.createChooser(shareIntent, "Share Performance Card via"))

        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}