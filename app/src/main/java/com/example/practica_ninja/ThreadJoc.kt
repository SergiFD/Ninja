import com.example.practica_ninja.Joc
import com.example.practica_ninja.VistaJoc

class ThreadJoc(private val gameView: VistaJoc) : Thread() {
    var running = true

    override fun run() {
        while (running) {
            gameView.actualitzaMoviment()
        }
    }

    fun parar() {
        running = false
        interrupt()
    }
}