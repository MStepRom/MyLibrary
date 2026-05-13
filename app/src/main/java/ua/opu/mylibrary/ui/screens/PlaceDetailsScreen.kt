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
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ua.opu.mylibrary.data.TravelPlace
import ua.opu.mylibrary.data.toDisplayName
import ua.opu.mylibrary.ui.viewmodel.PlaceDetailsUiState

/**
 * Екран деталей місця.
 *
 * Він отримує вже підготовлений PlaceDetailsUiState. Якщо місце знайдено,
 * відображає його дані. Якщо ні — показує повідомлення про помилку.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceDetailsScreen(
    uiState: PlaceDetailsUiState,
    onBackClick: () -> Unit,
    onEditClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    onInterestLevelChange: (Int, Float) -> Unit,
    onVisitedChange: (Int, Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Деталі місця") },
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
        val place = uiState.place

        if (place == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Місце не знайдено",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onBackClick) {
                    Text(text = "Повернутися")
                }
            }
        } else {
            PlaceDetailsContent(
                place = place,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick,
                onInterestLevelChange = onInterestLevelChange,
                onVisitedChange = onVisitedChange
            )
        }
    }
}

@Composable
private fun PlaceDetailsContent(
    place: TravelPlace,
    modifier: Modifier = Modifier,
    onEditClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    onInterestLevelChange: (Int, Float) -> Unit,
    onVisitedChange: (Int, Boolean) -> Unit
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = place.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Країна / регіон: ${place.country}",
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "Категорія: ${place.category.toDisplayName()}",
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = place.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Рівень зацікавленості: ${place.interestLevel.toInt()}%",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                /**
                 * Slider змінює числове значення. У цьому демо він не відкриває
                 * окремий екран, а одразу оновлює об'єкт у ViewModel.
                 */
                Slider(
                    value = place.interestLevel,
                    onValueChange = { newValue ->
                        onInterestLevelChange(place.id, newValue)
                    },
                    valueRange = 0f..100f
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (place.visited) "Місце вже відвідано" else "Місце ще не відвідано",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Switch(
                        checked = place.visited,
                        onCheckedChange = { checked ->
                            onVisitedChange(place.id, checked)
                        }
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { onEditClick(place.id) },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null
                )
                Text(text = "Редагувати")
            }

            Button(
                onClick = { onDeleteClick(place.id) },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null
                )
                Text(text = "Видалити")
            }
        }
    }
}
