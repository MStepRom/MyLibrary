package ua.opu.mylibrary.data

/**
 * Модель одного місця для мандрівки.
 *
 * У цій лабораторній роботі дані зберігаються тільки у пам'яті застосунку,
 * тобто у звичайному списку всередині ViewModel. База даних поки не використовується.
 *
 * Поле id потрібне для навігації: коли користувач натискає на місце у списку,
 * на екран деталей передається тільки id, а ViewModel уже знаходить об'єкт у списку.
 */
data class TravelPlace(
    val id: Int,
    val name: String,
    val country: String,
    val description: String,
    val category: PlaceCategory,
    val interestLevel: Float,
    val visited: Boolean
)

/**
 * Категорія місця.
 *
 * Enum використовується замість довільного тексту, щоб студентам було простіше
 * обмежити вибір кількома зрозумілими варіантами.
 */
enum class PlaceCategory {
    City,
    Nature,
    Museum,
    Beach
}

/**
 * Перетворює enum у текст, який буде показано користувачу.
 *
 * Такі функції зручно тримати поруч із моделлю, тому що вони описують
 * відображення конкретного значення предметної області.
 */
fun PlaceCategory.toDisplayName(): String {
    return when (this) {
        PlaceCategory.City -> "Місто"
        PlaceCategory.Nature -> "Природа"
        PlaceCategory.Museum -> "Музей"
        PlaceCategory.Beach -> "Пляж"
    }
}
