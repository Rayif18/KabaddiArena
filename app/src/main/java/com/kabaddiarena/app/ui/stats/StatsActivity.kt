package com.kabaddiarena.app.ui.stats

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.kabaddiarena.app.databinding.ActivityStatsBinding
import com.kabaddiarena.app.data.model.Match
import com.kabaddiarena.app.ui.card.PerformanceCardActivity
import com.kabaddiarena.app.viewmodel.KabaddiViewModel

class StatsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStatsBinding
    private lateinit var viewModel: KabaddiViewModel
    private lateinit var matchAdapter: MatchHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[KabaddiViewModel::class.java]

        matchAdapter = MatchHistoryAdapter(emptyList()) { match ->
            val intent = Intent(this, PerformanceCardActivity::class.java)
            intent.putExtra("MATCH_ID", match.id)
            startActivity(intent)
        }
        binding.rvMatchHistory.layoutManager = LinearLayoutManager(this)
        binding.rvMatchHistory.adapter = matchAdapter

        viewModel.allMatches.observe(this) { matches ->
            if (matches.isNullOrEmpty()) {
                binding.tvNoMatches.visibility = View.VISIBLE
                binding.lineChart.visibility = View.GONE
            } else {
                binding.tvNoMatches.visibility = View.GONE
                binding.lineChart.visibility = View.VISIBLE
                setupChart(matches)
                matchAdapter.updateMatches(matches)
                binding.heatmapView.setMatches(matches)
            }
        }
    }

    private fun setupChart(matches: List<Match>) {
        val entries = matches.reversed().mapIndexed { index, match ->
            val total = (match.touchPoints + match.bonusPoints +
                    match.superTackles + match.tackleSuccess).toFloat()
            Entry(index.toFloat(), total)
        }

        val dataSet = LineDataSet(entries, "Total Points").apply {
            color = 0xFFF5C518.toInt()
            valueTextColor = 0xFFFFFFFF.toInt()
            lineWidth = 2f
            circleRadius = 4f
            setCircleColor(0xFFF5C518.toInt())
            mode = LineDataSet.Mode.CUBIC_BEZIER
        }

        binding.lineChart.apply {
            data = LineData(dataSet)
            setBackgroundColor(0xFF2E2E2E.toInt())
            description.isEnabled = false
            legend.textColor = 0xFFFFFFFF.toInt()
            xAxis.apply {
                textColor = 0xFFFFFFFF.toInt()
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
            }
            axisLeft.textColor = 0xFFFFFFFF.toInt()
            axisRight.isEnabled = false
            animateX(1000)
            invalidate()
        }
    }
}