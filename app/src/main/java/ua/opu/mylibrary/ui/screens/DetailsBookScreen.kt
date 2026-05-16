package ua.opu.mylibrary.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ua.opu.mylibrary.data.Book
import ua.opu.mylibrary.ui.viewmodel.BookDetailsUiState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults

/**
 * Екран деталей книги.
 *
 * Він отримує вже підготовлений BookDetailsUiState. Якщо книгу знайдено,
 * відображає її дані. Якщо ні — показує повідомлення про помилку.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsBookScreen(
    uiState: BookDetailsUiState,
    onBackClick: () -> Unit,
    onEditClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    onRatingChange: (Int, Float) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Деталі книги",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Повернутися назад"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        val book = uiState.book

        if (book == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Книгу не знайдено",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onBackClick) {
                    Text(text = "Повернутися")
                }
            }
        } else {
            BookDetailsContent(
                book = book,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick,
                onRatingChange = onRatingChange
            )
        }
    }
}

@Composable
private fun BookDetailsContent(
    book: Book,
    modifier: Modifier = Modifier,
    onEditClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    onRatingChange: (Int, Float) -> Unit
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "📖",
                    style = MaterialTheme.typography.displayMedium
                )

                Text(
                    text = book.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = book.author,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Рік видання",
                    style = MaterialTheme.typography.headlineSmall,
                )

                Text(
                    text = book.year.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Ваша оцінка",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Row (
                ) {
                    for (i in 0 until 5) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            // Якщо індекс менше рейтингу, зірка жовта, інакше сіра
                            tint = if (i < book.rating)
                            {MaterialTheme.colorScheme.tertiary}
                            else
                            {MaterialTheme.colorScheme.outline}
                        )
                    }
                }
                /**
                 * Slider змінює числове значення, одразу оновлює об'єкт у ViewModel.
                 */
                Slider(
                    value = book.rating,
                    onValueChange = { onRatingChange(book.id, it) },
                    valueRange = 0f..5f,
                    steps = 4 // (1, 2, 3, 4 - це кроки між 0 та 5)
                )
            }
        }

        OutlinedButton(
            onClick = { onEditClick(book.id) },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color.Black),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            contentPadding = PaddingValues(vertical = 18.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null
            )
            Text(
                text = "Редагувати",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        OutlinedButton(
            onClick = { onDeleteClick(book.id) },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color.Black),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            contentPadding = PaddingValues(vertical = 18.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null
            )
            Text(
                text = "Видалити",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
