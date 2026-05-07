/*
 * Utilidades para trabajar con texto enriquecido. Convierte entre un formato HTML simple
 * (<u>subrayado</u>) y AnnotatedString de Compose. También permite aplicar o quitar
 * subrayado en una selección y obtener una vista previa sin etiquetas.
 */
package com.noteapp.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration

object RichTextUtils {

    private const val UNDERLINE_OPEN = "<u>"
    private const val UNDERLINE_CLOSE = "</u>"
    const val UNDERLINE_ANNOTATION = "underline"

    // Convierte HTML con etiquetas <u> a AnnotatedString con estilo de subrayado.
    fun htmlToAnnotatedString(html: String): AnnotatedString {
        return buildAnnotatedString {
            var remaining = html
            while (remaining.isNotEmpty()) {
                val openIdx = remaining.indexOf(UNDERLINE_OPEN)
                if (openIdx == -1) {
                    append(remaining)
                    break
                }
                if (openIdx > 0) {
                    append(remaining.substring(0, openIdx))
                }
                val closeIdx = remaining.indexOf(UNDERLINE_CLOSE, openIdx + UNDERLINE_OPEN.length)
                if (closeIdx == -1) {
                    append(remaining.substring(openIdx + UNDERLINE_OPEN.length))
                    break
                }
                val underlineText = remaining.substring(openIdx + UNDERLINE_OPEN.length, closeIdx)
                val startPos = length
                append(underlineText)
                addStyle(
                    style = SpanStyle(textDecoration = TextDecoration.Underline),
                    start = startPos,
                    end = length
                )
                addStringAnnotation(
                    tag = UNDERLINE_ANNOTATION,
                    annotation = UNDERLINE_ANNOTATION,
                    start = startPos,
                    end = length
                )
                remaining = remaining.substring(closeIdx + UNDERLINE_CLOSE.length)
            }
        }
    }

    // Convierte de nuevo a HTML, envolviendo los rangos subrayados con <u>.
    fun annotatedStringToHtml(annotatedString: AnnotatedString): String {
        val text = annotatedString.text
        if (annotatedString.spanStyles.isEmpty()) return text

        val underlineRanges = annotatedString.spanStyles
            .filter { it.item.textDecoration == TextDecoration.Underline }
            .map { it.start to it.end }
            .sortedBy { it.first }

        if (underlineRanges.isEmpty()) return text

        val sb = StringBuilder()
        var pos = 0
        for ((start, end) in underlineRanges) {
            if (pos < start) {
                sb.append(text.substring(pos, start))
            }
            sb.append(UNDERLINE_OPEN)
            sb.append(text.substring(start, end))
            sb.append(UNDERLINE_CLOSE)
            pos = end
        }
        if (pos < text.length) {
            sb.append(text.substring(pos))
        }
        return sb.toString()
    }

    // Aplica subrayado a un rango de selección (si no estaba ya subrayado).
    fun applyUnderline(
        annotatedString: AnnotatedString,
        selectionStart: Int,
        selectionEnd: Int
    ): AnnotatedString {
        if (selectionStart >= selectionEnd) return annotatedString
        val start = selectionStart.coerceIn(0, annotatedString.length)
        val end = selectionEnd.coerceIn(0, annotatedString.length)

        return buildAnnotatedString {
            append(annotatedString)
            val existingUnderline = annotatedString.spanStyles.any {
                it.item.textDecoration == TextDecoration.Underline &&
                        it.start <= start && it.end >= end
            }
            if (!existingUnderline) {
                addStyle(
                    style = SpanStyle(textDecoration = TextDecoration.Underline),
                    start = start,
                    end = end
                )
            }
        }
    }

    // Quita el subrayado del rango seleccionado, conservando otros estilos.
    fun removeUnderline(
        annotatedString: AnnotatedString,
        selectionStart: Int,
        selectionEnd: Int
    ): AnnotatedString {
        val start = selectionStart.coerceIn(0, annotatedString.length)
        val end = selectionEnd.coerceIn(0, annotatedString.length)

        return buildAnnotatedString {
            append(annotatedString.text)
            for (span in annotatedString.spanStyles) {
                if (span.item.textDecoration == TextDecoration.Underline) {
                    if (span.end <= start || span.start >= end) {
                        addStyle(span.item, span.start, span.end)
                    } else {
                        if (span.start < start) addStyle(span.item, span.start, start)
                        if (span.end > end) addStyle(span.item, end, span.end)
                    }
                } else {
                    addStyle(span.item, span.start, span.end)
                }
            }
        }
    }

    // Indica si el rango está completamente subrayado.
    fun isRangeUnderlined(
        annotatedString: AnnotatedString,
        start: Int,
        end: Int
    ): Boolean {
        if (start >= end) return false
        return annotatedString.spanStyles.any {
            it.item.textDecoration == TextDecoration.Underline &&
                    it.start <= start && it.end >= end
        }
    }

    // Elimina las etiquetas HTML para obtener texto plano (vista previa).
    fun stripHtml(html: String): String =
        html.replace(UNDERLINE_OPEN, "").replace(UNDERLINE_CLOSE, "")
}