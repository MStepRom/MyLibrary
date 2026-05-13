package ua.opu.mylibrary.ui.viewmodel

import ua.opu.mylibrary.data.PlaceCategory
import ua.opu.mylibrary.data.TravelPlace

/**
 * Стан екрана списку.
 *
 * Екран списку не повинен самостійно зберігати дані або змінювати список.
 * Він отримує готовий стан із ViewModel і тільки відображає його.
 */
data class PlacesListUiState(
    val places: List<TravelPlace> = emptyList()
)

/**
 * Стан екрана додавання / редагування.
 *
 * Один екран використовується у двох режимах:
 * 1. Додавання нового місця — placeId == null.
 * 2. Редагування існуючого місця — placeId має конкретне значення.
 */
data class AddEditPlaceUiState(
    val placeId: Int? = null,
    val name: String = "",
    val country: String = "",
    val description: String = "",
    val category: PlaceCategory = PlaceCategory.City,
    val isEditMode: Boolean = false
)

/**
 * Стан екрана деталей.
 *
 * Тут зберігається вибране місце. Якщо id неправильний або об'єкт було видалено,
 * place може бути null, і екран покаже повідомлення про помилку.
 */
data class PlaceDetailsUiState(
    val place: TravelPlace? = null
)
