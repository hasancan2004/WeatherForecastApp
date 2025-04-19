package com.hasancankula.weatherapp.view

import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.hasancankula.weatherapp.model.TempPoint
import java.text.SimpleDateFormat
import java.util.*

class TemperatureGraphView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    var tempPoints: List<TempPoint> = listOf()
        set(value) {
            field = value
            requestLayout()
            invalidate()
        }

    private val barPaint = Paint().apply {
        color = Color.BLUE
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 48f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
        typeface = Typeface.DEFAULT_BOLD


    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val barWidthPx = 100  // Her çubuğa 100px yer
        val desiredWidth = tempPoints.size * barWidthPx
        val desiredHeight = 300
        val width = resolveSize(desiredWidth, widthMeasureSpec)
        val height = resolveSize(desiredHeight, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        
        if (tempPoints.isEmpty()) return

        val barWidth = width / tempPoints.size.toFloat()
        val maxTemp = tempPoints.maxOf { it.temperature }

        tempPoints.forEachIndexed { index, point ->
            val left = index * barWidth + barWidth * 0.1f
            val right = left + barWidth * 0.8f
            val top = height - (point.temperature / maxTemp * height * 0.8f)

            barPaint.color = when {
                point.temperature < 10 -> Color.parseColor("#2196F3") // Mavi
                point.temperature < 20 -> Color.parseColor("#4CAF50") // Yeşil
                point.temperature < 30 -> Color.parseColor("#FFC107") // Sarı
                else -> Color.parseColor("#F44336") // Kırmızı
            }


            canvas.drawRect(left, top, right, height.toFloat(), barPaint)
            canvas.drawText("${point.temperature.toInt()}°", left + barWidth * 0.4f, top - 10, textPaint)
        }
    }



    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val barWidth = width / tempPoints.size.toFloat()
            val index = (event.x / barWidth).toInt()

            if (index in tempPoints.indices) {
                val point = tempPoints[index]
                val formattedTime = formatHour(point.timestamp)
                val day = formatDay(point.timestamp)

                Toast.makeText(
                    context,
                    "$day, $formattedTime\n${point.temperature.toInt()}°C",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return true
    }

    private fun formatHour(timestamp: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val date = inputFormat.parse(timestamp)
            val outputFormat = SimpleDateFormat("HH.mm", Locale.getDefault())
            outputFormat.format(date ?: return "")
        } catch (e: Exception) {
            ""
        }
    }

    private fun formatDay(timestamp: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val date = inputFormat.parse(timestamp)
            val outputFormat = SimpleDateFormat("EEEE", Locale.getDefault()) // Örn: Wednesday
            outputFormat.format(date ?: return "")
        } catch (e: Exception) {
            ""
        }
    }
}
