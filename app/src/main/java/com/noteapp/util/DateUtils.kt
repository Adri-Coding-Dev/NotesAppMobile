package com.noteapp.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    private val fullFormat = SimpleDateFormat("d MMM yyyy, HH:mm", Locale.getDefault())
    private val shortFormat = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun formatFull(timestamp: Long): String = fullFormat.format(Date(timestamp))

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
