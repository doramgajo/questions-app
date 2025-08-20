package aplicaciondepreguntas.main

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AnnadirPreguntas : AppCompatActivity() {

    lateinit var botonRespuestaVerdadero : Button
    lateinit var botonRespuestaFalso : Button
    lateinit var campoPregunta : EditText
    lateinit var campoExplicacion : EditText
    var respuesta : Boolean = true
    var seHaElegidoRespuesta : Boolean = false
    
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_annadirpreguntas)

        iniciarVariables()
        construirListeners()

    }

    /**
     * Inicializa las variables para los elementos que más usaremos.
     */
    fun iniciarVariables() {

        botonRespuestaVerdadero = findViewById(R.id.AP_botonRespuestaVerdadero)
        botonRespuestaFalso = findViewById(R.id.AP_botonRespuestaFalso)
        campoPregunta = findViewById(R.id.AP_campoPregunta)
        campoExplicacion = findViewById(R.id.AP_campoExplicacion)

    }

    /**
     * Construye las acciones que realizarán los diferentes botones.
     */
    fun construirListeners() {

        botonRespuestaVerdadero.setOnClickListener {
            elegirRespuesta((true))
        }

        botonRespuestaFalso.setOnClickListener {
            elegirRespuesta(false)
        }

        findViewById<Button>(R.id.AP_botonVolverMenu).setOnClickListener {
            finish()
        }

        /**
         * Añadirá la pregunta si los datos son correctos o mostrará un mensaje si faltan datos.
         */
        findViewById<Button>(R.id.AP_botonAnnadirPregunta).setOnClickListener {

            if (campoPregunta.length() == 0 || campoExplicacion.length() == 0 || !seHaElegidoRespuesta)

                Toast.makeText(this,
                    getString(R.string.toastPreguntaNoAnnadida), Toast.LENGTH_LONG).show()

            else {

                var nuevaPregunta = Clases.Pregunta("¿" + campoPregunta.text.toString()
                        + "?", respuesta, campoExplicacion.text.toString())

                Clases.Pregunta.listaPreguntas.add(nuevaPregunta)
                Clases.Pregunta.guardarNuevaPregunta(nuevaPregunta)

                Toast.makeText(this,
                    getString(R.string.toastPreguntaAnnadida), Toast.LENGTH_LONG).show()

                limpiarCampos()

            }
        }
    }

    /**
     * Elimina el texto de los campos y la respuesta que haya sido elegida.
     */
    fun limpiarCampos() {

        campoPregunta.text.clear()
        campoExplicacion.text.clear()
        botonRespuestaFalso.setBackgroundColor(getColor(R.color.gris))
        botonRespuestaVerdadero.setBackgroundColor(getColor(R.color.gris))
        seHaElegidoRespuesta = false

    }

    /**
     * Guarda ciertos datos para no perderlos al girar la pantalla.
     */
    override fun onSaveInstanceState(outState: Bundle) {

        super.onSaveInstanceState(outState)
        outState.putString("pregunta", findViewById<EditText>(R.id.AP_campoPregunta).text.toString())
        outState.putString("explicacion", findViewById<EditText>(R.id.AP_campoExplicacion).text.toString())
        outState.putBoolean("respuesta", respuesta)
        outState.putBoolean("seHaElegidoRespuesta", seHaElegidoRespuesta)

    }

    /**
     * Establece la respuesta elegida y cambia el color de los botones según la elección.
     */
    fun elegirRespuesta(respuestaElegida : Boolean) {

        if (respuestaElegida) {

            respuesta = true
            botonRespuestaVerdadero.setBackgroundColor(getColor(R.color.verde))
            botonRespuestaFalso.setBackgroundColor(getColor(R.color.gris))

        } else {

            respuesta = false
            botonRespuestaFalso.setBackgroundColor(getColor(R.color.rojo))
            botonRespuestaVerdadero.setBackgroundColor(getColor(R.color.gris))

        }

        seHaElegidoRespuesta = true

    }

    /**
     * Recupera los datos que se pierden al girar la pantalla y los vuelve a mostrar.
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {

        super.onRestoreInstanceState(savedInstanceState)

        campoPregunta.setText(savedInstanceState.getString("pregunta"))
        campoExplicacion.setText(savedInstanceState.getString("explicacion"))

        if (savedInstanceState.getBoolean("seHaElegidoRespuesta"))
            elegirRespuesta(savedInstanceState.getBoolean("respuesta"))

    }

}