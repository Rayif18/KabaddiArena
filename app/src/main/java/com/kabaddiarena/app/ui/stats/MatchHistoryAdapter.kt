package com.kabaddiarena.app.ui.stats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kabaddiarena.app.R
import com.kabaddiarena.app.data.model.Match

class MatchHistoryAdapter(
    private var matches: List<Match>,
    private val onMatchClick: (Match) -> Unit
) : RecyclerView.Adapter<MatchHistoryAdapter.MatchViewHolder>() {

    class MatchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvOpponent: TextView = view.findViewById(R.id.tvItemOpponent)
        val tvDate: TextView = view.findViewById(R.id.tvItemDate)
        val tvPoints: TextView = view.findViewById(R.id.tvItemPoints)
        val tvRaid: TextView = view.findViewById(R.id.tvItemRaid)
        val tvVideo: TextView = view.findViewById(R.id.tvItemVideo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_match, parent, false)
        return MatchViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        val match = matches[position]
        val total = match.touchPoints + match.bonusPoints +
                match.superTackles + match.tackleSuccess
        val raidSuccess = if (match.totalRaidAttempts > 0)
            ((match.touchPoints + match.bonusPoints).toFloat() /
                    match.totalRaidAttempts * 100).toInt()
        else -1
        val tackleRate = if (match.totalTackleAttempts > 0)
            ((match.tackleSuccess + match.superTackles).toFloat() /
                    match.totalTackleAttempts * 100).toInt()
        else -1

        holder.tvOpponent.text = "vs ${match.opponentTeam}"
        holder.tvDate.text = match.matchDate
        holder.tvPoints.text = "Points: $total"
        holder.tvRaid.text = "Raid: ${if (raidSuccess >= 0) "$raidSuccess%" else "N/A"} | Tackle: ${if (tackleRate >= 0) "$tackleRate%" else "N/A"}"
        holder.tvVideo.visibility = if (match.videoLink.isNotEmpty()) View.VISIBLE else View.GONE
        holder.itemView.setOnClickListener { onMatchClick(match) }
    }

    override fun getItemCount() = matches.size

    fun updateMatches(newMatches: List<Match>) {
        matches = newMatches
        notifyDataSetChanged()
    }
}