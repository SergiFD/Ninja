package com.example.practica_ninja

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class PuntuacionsActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private val puntuacions = arrayOf("Jugador 1: 50", "Jugador 2: 75", "Jugador 3: 100")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puntuacio)

        listView = findViewById(R.id.listview_puntuacions)

        // Guardar dades en SharedPreferences
        guardarPuntuacions()

        // Mostrar les puntuacions
        mostrarPuntuacions()
    }

    private fun guardarPuntuacions() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("PuntuacionsPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        for (i in puntuacions.indices) {
            editor.putString("jugador${i + 1}", puntuacions[i])
        }
        editor.apply()
    }

    private fun mostrarPuntuacions() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("PuntuacionsPrefs", MODE_PRIVATE)
        val puntuacionsGuardades = Array(puntuacions.size) { "" }

        for (i in puntuacions.indices) {
            puntuacionsGuardades[i] = sharedPreferences.getString("jugador${i + 1}", "No disponible") ?: "No disponible"
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, puntuacionsGuardades)
        listView.adapter = adapter
    }
}

