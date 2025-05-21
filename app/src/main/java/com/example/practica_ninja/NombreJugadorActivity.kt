package com.example.practica_ninja;

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class NombreJugadorActivity : AppCompatActivity() {
    private lateinit var editText: EditText
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nombre_jugador)

        editText = findViewById(R.id.editTextNombre)
        button = findViewById(R.id.buttonGuardar)

        button.setOnClickListener {
            val nom = editText.text.toString()
            val resultIntent = Intent()
            resultIntent.putExtra("NOM_JUGADOR", nom)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}
