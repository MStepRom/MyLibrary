package ua.opu.mylibrary.ui.viewmodel

import ua.opu.mylibrary.data.Book

/**
 * Стан екрана списку.
 *
 * Екран списку не повинен самостійно зберігати дані або змінювати список.
 * Він отримує готовий стан із ViewModel і тільки відображає його.
 */
data class BooksListUiState(
    val books: List<Book> = emptyList()
)

/**
 * Стан екрана додавання / редагування.
 *
 * Один екран використовується у двох режимах:
 * 1. Додавання нової книги — bookId == null.
 * 2. Редагування існуючої книги — bookId має конкретне значення.
 */
data class AddEditBookUiState(
    val bookId: Int? = null,
    val name: String = "",
    val author: String = "",
    val year: String = "", //рік тут зберігається як рядок, це для елементу інтерфейсу
    val isEditMode: Boolean = false
)

/**
 * Стан екрана деталей.
 *
 * Тут зберігається вибрана книга. Якщо id неправильний або об'єкт було видалено,
 * place може бути null, і екран покаже повідомлення про помилку.
 */
data class BookDetailsUiState(
    val book: Book? = null
)
