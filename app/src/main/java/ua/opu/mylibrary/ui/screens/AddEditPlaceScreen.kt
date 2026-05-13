package ua.opu.mylibrary.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ua.opu.mylibrary.data.PlaceCategory
import ua.opu.mylibrary.data.toDisplayName
import ua.opu.mylibrary.ui.viewmodel.AddEditPlaceUiState

/**
 * Екран додавання / редагування місця.
 *
 * Це один екран для двох сценаріїв. Якщо uiState.isEditMode == false, користувач
 * додає нове місце. Якщо true — редагує вже існуюче місце.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditPlaceScreen(
    uiState: AddEditPlaceUiState,
    onNameChange: (String) -> Unit,
    onCountryChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onCategoryChange: (PlaceCategory) -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (uiState.isEditMode) "Редагування місця" else "Нове місце"
                    )
                },
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
            Text(
                text = "Основна інформація",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = uiState.name,
                onValueChange = onNameChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Назва місця") },
                placeholder = { Text("Наприклад: Парк, місто або музей") },
                singleLine = true
            )

            OutlinedTextField(
                value = uiState.country,
                onValueChange = onCountryChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Країна або регіон") },
                placeholder = { Text("Наприклад: Україна") },
                singleLine = true
            )

            OutlinedTextField(
                value = uiState.description,
                onValueChange = onDescriptionChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Опис") },
                placeholder = { Text("Коротко опишіть, чому це місце цікаве") },
                minLines = 4
            )

            Text(
                text = "Категорія",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            CategorySelector(
                selectedCategory = uiState.category,
                onCategoryChange = onCategoryChange
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onCancelClick,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 14.dp)
                ) {
                    Text(text = "Скасувати")
                }

                Button(
                    onClick = onSaveClick,
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(vertical = 14.dp),
                    enabled = uiState.name.isNotBlank() && uiState.country.isNotBlank()
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = if (uiState.isEditMode) "Зберегти" else "Додати")
                }
            }
        }
    }
}

/**
 * Вибір категорії через FilterChip.
 *
 * Тут навмисно не використовується складний DropdownMenu, щоб код був простішим
 * для студентів-початківців.
 */
@Composable
private fun CategorySelector(
    selectedCategory: PlaceCategory,
    onCategoryChange: (PlaceCategory) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CategoryChip(
                modifier = Modifier.weight(1f),
                category = PlaceCategory.City,
                selected = selectedCategory == PlaceCategory.City,
                onClick = { onCategoryChange(PlaceCategory.City) }
            )
            CategoryChip(
                modifier = Modifier.weight(1f),
                category = PlaceCategory.Nature,
                selected = selectedCategory == PlaceCategory.Nature,
                onClick = { onCategoryChange(PlaceCategory.Nature) }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CategoryChip(
                modifier = Modifier.weight(1f),
                category = PlaceCategory.Museum,
                selected = selectedCategory == PlaceCategory.Museum,
                onClick = { onCategoryChange(PlaceCategory.Museum) }
            )
            CategoryChip(
                modifier = Modifier.weight(1f),
                category = PlaceCategory.Beach,
                selected = selectedCategory == PlaceCategory.Beach,
                onClick = { onCategoryChange(PlaceCategory.Beach) }
            )
        }
    }
}

@Composable
private fun CategoryChip(
    modifier: Modifier = Modifier,
    category: PlaceCategory,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        modifier = modifier,
        selected = selected,
        onClick = onClick,
        label = {
            Text(text = category.toDisplayName())
        }
    )
}
