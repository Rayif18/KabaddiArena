package com.kabaddiarena.app.ui.match

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.kabaddiarena.app.databinding.ActivityMatchSetupBinding
import com.kabaddiarena.app.data.model.Match
import com.kabaddiarena.app.ui.logger.LiveLoggerActivity
import com.kabaddiarena.app.viewmodel.KabaddiViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MatchSetupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMatchSetupBinding
    private lateinit var viewModel: KabaddiViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchSetupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[KabaddiViewModel::class.java]

        val calendar = Calendar.getInstance()
        val today = "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH)+1}/${calendar.get(Calendar.YEAR)}"
        binding.etMatchDate.setText(today)

        binding.etMatchDate.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, day ->
                binding.etMatchDate.setText("$day/${month+1}/$year")
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.btnStartLogging.setOnClickListener {
            val opponent = binding.etOpponentTeam.text.toString().trim()
            val date = binding.etMatchDate.text.toString().trim()

            if (opponent.isEmpty()) {
                Toast.makeText(this, "Please enter opponent team name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                val match = Match(
                    playerId = 1,
                    opponentTeam = opponent,
                    matchDate = date
                )
                val matchId = viewModel.insertMatch(match)
                withContext(Dispatchers.Main) {
                    val intent = Intent(this@MatchSetupActivity, LiveLoggerActivity::class.java)
                    intent.putExtra("MATCH_ID", matchId.toInt())
                    intent.putExtra("OPPONENT", opponent)
                    intent.putExtra("DATE", date)
                    startActivity(intent)
                }
            }
        }
    }
}