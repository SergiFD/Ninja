package com.example.practica_ninja

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Joc : AppCompatActivity() {

    private var tvScore: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_joc)
        tvScore = findViewById(R.id.tvScore)
        val vistaJoc = findViewById<VistaJoc>(R.id.vistaJoc)
        vistaJoc.setTextViewScore(tvScore!!)
    }
}
