package com.kabaddiarena.app.ui.profile

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.kabaddiarena.app.data.model.Player
import com.kabaddiarena.app.databinding.ActivityPlayerProfileBinding
import com.kabaddiarena.app.viewmodel.KabaddiViewModel

class PlayerProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerProfileBinding
    private lateinit var viewModel: KabaddiViewModel
    private var existingPlayer: Player? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[KabaddiViewModel::class.java]

        viewModel.player.observe(this) { player ->
            if (player != null) {
                existingPlayer = player
                binding.etPlayerName.setText(player.name)
                binding.etTeamName.setText(player.teamName)
                when (player.position) {
                    "Raider" -> binding.rgPosition.check(binding.rbRaider.id)
                    "Defender" -> binding.rgPosition.check(binding.rbDefender.id)
                    "All-Rounder" -> binding.rgPosition.check(binding.rbAllRounder.id)
                }
            }
        }

        binding.btnSaveProfile.setOnClickListener {
            val name = binding.etPlayerName.text.toString().trim()
            val team = binding.etTeamName.text.toString().trim()
            val position = when (binding.rgPosition.checkedRadioButtonId) {
                binding.rbRaider.id -> "Raider"
                binding.rbDefender.id -> "Defender"
                binding.rbAllRounder.id -> "All-Rounder"
                else -> "Raider"
            }

            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val player = Player(
                id = existingPlayer?.id ?: 0,
                name = name,
                position = position,
                teamName = team
            )
            viewModel.savePlayer(player)
            Toast.makeText(this, "Profile saved!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}