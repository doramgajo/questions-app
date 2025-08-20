package aplicaciondepreguntas.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView

class Estadisticas : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estadisticas)

        construirListeners()
        mostrarEstadisticas()
        mostrarPorcentajes()
        mostrarTiempoRestante()

    }

    /**
     * Construye las acciones que realizarán los diferentes botones.
     */
    fun construirListeners() {
        findViewById<Button>(R.id.E_botonVolverMenu).setOnClickListener {
            finish()
        }
    }

    /**
     * Muestra los valores de las estadísticas en los correspondientes TextView.
     */
    fun mostrarEstadisticas() {

        findViewById<TextView>(R.id.E_datoPartidasJugadas).text = Clases.Estadisticas.partidasJugadas.toString()
        findViewById<TextView>(R.id.E_datoPreguntasRespondidas).text = Clases.Estadisticas.preguntasRespondidas.toString()
        findViewById<TextView>(R.id.E_datoPreguntasAcertadas).text = Clases.Estadisticas.aciertos.toString()
        findViewById<TextView>(R.id.E_datoPreguntasFalladas).text = Clases.Estadisticas.fallos.toString()
        findViewById<TextView>(R.id.E_datoBotonesPulsados).text = Clases.Estadisticas.botonesPulsados.toString()
        findViewById<TextView>(R.id.E_datoRachaMasAlta).text = Clases.Estadisticas.mayorRacha.toString()
        findViewById<TextView>(R.id.E_datoRachasTotales).text = Clases.Estadisticas.rachasTotales.toString()
        findViewById<TextView>(R.id.E_datoRachasDe5).text = Clases.Estadisticas.rachasDe5.toString()
        findViewById<TextView>(R.id.E_datoRachasDe10).text = Clases.Estadisticas.rachasDe10.toString()
        findViewById<TextView>(R.id.E_datoPartidasFaciles).text = Clases.Estadisticas.partidasFaciles.toString()
        findViewById<TextView>(R.id.E_datoPartidasDificiles).text = Clases.Estadisticas.partidasDificiles.toString()
        findViewById<TextView>(R.id.E_datoRespuestasATiempo).text = Clases.Estadisticas.aTiempo.toString()
        findViewById<TextView>(R.id.E_datoTiempoAgotado).text = Clases.Estadisticas.sinTiempo.toString()

    }

    /**
     * Si existen estadísticas de preguntas, muestra los valores de las estadísticas con porcentaje
     * en los correspondientes TextView.
     */
    fun mostrarPorcentajes() {

        if (Clases.Estadisticas.preguntasRespondidas > 0) {

            // Calcula (en Float) el porcentaje de aciertos y fallos:
            val porcentajeAciertos : Float = ((Clases.Estadisticas.aciertos.toFloat()
                    / Clases.Estadisticas.preguntasRespondidas.toFloat()) * 100F)
            val porcentajeFallos : Float = ((Clases.Estadisticas.fallos.toFloat()
                    / Clases.Estadisticas.preguntasRespondidas.toFloat()) * 100F)

            // Muestra el porcentaje en las barras de progreso:
            findViewById<ProgressBar>(R.id.E_barraPorcentajeAciertos).progress = porcentajeAciertos.toInt()
            findViewById<ProgressBar>(R.id.E_barraPorcentajeFallos).progress = porcentajeFallos.toInt()

            // Muestra el porcentaje (con un único decimal) en los TextView correspondientes:
            findViewById<TextView>(R.id.E_datoPorcentajeAciertos).text =
                porcentajeAciertos.toString().replace(".", ",")
                    .substring(0, porcentajeAciertos.toString().indexOf(".") + 2) + "%"

            findViewById<TextView>(R.id.E_datoPorcentajeFallos).text =
                porcentajeFallos.toString().replace(".", ",")
                    .substring(0, porcentajeFallos.toString().indexOf(".") + 2) + "%"

        } else {

            // Al no existir estadísticas, muestra los porcentajes como 0%:
            findViewById<TextView>(R.id.E_datoPorcentajeAciertos).text = "0%"
            findViewById<TextView>(R.id.E_datoPorcentajeFallos).text = "0%"

        }
    }

    /**
     * Si los segundos sobrepasan un minuto, los divide entre 60 para mostrar el dato en minutos.
     */
    fun mostrarTiempoRestante() {

        if (Clases.Estadisticas.tiempoSobrante > 60)
            findViewById<TextView>(R.id.E_datoTiempoRestante).text = (Clases.Estadisticas.tiempoSobrante / 60).toString() + " m " + (Clases.Estadisticas.tiempoSobrante % 60).toString() + " s"
        else
            findViewById<TextView>(R.id.E_datoTiempoRestante).text = Clases.Estadisticas.tiempoSobrante.toString() + " s"

    }

}