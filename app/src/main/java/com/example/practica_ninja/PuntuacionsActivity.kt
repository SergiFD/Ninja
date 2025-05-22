package com.example.practica_ninja

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class PuntuacionsActivity : AppCompatActivity() {
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puntuacio)

        listView = findViewById(R.id.listview_puntuacions)

        mostrarPuntuacions()
    }

    private fun mostrarPuntuacions() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("PuntuacionsPrefs", MODE_PRIVATE)
        val puntuacionsGuardades = mutableListOf<String>()

        var i = 1
        while (true) {
            val puntuacio = sharedPreferences.getString("jugador$i", null)
            if (puntuacio == null) break
            puntuacionsGuardades.add(puntuacio)
            i++
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, puntuacionsGuardades)
        listView.adapter = adapter
    }
}
