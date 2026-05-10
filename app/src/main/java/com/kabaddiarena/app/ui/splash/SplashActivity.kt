package com.kabaddiarena.app.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.kabaddiarena.app.databinding.ActivitySplashBinding
import com.kabaddiarena.app.ui.match.MatchSetupActivity
import com.kabaddiarena.app.ui.profile.PlayerProfileActivity
import com.kabaddiarena.app.ui.stats.StatsActivity
import com.kabaddiarena.app.viewmodel.KabaddiViewModel

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var viewModel: KabaddiViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[KabaddiViewModel::class.java]

        binding.btnNewMatch.setOnClickListener {
            startActivity(Intent(this, MatchSetupActivity::class.java))
        }

        binding.btnViewStats.setOnClickListener {
            startActivity(Intent(this, StatsActivity::class.java))
        }

        binding.btnProfile.setOnClickListener {
            startActivity(Intent(this, PlayerProfileActivity::class.java))
        }
    }
}