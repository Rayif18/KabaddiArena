package com.kabaddiarena.app.ui.stats

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.kabaddiarena.app.data.model.Match

class HeatmapView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val courtPaint = Paint().apply {
        color = Color.parseColor("#2E2E2E")
        style = Paint.Style.FILL
    }

    private val linePaint = Paint().apply {
        color = Color.parseColor("#F5C518")
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }

    private val raidPaint = Paint().apply {
        color = Color.parseColor("#4CAF50")
        style = Paint.Style.FILL
        alpha = 180
    }

    private val tacklePaint = Paint().apply {
        color = Color.parseColor("#FF5722")
        style = Paint.Style.FILL
        alpha = 180
    }

    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 28f
        textAlign = Paint.Align.CENTER
    }

    private var totalRaids = 0
    private var totalTackles = 0

    fun setMatches(matches: List<Match>) {
        totalRaids = matches.sumOf { it.touchPoints + it.bonusPoints + it.emptyRaids }
        totalTackles = matches.sumOf { it.tackleSuccess + it.superTackles }
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val w = width.toFloat()
        val h = height.toFloat()
        val padding = 20f

        // Court background
        canvas.drawRect(padding, padding, w - padding, h - padding, courtPaint)
        canvas.drawRect(padding, padding, w - padding, h - padding, linePaint)

        // Center line
        canvas.drawLine(padding, h / 2, w - padding, h / 2, linePaint)

        // Labels
        canvas.drawText("RAIDING HALF", w / 2, h / 4, textPaint)
        canvas.drawText("DEFENSIVE HALF", w / 2, h * 3 / 4, textPaint)

        // Raid dots in raiding half
        val raidCount = minOf(totalRaids, 20)
        for (i in 0 until raidCount) {
            val x = padding + 40f + (i % 5) * ((w - 2 * padding - 80f) / 4)
            val y = padding + 40f + (i / 5) * ((h / 2 - padding - 80f) / 4)
            canvas.drawCircle(x, y, 15f, raidPaint)
        }

        // Tackle dots in defensive half
        val tackleCount = minOf(totalTackles, 20)
        for (i in 0 until tackleCount) {
            val x = padding + 40f + (i % 5) * ((w - 2 * padding - 80f) / 4)
            val y = h / 2 + 40f + (i / 5) * ((h / 2 - padding - 80f) / 4)
            canvas.drawCircle(x, y, 15f, tacklePaint)
        }
    }
}