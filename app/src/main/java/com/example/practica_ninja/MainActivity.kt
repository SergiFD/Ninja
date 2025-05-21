package com.example.practica_ninja

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var bt_Jugar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicialitzar botons
        val puntuacio = findViewById<Button>(R.id.tb_Puntuaciones)
        bt_Jugar = findViewById(R.id.bt_Jugar)

        // Configurar la toolbar com a ActionBar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Animació de la imatge
        val imageView = findViewById<ImageView>(R.id.iw_ninja)
        val animation: Animation = AnimationUtils.loadAnimation(this, R.anim.rotate_and_move)
        imageView.startAnimation(animation)

        // Reproduir música de fons
        reproSong()

        // Configurar el botó de puntuacions
        puntuacio.setOnClickListener {
            val intent = Intent(this, PuntuacionsActivity::class.java)
            startActivity(intent)
        }

        // Configurar el botó de jugar
        bt_Jugar.setOnClickListener {
            mostrarDialogNom()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private fun reproSong() {
        // Inicialitzar la música
        mediaPlayer = MediaPlayer.create(this, R.raw.bluebird_musica_fondo)
        mediaPlayer.isLooping = true // Perquè la música es repeteixi
        mediaPlayer.start()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_informacio -> {
                mostrarInformacio()
                true
            }
            R.id.action_configuracio -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun mostrarInformacio() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Informació")
        builder.setMessage(
            "Aplicació: Ninja App\n" +
                    "Versió: 1.0\n\n" +
                    "Programador: Sergi Fernández\n" +
                    "Contacte: correoReal@gmail.com"
        )
        builder.setPositiveButton("Tancar", null)
        builder.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Alliberar recursos del MediaPlayer
        if (::mediaPlayer.isInitialized && mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }

    private fun mostrarDialogNom() {
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(
            R.layout.activity_nombre_jugador,
            null
        ) // Assegura't que el layout és correcte

        val editText = view.findViewById<EditText>(R.id.editTextNombre)
        val buttonName = view.findViewById<Button>(R.id.buttonGuardar)

        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .create() // Crear el diàleg

        buttonName.setOnClickListener {
            val nom = editText.text.toString()

            // Guardar en SharedPreferences
            val prefs = this.getSharedPreferences("prefs", Context.MODE_PRIVATE)
            prefs.edit().putString("NOM_JUGADOR", nom).apply()

            // Devolver resultado con Intent
            val resultIntent = Intent()
            resultIntent.putExtra("NOM_JUGADOR", nom)
            setResult(RESULT_OK, resultIntent)
            val intent = Intent(this, Joc::class.java)
            startActivity(intent)
            dialog.dismiss() // Tanca el diàleg
        }

        dialog.show()
    }
    }
