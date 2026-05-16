package ua.opu.mylibrary.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ua.opu.mylibrary.ui.viewmodel.AddEditBookUiState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.layout.Box

/**
 * Екран додавання / редагування книги.
 *
 * Це один екран для двох сценаріїв. Якщо uiState.isEditMode == false, користувач
 * додає нову книгу. Якщо true — редагує вже існуючу книгу.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditBookScreen(
    uiState: AddEditBookUiState,
    onNameChange: (String) -> Unit,
    onAuthorChange: (String) -> Unit,
    onYearChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit
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
                            text = if (uiState.isEditMode) "Редагування книги" else "Нова книга",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = onCancelClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Повернутися назад"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.name,
                onValueChange = onNameChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(12.dp)
                    ),
                placeholder = {
                    Text(
                        text = "Назва книги",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,

                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )

            OutlinedTextField(
                value = uiState.author,
                onValueChange = onAuthorChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(12.dp)
                    ),
                placeholder = {
                    Text(
                        text = "Автор",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,

                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )

            OutlinedTextField(
                value = uiState.year,
                onValueChange = { newValue ->
                    // Дозволяємо лише цифри та обмежуємо довжину до 4 символів
                    if (newValue.all { it.isDigit() } && newValue.length <= 4) {
                        onYearChange(newValue)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(12.dp)
                    ),
                placeholder = {
                    Text(
                        text = "Рід видання",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,

                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                // Включаємо цифрову клавіатуру
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onCancelClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color.Black),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    contentPadding = PaddingValues(vertical = 18.dp)
                ) {
                    Text(
                        text = "Скасувати",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                Button(
                    onClick = onSaveClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color.Black),
                    enabled = uiState.name.isNotBlank() && uiState.author.isNotBlank(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black,
                        disabledContainerColor = Color.White,
                        disabledContentColor = Color.Gray
                    ),
                    contentPadding = PaddingValues(vertical = 18.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = if (uiState.isEditMode) "Зберегти" else "Додати",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}