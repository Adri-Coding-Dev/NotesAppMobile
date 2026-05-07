package com.noteapp.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration

/**
 * Utility for converting between HTML-like rich text storage format and
 * Compose AnnotatedString. We use a simple custom format:
 * <u>underlined text</u> for underlines.
 */
object RichTextUtils {

    private const val UNDERLINE_OPEN = "<u>"
    private const val UNDERLINE_CLOSE = "</u>"
    const val UNDERLINE_ANNOTATION = "underline"

    /**
     * Converts our HTML-like storage string to Compose AnnotatedString.
     */
    fun htmlToAnnotatedString(html: String): AnnotatedString {
        return buildAnnotatedString {
            var remaining = html
            while (remaining.isNotEmpty()) {
                val openIdx = remaining.indexOf(UNDERLINE_OPEN)
                if (openIdx == -1) {
                    append(remaining)
                    break
                }
                // Append text before tag
                if (openIdx > 0) {
                    append(remaining.substring(0, openIdx))
                }
                // Find closing tag
                val closeIdx = remaining.indexOf(UNDERLINE_CLOSE, openIdx + UNDERLINE_OPEN.length)
                if (closeIdx == -1) {
                    // No closing tag, append rest as plain text
                    append(remaining.substring(openIdx + UNDERLINE_OPEN.length))
                    break
                }
                // Apply underline span
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

    /**
     * Converts AnnotatedString back to HTML-like storage format.
     * Scans for underline spans and wraps them with <u> tags.
     */
    fun annotatedStringToHtml(annotatedString: AnnotatedString): String {
        val text = annotatedString.text
        if (annotatedString.spanStyles.isEmpty()) return text

        // Collect underline ranges
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

    /**
     * Applies underline formatting to a selection within an AnnotatedString.
     */
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
            // Check if already underlined (toggle off)
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

    /**
     * Removes underline from a selection (toggle off).
     */
    fun removeUnderline(
        annotatedString: AnnotatedString,
        selectionStart: Int,
        selectionEnd: Int
    ): AnnotatedString {
        val start = selectionStart.coerceIn(0, annotatedString.length)
        val end = selectionEnd.coerceIn(0, annotatedString.length)

        return buildAnnotatedString {
            append(annotatedString.text)
            // Re-add all span styles except underlines in the selected range
            for (span in annotatedString.spanStyles) {
                if (span.item.textDecoration == TextDecoration.Underline) {
                    // Split or skip underline spans that overlap with selection
                    if (span.end <= start || span.start >= end) {
                        // No overlap: keep
                        addStyle(span.item, span.start, span.end)
                    } else {
                        // Partial overlap: keep parts outside selection
                        if (span.start < start) addStyle(span.item, span.start, start)
                        if (span.end > end) addStyle(span.item, end, span.end)
                    }
                } else {
                    addStyle(span.item, span.start, span.end)
                }
            }
        }
    }

    /**
     * Returns true if the given range is fully underlined.
     */
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

    /**
     * Plain text preview without HTML tags.
     */
    fun stripHtml(html: String): String =
        html.replace(UNDERLINE_OPEN, "").replace(UNDERLINE_CLOSE, "")
}
