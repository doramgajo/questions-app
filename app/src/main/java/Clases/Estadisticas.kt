package Clases

import aplicaciondepreguntas.main.MenuPrincipal
import java.io.File
import java.io.RandomAccessFile

class Estadisticas {

    companion object {

        private lateinit var archivo : RandomAccessFile

        var partidasJugadas : Int = 0
        var preguntasRespondidas : Int = 0
        var aciertos : Int = 0
        var fallos : Int = 0
        var botonesPulsados : Int = 0
        var mayorRacha : Int = 0
        var rachasTotales : Int = 0
        var rachasDe5 : Int = 0
        var rachasDe10 : Int = 0
        var partidasFaciles : Int = 0
        var partidasDificiles : Int = 0
        var aTiempo : Int = 0
        var sinTiempo : Int = 0
        var tiempoSobrante : Int = 0

        /**
         * Comprueba la existencia del archivo donde se guardan las estadísticas.
         */
        fun comprobarArchivoEstadisticas() {

            if (!File(MenuPrincipal.applicationContext().filesDir, "Estadisticas.dat").exists())
                escribirArchivoEstadisticas()
            else leerArchivoEstadisticas()

        }

        /**
         * Lee el archivo de estadisticas y las carga en la clase.
         */
        fun leerArchivoEstadisticas() {
            try {

                archivo = RandomAccessFile(File(MenuPrincipal.applicationContext().filesDir,
                    "Estadisticas.dat"), "r")
                archivo.seek(0)

                partidasJugadas = archivo.readInt()
                preguntasRespondidas = archivo.readInt()
                aciertos = archivo.readInt()
                fallos = archivo.readInt()
                botonesPulsados = archivo.readInt()
                mayorRacha = archivo.readInt()
                rachasTotales = archivo.readInt()
                rachasDe5 = archivo.readInt()
                rachasDe10 = archivo.readInt()
                partidasFaciles = archivo.readInt()
                partidasDificiles = archivo.readInt()
                aTiempo = archivo.readInt()
                sinTiempo = archivo.readInt()
                tiempoSobrante = archivo.readInt()

                archivo.close()

            } catch (e: Exception) {
                println(e.message)
            }
        }

        /**
         * Guarda la estadisticas en el archivo.
         */
        fun escribirArchivoEstadisticas() {
            try {

                archivo = RandomAccessFile(File(MenuPrincipal.applicationContext().filesDir,
                    "Estadisticas.dat"), "rw")
                archivo.seek(0)

                archivo.writeInt(partidasJugadas)
                archivo.writeInt(preguntasRespondidas)
                archivo.writeInt(aciertos)
                archivo.writeInt(fallos)
                archivo.writeInt(botonesPulsados)
                archivo.writeInt(mayorRacha)
                archivo.writeInt(rachasTotales)
                archivo.writeInt(rachasDe5)
                archivo.writeInt(rachasDe10)
                archivo.writeInt(partidasFaciles)
                archivo.writeInt(partidasDificiles)
                archivo.writeInt(aTiempo)
                archivo.writeInt(sinTiempo)
                archivo.writeInt(tiempoSobrante)

                archivo.close()

            } catch (ex: Exception) {
                println(ex.message)
            }
        }
    }
}