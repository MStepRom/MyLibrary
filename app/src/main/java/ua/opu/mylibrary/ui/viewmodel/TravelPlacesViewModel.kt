package ua.opu.mylibrary.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ua.opu.mylibrary.data.PlaceCategory
import ua.opu.mylibrary.data.TravelPlace

/**
 * ViewModel є центральним місцем зберігання стану для демонстраційного застосунку.
 *
 * У цьому проєкті НЕ використовується база даних. Список місць зберігається
 * у MutableStateFlow всередині ViewModel. Це зроблено спеціально для теми
 * "Навігація в Jetpack Compose", щоб студенти зосередилися на переходах,
 * передачі аргументів та роботі зі станом.
 */
class TravelPlacesViewModel : ViewModel() {

    /**
     * Внутрішній список місць.
     *
     * Він приватний, тому UI не може змінити його напряму. Усі зміни проходять
     * через спеціальні методи ViewModel: addPlace, updatePlace, deletePlace тощо.
     */
    private val _places = MutableStateFlow(
        listOf(
            TravelPlace(
                id = 1,
                name = "Львівська площа Ринок",
                country = "Україна",
                description = "Історичний центр міста з кав'ярнями, архітектурою та музеями.",
                category = PlaceCategory.City,
                interestLevel = 85f,
                visited = true
            ),
            TravelPlace(
                id = 2,
                name = "Озеро Синевир",
                country = "Україна",
                description = "Гірське озеро в Карпатах, яке часто називають морським оком.",
                category = PlaceCategory.Nature,
                interestLevel = 95f,
                visited = false
            ),
            TravelPlace(
                id = 3,
                name = "Одеський художній музей",
                country = "Україна",
                description = "Музей з великою колекцією українського та європейського мистецтва.",
                category = PlaceCategory.Museum,
                interestLevel = 70f,
                visited = false
            )
        )
    )

    /**
     * Стан екрана списку.
     *
     * Ми дублюємо список у UI State, щоб екран працював не з внутрішнім сховищем,
     * а з готовою моделлю стану інтерфейсу.
     */
    private val _placesListUiState = MutableStateFlow(
        PlacesListUiState(places = _places.value)
    )
    val placesListUiState: StateFlow<PlacesListUiState> = _placesListUiState.asStateFlow()

    /**
     * Стан форми додавання / редагування.
     */
    private val _addEditPlaceUiState = MutableStateFlow(AddEditPlaceUiState())
    val addEditPlaceUiState: StateFlow<AddEditPlaceUiState> = _addEditPlaceUiState.asStateFlow()

    /**
     * Стан екрана деталей.
     */
    private val _placeDetailsUiState = MutableStateFlow(PlaceDetailsUiState())
    val placeDetailsUiState: StateFlow<PlaceDetailsUiState> = _placeDetailsUiState.asStateFlow()

    /**
     * Готує форму для додавання нового місця.
     *
     * Важливо очистити поля перед переходом на екран додавання, інакше там можуть
     * залишитися дані попереднього редагування.
     */
    fun prepareNewPlace() {
        _addEditPlaceUiState.value = AddEditPlaceUiState(
            placeId = null,
            name = "",
            country = "",
            description = "",
            category = PlaceCategory.City,
            isEditMode = false
        )
    }

    /**
     * Готує форму для редагування існуючого місця.
     *
     * На екран редагування передається тільки id. Далі ViewModel шукає потрібний
     * об'єкт у списку і заповнює поля форми.
     */
    fun prepareEditPlace(placeId: Int) {
        val place = _places.value.firstOrNull { it.id == placeId }

        if (place != null) {
            _addEditPlaceUiState.value = AddEditPlaceUiState(
                placeId = place.id,
                name = place.name,
                country = place.country,
                description = place.description,
                category = place.category,
                isEditMode = true
            )
        }
    }

    /**
     * Готує екран деталей.
     *
     * Екран деталей також отримує тільки id. Це демонструє правильний підхід:
     * між екранами передаємо простий аргумент, а не весь об'єкт.
     */
    fun prepareDetails(placeId: Int) {
        val place = _places.value.firstOrNull { it.id == placeId }
        _placeDetailsUiState.value = PlaceDetailsUiState(place = place)
    }

    fun updateName(name: String) {
        _addEditPlaceUiState.update { currentState ->
            currentState.copy(name = name)
        }
    }

    fun updateCountry(country: String) {
        _addEditPlaceUiState.update { currentState ->
            currentState.copy(country = country)
        }
    }

    fun updateDescription(description: String) {
        _addEditPlaceUiState.update { currentState ->
            currentState.copy(description = description)
        }
    }

    fun updateCategory(category: PlaceCategory) {
        _addEditPlaceUiState.update { currentState ->
            currentState.copy(category = category)
        }
    }

    /**
     * Зберігає дані з форми.
     *
     * Якщо isEditMode == false, створюється нове місце.
     * Якщо isEditMode == true, оновлюється існуюче місце.
     */
    fun savePlace() {
        val state = _addEditPlaceUiState.value

        if (state.name.isBlank() || state.country.isBlank()) {
            return
        }

        if (state.isEditMode) {
            updateExistingPlace(state)
        } else {
            addNewPlace(state)
        }

        refreshListState()
    }

    /**
     * Оновлює рівень зацікавленості на екрані деталей.
     *
     * Slider повертає Float, тому interestLevel теж має тип Float.
     */
    fun updateInterestLevel(placeId: Int, interestLevel: Float) {
        _places.update { currentPlaces ->
            currentPlaces.map { place ->
                if (place.id == placeId) {
                    place.copy(interestLevel = interestLevel)
                } else {
                    place
                }
            }
        }

        prepareDetails(placeId)
        refreshListState()
    }

    /**
     * Змінює статус відвідування.
     */
    fun updateVisited(placeId: Int, visited: Boolean) {
        _places.update { currentPlaces ->
            currentPlaces.map { place ->
                if (place.id == placeId) {
                    place.copy(visited = visited)
                } else {
                    place
                }
            }
        }

        prepareDetails(placeId)
        refreshListState()
    }

    /**
     * Видаляє місце за id.
     */
    fun deletePlace(placeId: Int) {
        _places.update { currentPlaces ->
            currentPlaces.filterNot { place -> place.id == placeId }
        }

        refreshListState()
    }

    private fun addNewPlace(state: AddEditPlaceUiState) {
        val newPlace = TravelPlace(
            id = generateNextId(),
            name = state.name.trim(),
            country = state.country.trim(),
            description = state.description.trim(),
            category = state.category,
            interestLevel = 50f,
            visited = false
        )

        _places.update { currentPlaces ->
            currentPlaces + newPlace
        }
    }

    private fun updateExistingPlace(state: AddEditPlaceUiState) {
        val placeId = state.placeId ?: return

        _places.update { currentPlaces ->
            currentPlaces.map { place ->
                if (place.id == placeId) {
                    place.copy(
                        name = state.name.trim(),
                        country = state.country.trim(),
                        description = state.description.trim(),
                        category = state.category
                    )
                } else {
                    place
                }
            }
        }
    }

    private fun refreshListState() {
        _placesListUiState.value = PlacesListUiState(places = _places.value)
    }

    /**
     * Генерує новий id на основі максимального id у списку.
     *
     * Для навчального проєкту цього достатньо. У реальних застосунках id часто
     * генерує база даних або сервер.
     */
    private fun generateNextId(): Int {
        return (_places.value.maxOfOrNull { it.id } ?: 0) + 1
    }
}
