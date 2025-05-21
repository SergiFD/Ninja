package com.example.practica_ninja

import ThreadJoc
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import kotlin.math.abs
import kotlin.math.roundToInt


class VistaJoc(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val objectius = mutableListOf<Grafics>()
    private val drawableEnemic: Drawable
    private val drawableNinja: Drawable
    private val ninja: Grafics
    private var partidaAcabada = false
    private var score = 0
    private var partidaDialogMostrat = false
    private var tvScore: TextView? = null

    // ////// NINJA ////////
    private var girNinja = 0
    private var acceleracioNinja = 0f
    private val INC_GIR = 5
    private val INC_ACCELERACIO = 0.5f

    //Ganivet//
    private val ganivets = mutableListOf<Grafics>()
    private val drawableGanivet: Drawable
    private lateinit var ganivet: Grafics
    private val INC_VELOCITAT_GANIVET = 12
    private var ganivetActiu = false
    private val tempsGanivet = 0

    // ////// THREAD ////////
    private lateinit var thread: ThreadJoc
    private val PERIODE_PROCES = 50F
    private var ultimProces = 0L

    //Trocitos enemigo //
    private val drawableObjectiu = arrayOfNulls<Drawable>(3)


    init {
        tvScore = findViewById(R.id.tvScore)
        val prefs = context.getSharedPreferences("com.example.practica_ninja_preferences", Context.MODE_PRIVATE)
        val numEnemics = prefs.getString("enemics", "5")?.toIntOrNull() ?: 5

        drawableEnemic = AppCompatResources.getDrawable(context, R.drawable.ninja_enemic)
            ?: throw IllegalArgumentException("Falta ninja_enemic.png en drawable")

        val ninjaImageResId = when (prefs.getString("disseny_personatge", "1") ?: "1") {
            "1" -> R.drawable.ninja01
            "2" -> R.drawable.ninja02
            "3" -> R.drawable.ninja03
            else -> R.drawable.ninja01
        }

        drawableNinja = AppCompatResources.getDrawable(context, ninjaImageResId)
            ?: throw IllegalArgumentException("Falta ninja drawable en drawable")
        drawableGanivet = AppCompatResources.getDrawable(context, R.drawable.ganivet)
            ?: throw IllegalArgumentException("Falta ganivet.png en drawable")


        ninja = Grafics(this, drawableNinja)

        repeat(numEnemics) {
            val enemic = Grafics(this, drawableEnemic).apply {
                incX = Math.random() * 4 - 2
                incY = Math.random() * 4 - 2
                angle = (Math.random() * 360).toInt()
                rotacio = (Math.random() * 8 - 4).toInt()
            }
            objectius.add(enemic)
        }

        drawableObjectiu?.set(0, context.getResources().getDrawable(R.drawable.cap_ninja, null)); //cap
        drawableObjectiu?.set(1, context.getResources().getDrawable(R.drawable.cos_ninja, null)); //cos
        drawableObjectiu?.set(2, context.getResources().getDrawable(R.drawable.cua_ninja, null)); //cua?
    }

    private fun disparaGanivet() {
        ganivet = Grafics(this, drawableGanivet)
        ganivet.posX = ninja.posX + ninja.amplada / 2 - ganivet.amplada / 2
        ganivet.posY = ninja.posY + ninja.altura / 2 - ganivet.altura / 2
        ganivet.angle = ninja.angle
        ganivet.incX = INC_VELOCITAT_GANIVET * Math.cos(Math.toRadians(ninja.angle.toDouble()))
        ganivet.incY = INC_VELOCITAT_GANIVET * Math.sin(Math.toRadians(ninja.angle.toDouble()))
        ganivets.add(ganivet)
    }


    override fun onSizeChanged(ancho: Int, alto: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(ancho, alto, oldw, oldh)

    ninja.posX = (ancho / 2 - ninja.amplada / 2).toDouble()
    ninja.posY = (alto / 2 - ninja.altura / 2).toDouble()

    for (obj in objectius) {
        do {
            obj.posX = Math.random() * (ancho - obj.amplada)
            obj.posY = Math.random() * (alto - obj.altura)
        } while (obj.distancia(ninja) < (ancho + alto) / 5)
    }

    ultimProces = System.currentTimeMillis()
    thread = ThreadJoc(this)
    thread.start()
}

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        ganivets.forEach { it.dibuixaGrafic(canvas) }
        objectius.forEach { it.dibuixaGrafic(canvas) }
        ninja.dibuixaGrafic(canvas)
    }


    private var mX = 0f
    private var mY = 0f
    private var llancament = false

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                llancament = true
            }

            MotionEvent.ACTION_MOVE -> {
                val dx = abs(x - mX)
                val dy = abs(y - mY)

                if (dy < 6 && dx > 6) {
                    girNinja = ((x - mX) / 2).roundToInt()
                    llancament = false
                } else if (dx < 6 && dy > 6) {
                    acceleracioNinja = ((mY - y) / 25).roundToInt().toFloat()
                    llancament = false
                }
            }

            MotionEvent.ACTION_UP -> {
                girNinja = 0
                acceleracioNinja = 0f
                if (llancament) {
                    // disparaGanivet()
                    disparaGanivet()
                }
            }
        }

        mX = x
        mY = y
        return true
    }


    private fun destrueixObjectiu(i: Int) {
        val objectiuOriginal = objectius[i]

        if (objectiuOriginal.drawable == drawableEnemic) {
            score += 100  // +100 per enemic destruït
            actualitzaScore()
            val numParts = 3
            for (n in 0 until numParts) {
                drawableObjectiu.getOrNull(n)?.let { drawable ->
                    val fragment = Grafics(this, drawable).apply {
                        posX = objectiuOriginal.posX
                        posY = objectiuOriginal.posY
                        incX = Math.random() * 7 - 3
                        incY = Math.random() * 7 - 3
                        angle = (Math.random() * 360).toInt()
                        rotacio = (Math.random() * 8 - 4).toInt()
                    }
                    objectius.add(fragment)
                }
            }
        } else if (drawableObjectiu.contains(objectiuOriginal.drawable)) {
            score += 25 // +25 per fragment destruït
            actualitzaScore()
        }

        objectius.removeAt(i)
        ganivetActiu = false
    }


    fun actualitzaMoviment() {
        if (partidaAcabada) return

        val instantActual = System.currentTimeMillis()
        if (ultimProces + PERIODE_PROCES > instantActual) return

        val retard = (instantActual - ultimProces).toDouble() / PERIODE_PROCES
        ultimProces = instantActual

        ninja.angle = (ninja.angle + girNinja * retard).toInt()

        val nIncX = ninja.incX + acceleracioNinja *
                Math.cos(Math.toRadians(ninja.angle.toDouble())) * retard
        val nIncY = ninja.incY + acceleracioNinja *
                Math.sin(Math.toRadians(ninja.angle.toDouble())) * retard

        if (Math.hypot(nIncX, nIncY) <= Grafics.MAX_VELOCITAT) {
            ninja.incX = nIncX
            ninja.incY = nIncY
        }

        ninja.incrementaPos(retard)
        objectius.forEach { it.incrementaPos(retard) }

        // Comprovar col·lisions ninja-enemics
        for (enemic in objectius) {
            if (enemic.drawable == drawableEnemic &&
                ninja.distancia(enemic) < (ninja.amplada + enemic.amplada) / 2) {

                partidaAcabada = true // <-- AÑADE ESTO

                if (!partidaDialogMostrat) {
                    partidaDialogMostrat = true
                    post {
                        AlertDialog.Builder(context)
                            .setTitle("Game Over")
                            .setMessage("Has perdut la partida!\nPuntuació: $score")
                            .setCancelable(false)
                            .setPositiveButton("Tornar al menú") { _, _ ->
                                (context as? android.app.Activity)?.finish()
                            }
                            .show()
                    }
                }
            }

        }

        // Actualitzar ganivets
        val iterator = ganivets.iterator()
        while (iterator.hasNext()) {
            val g = iterator.next()
            g.incrementaPos(retard)

            // Eliminar ganivet si surt de pantalla
            if (g.posX < -g.amplada || g.posX > width + g.amplada ||
                g.posY < -g.altura || g.posY > height + g.altura
            ) {
                iterator.remove()
                continue
            }

            // Comprovar col·lisions amb enemics
            for ((index, enemic) in objectius.withIndex()) {
                if ((enemic.drawable == drawableEnemic ||
                            drawableObjectiu.contains(enemic.drawable)) &&
                    g.distancia(enemic) < (enemic.amplada + g.amplada) / 2
                ) {
                    destrueixObjectiu(index)
                    iterator.remove()
                    break
                }
            }

        }

    }

    fun pararThread() {
        thread.parar()
    }


    fun setTextViewScore(textView: TextView) {
        this.tvScore = textView
        tvScore?.text = "Punts: $score"
    }

    private fun actualitzaScore() {
        tvScore?.post {
            tvScore?.text = "Punts: $score"
        }
    }

}

