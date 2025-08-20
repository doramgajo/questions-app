package aplicaciondepreguntas.main

import android.content.res.ColorStateList
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class Juego : AppCompatActivity() {

    private lateinit var temporizador : CountDownTimer
    lateinit var textoPregunta : TextView
    lateinit var textoResultado : TextView
    lateinit var botonVerdadero : Button
    lateinit var botonFalso : Button
    lateinit var botonSiguiente : Button
    lateinit var botonAnterior : Button
    lateinit var botonVolver : Button
    lateinit var botonAleatorio : Button
    lateinit var textoRacha : TextView
    lateinit var preguntaActual : Clases.Pregunta
    lateinit var barraTemporizador : ProgressBar
    var jugarConTemporizador : Boolean = false
    var nivelTemporizador : Boolean = false
    var preguntaYaRespondida : Boolean = false
    var respuestaDelJugador : Boolean = false
    var racha : Int = 0
    var posicion : Int = 0
    var temporizadorActivo : Boolean = false //El tiempo no ha llegado a 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_juego)

        declararVariables()
        recibirIntent()
        aumentarNumeroDePartidas()
        construirListeners()
        configurarTemporizador()
        mostrarPregunta()

    }

    /**
     * Guarda las estadísticas al salir del juego.
     */
    override fun onDestroy() {
        super.onDestroy()
        Clases.Estadisticas.escribirArchivoEstadisticas()
    }

    /**
     * Inicializa las variables para los elementos que más usaremos.
     */
    fun declararVariables() {
        textoPregunta = findViewById(R.id.textoPregunta)
        textoResultado = findViewById(R.id.textoResultado)
        botonVerdadero = findViewById(R.id.botonVerdadero)
        botonFalso = findViewById(R.id.botonFalso)
        botonSiguiente = findViewById(R.id.botonSiguiente)
        botonAnterior = findViewById(R.id.botonAnterior)
        botonVolver = findViewById(R.id.botonVolver)
        botonAleatorio = findViewById(R.id.botonAleatorio)
        textoRacha = findViewById(R.id.juego_texto_racha)
        barraTemporizador = findViewById(R.id.progressBar)
    }

    /**
     * Recibe los datos de selección del modo del temporizador del menú principal.
     */
    fun recibirIntent() {
        jugarConTemporizador = intent.getBooleanExtra("tempo", false)
        if (jugarConTemporizador) {
            nivelTemporizador = intent.getBooleanExtra("nivel", false)
        }
    }

    /**
     * Aumenta estadísticas.
     */
    fun aumentarNumeroDePartidas() {
        Clases.Estadisticas.partidasJugadas++
        if (jugarConTemporizador) {
            if (nivelTemporizador) {
                Clases.Estadisticas.partidasDificiles++
            }
            else {
                Clases.Estadisticas.partidasFaciles++
            }
        }
    }


    /**
     * Configuración del temporizador
     */
    fun configurarTemporizador() {

        if (!jugarConTemporizador) {
            barraTemporizador.visibility = View.INVISIBLE
            findViewById<ImageView>(R.id.imagenTemporizador).visibility = View.INVISIBLE
        } else {
            // aqui todoo el manejo y creacion del temporizador
        }
    }


    /**
     * Construye las acciones que realizarán los diferentes botones.
     */
    fun construirListeners() {

        botonSiguiente.setOnClickListener {
            // Si nos encontramos en la última pregunta, volvemos a la primera.
            if (posicion == Clases.Pregunta.listaPreguntas.size - 1) posicion = 0
            else posicion++
            mostrarPregunta()
            preguntaYaRespondida = false
            Clases.Estadisticas.botonesPulsados++
        }

        botonAnterior.setOnClickListener {
            // Si nos encontramos en la primera pregunta, volvemos a la última.
            if (posicion == 0) posicion = Clases.Pregunta.listaPreguntas.size - 1
            else posicion--
            mostrarPregunta()
            preguntaYaRespondida = false
            Clases.Estadisticas.botonesPulsados++
        }

        botonVolver.setOnClickListener {
            finish()
        }

        botonVerdadero.setOnClickListener {
            respuestaDelJugador = true
            preguntaYaRespondida = true
            procesarRespuesta(respuestaDelJugador)
            Clases.Estadisticas.botonesPulsados++
            Clases.Estadisticas.preguntasRespondidas++
        }

        botonFalso.setOnClickListener {
            respuestaDelJugador = false
            preguntaYaRespondida = true
            procesarRespuesta(respuestaDelJugador)
            Clases.Estadisticas.botonesPulsados++
            Clases.Estadisticas.preguntasRespondidas++
        }

        botonAleatorio.setOnClickListener {
            Clases.Estadisticas.botonesPulsados++
            var random = Random.nextInt(Clases.Pregunta.listaPreguntas.size)

            //Evita que nos aparezca la misma pregunta que ya estabamos viendo.
            while (posicion == random) {
                random = Random.nextInt(Clases.Pregunta.listaPreguntas.size)

            }
            preguntaYaRespondida = false
            posicion = random
            mostrarPregunta()

        }

    }

    fun mostrarPregunta() {

        // Borra el icono de true y false.
        botonFalso.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        botonVerdadero.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        botonVerdadero.setBackgroundColor(getColor(R.color.boton_destacado))
        botonFalso.setBackgroundColor(getColor(R.color.boton_destacado))
        preguntaActual = Clases.Pregunta.listaPreguntas.get(posicion)
        textoResultado.text = ""
        textoPregunta.text = preguntaActual.pregunta
        botonVerdadero.setEnabled(true)
        botonFalso.setEnabled(true)
        botonSiguiente.setEnabled(false)
        botonAnterior.setEnabled(false)
        botonAleatorio.setEnabled(false)
        botonAleatorio.setBackgroundColor(getColor(R.color.gris))
        botonAnterior.setBackgroundColor(getColor(R.color.gris))
        botonSiguiente.setBackgroundColor(getColor(R.color.gris))
        textoRacha.text = ""
        findViewById<TextView>(R.id.juego_texto_explicacion).text = ""

        if (jugarConTemporizador) establecerTemporizador()

    }

    fun procesarRespuesta(respuesta : Boolean) {

        if (jugarConTemporizador && temporizadorActivo) temporizador.cancel()
        mostrarRespuesta()
        if (respuesta == preguntaActual.respuesta) {

            if (jugarConTemporizador) {
                Clases.Estadisticas.tiempoSobrante += barraTemporizador.progress / 100
                Clases.Estadisticas.aTiempo++

            }
            racha++
            if (racha == 2) Clases.Estadisticas.rachasTotales++
            else if (racha == 5) Clases.Estadisticas.rachasDe5++
            else if (racha == 10) Clases.Estadisticas.rachasDe10++
            if (racha > Clases.Estadisticas.mayorRacha) Clases.Estadisticas.mayorRacha = racha

            textoResultado.text = "¡Respuesta correcta!"
            textoResultado.setTextColor(getColor(R.color.verde))
            Clases.Estadisticas.aciertos++
            if (racha >= 2) textoRacha.text = "¡Llevas una racha de $racha!"
        } else {
            val rachaTemporal = racha
            racha = 0
            textoResultado.text = "¡Respuesta incorrecta!"
            textoResultado.setTextColor(getColor(R.color.rojo))
            Clases.Estadisticas.fallos++
            if (rachaTemporal >= 2) textoRacha.text = "¡Perdiste tu racha de $rachaTemporal!"
        }
        botonAleatorio.setBackgroundColor(getColor(R.color.boton_destacado))
        botonAnterior.setBackgroundColor(getColor(R.color.boton_destacado))
        botonSiguiente.setBackgroundColor(getColor(R.color.boton_destacado))
        botonVerdadero.setEnabled(false)
        botonFalso.setEnabled(false)
        botonSiguiente.setEnabled(true)
        botonAnterior.setEnabled(true)
        botonAleatorio.setEnabled(true)

        findViewById<TextView>(R.id.juego_texto_explicacion).text = preguntaActual.explicacion
    }


    /**
     * Define cómo se comportará el temporizador de juego.
     */
    fun establecerTemporizador() {

        temporizadorActivo = true

        var tiempoTemporizador : Long

        if (nivelTemporizador) {
            tiempoTemporizador = 3000
            barraTemporizador.max = 300
        }
        else {
            tiempoTemporizador = 10000
            barraTemporizador.max = 1000
        }

        temporizador = object : CountDownTimer(tiempoTemporizador, 10) {

            override fun onTick(millisUntilFinished: Long) {
                if (barraTemporizador.progress > barraTemporizador.max * 0.75) {
                    barraTemporizador.progressTintList = ColorStateList.valueOf(getColor(R.color.verde))
                } else if (barraTemporizador.progress > barraTemporizador.max * 0.5) {
                    barraTemporizador.progressTintList = ColorStateList.valueOf(getColor(R.color.amarillo))
                } else if (barraTemporizador.progress > barraTemporizador.max * 0.25) {
                    barraTemporizador.progressTintList = ColorStateList.valueOf(getColor(R.color.naranja))
                } else if (barraTemporizador.progress > 0) {
                    barraTemporizador.progressTintList = ColorStateList.valueOf(getColor(R.color.rojo))
                }
                barraTemporizador.progress = ((millisUntilFinished / 10).toInt())
            }

            override fun onFinish() {

                temporizadorActivo = false
                Clases.Estadisticas.preguntasRespondidas
                Clases.Estadisticas.sinTiempo++

                if (preguntaActual.respuesta) {
                    botonVerdadero.setBackgroundColor(getColor(R.color.verde))
                    botonFalso.setBackgroundColor(getColor(R.color.rojo))
                    botonVerdadero.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icono_verdadero, 0, 0, 0);
                    botonFalso.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icono_falso, 0, 0, 0);
                } else {
                    botonFalso.setBackgroundColor(getColor(R.color.verde))
                    botonVerdadero.setBackgroundColor(getColor(R.color.rojo))
                    botonFalso.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icono_verdadero, 0, 0, 0);
                    botonVerdadero.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icono_falso, 0, 0, 0);
                }

                textoResultado.text = "¡Tiempo agotado!"
                textoResultado.setTextColor(getColor(R.color.rojo))
                if (racha >= 2) {
                    textoRacha.text = "¡Perdiste tu racha de $racha!"
                }
                racha = 0

                botonAleatorio.setBackgroundColor(getColor(R.color.boton_destacado))
                botonAnterior.setBackgroundColor(getColor(R.color.boton_destacado))
                botonSiguiente.setBackgroundColor(getColor(R.color.boton_destacado))
                botonVerdadero.setEnabled(false)
                botonFalso.setEnabled(false)
                botonSiguiente.setEnabled(true)
                botonAnterior.setEnabled(true)
                botonAleatorio.setEnabled(true)

                findViewById<TextView>(R.id.juego_texto_explicacion).text = preguntaActual.explicacion

            }
        }.start()

    }

    /**
     * Método que guardará información del juego para no perderla cuando giremos la pantalla.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("posicion", posicion)
        outState.putInt("racha", racha)
        outState.putBoolean("respuestaDelJugador", respuestaDelJugador)
        outState.putBoolean("jugarConTemporizador", jugarConTemporizador)
        outState.putBoolean("nivelTemporizador", nivelTemporizador)
        outState.putBoolean("temporizadorActivo", temporizadorActivo)
        outState.putBoolean("preguntaYaRespondida", preguntaYaRespondida)

        outState.putInt("partidasJugadas", Clases.Estadisticas.partidasJugadas)
        outState.putInt("partidasFaciles", Clases.Estadisticas.partidasFaciles)
        outState.putInt("partidasDificiles", Clases.Estadisticas.partidasDificiles)
        println("progreso : " + barraTemporizador.progress)
        outState.putInt("progreso", barraTemporizador.progress)
    }

    /**
     * Método que recuperará la información del juego perdida al haber girado la pantalla.
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        println("progreso : " + barraTemporizador.progress)
        barraTemporizador.progress = savedInstanceState.getInt("progreso")

        Clases.Estadisticas.partidasJugadas = savedInstanceState.getInt("partidasJugadas")
        Clases.Estadisticas.partidasFaciles = savedInstanceState.getInt("partidasFaciles")
        Clases.Estadisticas.partidasDificiles = savedInstanceState.getInt("partidasDificiles")

        posicion = savedInstanceState.getInt("posicion")
        mostrarPregunta()
        racha = savedInstanceState.getInt("racha")
        respuestaDelJugador = savedInstanceState.getBoolean("respuestaDelJugador")
        jugarConTemporizador = savedInstanceState.getBoolean("jugarConTemporizador")
        nivelTemporizador = savedInstanceState.getBoolean("nivelTemporizador")
        temporizadorActivo = savedInstanceState.getBoolean("temporizadorActivo")

        preguntaYaRespondida = savedInstanceState.getBoolean("preguntaYaRespondida")

        if (preguntaYaRespondida) {
            mostrarRespuesta()
            if (jugarConTemporizador) {

                temporizador.cancel()
                temporizadorActivo = savedInstanceState.getBoolean("temporizadorActivo")
            }
        }
    }

    /**
     * Método que establece el color de fondo y el icono de los botones Verdadero y Falso,
     * permitiendo conocer la respuesta correcta a través de estos botones.
     */
    fun mostrarRespuesta() {

        if (preguntaActual.respuesta) {

            botonVerdadero.setBackgroundColor(getColor(R.color.verde))
            botonFalso.setBackgroundColor(getColor(R.color.rojo))

            botonVerdadero.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icono_verdadero, 0, 0, 0)
            botonFalso.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icono_falso, 0, 0, 0)

        } else {

            botonFalso.setBackgroundColor(getColor(R.color.verde))
            botonVerdadero.setBackgroundColor(getColor(R.color.rojo))

            botonFalso.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icono_verdadero, 0, 0, 0)
            botonVerdadero.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icono_falso, 0, 0, 0)

        }


        if (preguntaYaRespondida && respuestaDelJugador == preguntaActual.respuesta) {

            textoResultado.text = "¡Respuesta correcta!"
            textoResultado.setTextColor(getColor(R.color.verde))
            if (racha >= 2) textoRacha.text = "¡Llevas una racha de $racha!"
        } else if (preguntaYaRespondida && respuestaDelJugador != preguntaActual.respuesta) {
            textoResultado.text = "¡Respuesta incorrecta!"
            textoResultado.setTextColor(getColor(R.color.rojo))
        }
        botonAleatorio.setBackgroundColor(getColor(R.color.boton_destacado))
        botonAnterior.setBackgroundColor(getColor(R.color.boton_destacado))
        botonSiguiente.setBackgroundColor(getColor(R.color.boton_destacado))
        botonVerdadero.setEnabled(false)
        botonFalso.setEnabled(false)
        botonSiguiente.setEnabled(true)
        botonAnterior.setEnabled(true)
        botonAleatorio.setEnabled(true)

        findViewById<TextView>(R.id.juego_texto_explicacion).text = preguntaActual.explicacion

    }

}