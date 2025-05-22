package com.example.practica_ninja

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import kotlin.math.hypot

class Grafics(private val view: View, val drawable: Drawable) {

    companion object {
        const val MAX_VELOCITAT = 20
    }

    var posX: Double = 0.0
    var posY: Double = 0.0
    var incX: Double = 0.0
    var incY: Double = 0.0
    var angle: Int = 0
    var rotacio: Int = 0
    val amplada: Int = drawable.intrinsicWidth
    val altura: Int = drawable.intrinsicHeight

    fun dibuixaGrafic(canvas: Canvas) {
        canvas.save()

        val x = (posX + amplada / 2).toInt()
        val y = (posY + altura / 2).toInt()
        canvas.rotate(angle.toFloat(), x.toFloat(), y.toFloat())

        drawable.setBounds(posX.toInt(), posY.toInt(), posX.toInt() + amplada, posY.toInt() + altura)
        drawable.draw(canvas)

        canvas.restore()

        val rInval = (hypot(amplada.toDouble(), altura.toDouble()) / 2 + MAX_VELOCITAT).toInt()
        view.invalidate(x - rInval, y - rInval, x + rInval, y + rInval)
    }

    fun incrementaPos(factor: Double) {
        posX += incX * factor
        if (posX < -amplada / 2) posX = view.width - amplada / 2.0
        if (posX > view.width - amplada / 2) posX = -amplada / 2.0

        posY += incY * factor
        if (posY < -altura / 2) posY = view.height - altura / 2.0
        if (posY > view.height - altura / 2) posY = -altura / 2.0

        angle = (angle + (rotacio * factor).toInt()) % 360
    }

    fun distancia(g: Grafics): Double {
        return hypot(posX - g.posX, posY - g.posY)
    }
}
