/*
 * Conjunto de componentes visuales compartidos: barra superior, FAB animado, diálogos,
 * divisores, chips, placeholders, etc. Cada uno encapsula un pequeño comportamiento visual
 * reutilizable en varias pantallas.
 */
package com.noteapp.presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noteapp.presentation.theme.*

// Barra superior reutilizable.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteTopBar(
    title: String,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = OnBackground
                )
            )
        },
        navigationIcon = { navigationIcon?.invoke() },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = DarkBackground,
            titleContentColor = OnBackground,
            navigationIconContentColor = OnBackground,
            actionIconContentColor = OnBackground
        )
    )
}

// Botón flotante principal con animación de presión.
@Composable
fun PrimaryFab(
    onClick: () -> Unit,
    icon: ImageVector = Icons.Default.Add,
    contentDescription: String = "Crear nota"
) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.92f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "fab_scale"
    )

    FloatingActionButton(
        onClick = onClick,
        containerColor = AccentPrimary,
        contentColor = OnPrimary,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.scale(scale)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp)
        )
    }
}

// Overlay de carga.
@Composable
fun LoadingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground.copy(alpha = 0.7f)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = AccentPrimary,
            modifier = Modifier.size(48.dp),
            strokeWidth = 3.dp
        )
    }
}

// Snackbar personalizado para errores.
@Composable
fun ErrorSnackbar(
    message: String,
    onDismiss: () -> Unit
) {
    Snackbar(
        modifier = Modifier.padding(16.dp),
        action = {
            TextButton(onClick = onDismiss) {
                Text("OK", color = AccentPrimary)
            }
        },
        containerColor = DarkSurfaceVariant,
        contentColor = OnSurface,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(message)
    }
}

// Divisor con gradiente.
@Composable
fun GradientDivider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        DividerColor,
                        DividerColor,
                        Color.Transparent
                    )
                )
            )
    )
}

// Pequeña etiqueta tipo chip.
@Composable
fun ChipLabel(
    text: String,
    color: Color = AccentPrimary,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.5.sp
        )
    }
}

// Diálogo de confirmación para eliminar una nota.
@Composable
fun DeleteConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        titleContentColor = OnBackground,
        textContentColor = OnSurface,
        title = {
            Text(
                "Eliminar nota",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Text(
                "¿Estás seguro de que quieres eliminar esta nota? Esta acción no se puede deshacer.",
                style = MaterialTheme.typography.bodyMedium,
                color = OnSurfaceSubtle
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentError,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Eliminar", fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = OnSurfaceSubtle)
            }
        }
    )
}

// Placeholder mostrado cuando no hay notas, con una animación de pulso.
@Composable
fun EmptyNotesPlaceholder(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val infiniteTransition = rememberInfiniteTransition(label = "pulse")
        val alpha by infiniteTransition.animateFloat(
            initialValue = 0.4f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1800, easing = EaseInOutSine),
                repeatMode = RepeatMode.Reverse
            ),
            label = "alpha_pulse"
        )

        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(AccentPrimary.copy(alpha = alpha * 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Text("📝", fontSize = 36.sp)
        }

        Spacer(Modifier.height(20.dp))

        Text(
            text = "Sin notas todavía",
            style = MaterialTheme.typography.titleMedium.copy(
                color = OnBackground,
                fontWeight = FontWeight.SemiBold
            )
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Toca el botón + para crear tu primera nota",
            style = MaterialTheme.typography.bodyMedium.copy(color = OnSurfaceSubtle),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}