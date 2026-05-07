/*
 * Utilidad para formatear fechas en formatos legibles. Convierte timestamps en cadenas como
 * "5 abr 2025, 15:30", "Hoy 15:30", "Ayer" o fechas cortas.
 */
package com.noteapp.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    private val fullFormat = SimpleDateFormat("d MMM yyyy, HH:mm", Locale.getDefault())
    private val shortFormat = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    // Formato completo: "5 abr 2025, 15:30"
    fun formatFull(timestamp: Long): String = fullFormat.format(Date(timestamp))

    // Formato corto relativo: "Hoy 15:30", "Ayer" o "5 abr 2025"
    fun formatShort(timestamp: Long): String {
        val now = Calendar.getInstance()
        val date = Calendar.getInstance().apply { timeInMillis = timestamp }
        return when {
            now.get(Calendar.DATE) == date.get(Calendar.DATE) &&
                    now.get(Calendar.YEAR) == date.get(Calendar.YEAR) ->
                "Today ${timeFormat.format(Date(timestamp))}"
            now.get(Calendar.DATE) - date.get(Calendar.DATE) == 1 &&
                    now.get(Calendar.YEAR) == date.get(Calendar.YEAR) ->
                "Yesterday"
            else -> shortFormat.format(Date(timestamp))
        }
    }
}