package aplicaciondepreguntas.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class MenuPrincipal : AppCompatActivity() {

    lateinit var botonTemporizador : ToggleButton
    lateinit var menuDificultad : Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menuprincipal)

        iniciarVariables()
        comprobarFicheros()
        construirListeners()
        manejarMenuTemporizador(botonTemporizador.isChecked)

    }

    /**
     * Inicializa las variables para los elementos que más usaremos.
     */
    fun iniciarVariables() {

        menuDificultad = findViewById(R.id.M_switchDificultad)
        botonTemporizador = findViewById(R.id.M_botonTemporizador)

    }

    init {
        instancia = this
    }

    /**
     * Utilizaremos el companion object para acceder al estado del menú del temporizador desde
     * otra actividad.
     */
    companion object {
        private var instancia: MenuPrincipal? = null

        fun applicationContext() : Context {
            return instancia!!.applicationContext
        }
    }

    /**
     * Comprueba que existan los archivos necesarios para el funcionamiento de la app.
     */
    fun comprobarFicheros() {

        Clases.Estadisticas.comprobarArchivoEstadisticas()
        Clases.Pregunta.comprobarArchivoPreguntas()

    }

    /**
     * Construye las acciones que realizarán los diferentes botones.
     */
    fun construirListeners() {

        // Recoge el estado del menú del temporizador e inicia la actividad de juego, enviando
        // dicho estado.
        findViewById<Button>(R.id.M_botonJugar).setOnClickListener {

            val intentJuego = Intent(this, Juego::class.java)

            intentJuego.putExtra("tempo", botonTemporizador.isChecked)

            if (botonTemporizador.isChecked) {
                intentJuego.putExtra("nivel", menuDificultad.isChecked)
            }
            startActivity(intentJuego)
        }

        findViewById<Button>(R.id.M_botonEstadisticas).setOnClickListener {
            startActivity(Intent(this, Estadisticas::class.java))
        }

        findViewById<Button>(R.id.M_botonAnnadirPregunta).setOnClickListener {
            startActivity(Intent(this, AnnadirPreguntas::class.java))
        }

        botonTemporizador.setOnCheckedChangeListener { _, isChecked ->
            manejarMenuTemporizador(isChecked)
        }
    }

    /**
     * Define el comportamiento del menú del temporizador cuando el jugador interactúa con él.
     */
    fun manejarMenuTemporizador(isChecked : Boolean) {

        if (isChecked) {
            botonTemporizador.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icono_quitar_temporizador, 0, 0, 0);
            menuDificultad.visibility = View.VISIBLE
            findViewById<TextView>(R.id.M_textoFacil).visibility = View.VISIBLE
            findViewById<TextView>(R.id.M_textoDificil).visibility = View.VISIBLE
        } else {
            botonTemporizador.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icono_temporizador, 0, 0, 0);
            menuDificultad.visibility = View.INVISIBLE
            findViewById<TextView>(R.id.M_textoFacil).visibility = View.INVISIBLE
            findViewById<TextView>(R.id.M_textoDificil).visibility = View.INVISIBLE
        }

    }

    /**
     * Guarda ciertos datos para no perderlos al girar la pantalla.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("temporizador", botonTemporizador.isChecked)
        outState.putBoolean("dificultad", menuDificultad.isChecked)
    }

    /**
     * Recupera los datos que se pierden al girar la pantalla y los vuelve a mostrar.
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        manejarMenuTemporizador(savedInstanceState.getBoolean("temporizador"))
        menuDificultad.isChecked = savedInstanceState.getBoolean("dificultad")

    }

}