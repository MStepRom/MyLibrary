package ua.opu.mylibrary.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF9C27B0),        // основний рожево-фіолетовий
    onPrimary = Color.White,            // текст/іконки на primary

    secondary = Color(0xFFE1BEE7),      // світло-фіолетовий
    onSecondary = Color(0xFF2B124C),

    tertiary = Color(0xFFFFEB3B),       // жовтий для зірок

    background = Color(0xFFF5EDF5),
    surface = Color(0xFFFFFCFF),
    onSurface = Color(0xFF1D1B20),
    onSurfaceVariant = Color(0xFF5F5863)

)

/**
 * Єдина тема застосунку.
 *
 * Окремий файл теми потрібен, щоб усі екрани мали однаковий стиль.
 */
@Composable
fun MyLibraryTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}
