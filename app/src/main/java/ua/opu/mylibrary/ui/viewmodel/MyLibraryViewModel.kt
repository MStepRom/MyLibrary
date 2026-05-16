package ua.opu.mylibrary.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ua.opu.mylibrary.data.Book
import java.time.Year

/**
 * ViewModel є центральним місцем зберігання стану для застосунку.
 *
 * У цьому проєкті НЕ використовується база даних. Список книг зберігається
 * у MutableStateFlow всередині ViewModel.
 */
class MyLibraryViewModel : ViewModel() {
    /**
     * Внутрішній список книг.
     *
     * Він приватний, тому UI не може змінити його напряму. Усі зміни проходять
     * через спеціальні методи ViewModel: addBook, updateBook, deleteBook тощо.
     */
    private val _books = MutableStateFlow(
        listOf(
            Book(
                id = 1,
                name = "Хіба ревуть воли як ясла повні",
                author = "Панас Мирний",
                year = Year.of(1880),
                rating = 3.0f
            ),
            Book(
                id = 2,
                name = "1984",
                author = "Джордж Оруелл",
                year = Year.of(1949),
                rating = 4.0f
            ),
            Book(
                id = 3,
                name = "Відьмак",
                author = "Анджей Сапковський",
                year = Year.of(1986),
                rating = 5.0f
            )
        )
    )

    /**
     * Стан екрана списку.
     *
     * Ми дублюємо список у UI State, щоб екран працював не з внутрішнім сховищем,
     * а з готовою моделлю стану інтерфейсу.
     */
    private val _booksListUiState = MutableStateFlow(
        BooksListUiState(books = _books.value)
    )
    val booksListUiState: StateFlow<BooksListUiState> = _booksListUiState.asStateFlow()

    /**
     * Стан форми додавання / редагування.
     */
    private val _addEditBookUiState = MutableStateFlow(AddEditBookUiState())
    val addEditBookUiState: StateFlow<AddEditBookUiState> = _addEditBookUiState.asStateFlow()

    /**
     * Стан екрана деталей.
     */
    private val _bookDetailsUiState = MutableStateFlow(BookDetailsUiState())
    val bookDetailsUiState: StateFlow<BookDetailsUiState> = _bookDetailsUiState.asStateFlow()

    /**
     * Готує форму для додавання нової книги.
     *
     * Важливо очистити поля перед переходом на екран додавання, інакше там можуть
     * залишитися дані попереднього редагування.
     */
    fun prepareNewBook() {
        _addEditBookUiState.value = AddEditBookUiState(
            bookId = null,
            name = "",
            author = "",
            year = "",
            isEditMode = false
        )
    }

    /**
     * Готує форму для редагування існуючої книги.
     *
     * На екран редагування передається тільки id. Далі ViewModel шукає потрібний
     * об'єкт у списку і заповнює поля форми.
     */
    fun prepareEditBook(bookId: Int) {
        val book = _books.value.firstOrNull { it.id == bookId }

        if (book != null) {
            _addEditBookUiState.value = AddEditBookUiState(
                bookId = book.id,
                name = book.name,
                author = book.author,
                year = book.year.toString(),
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
    fun prepareDetails(bookId: Int) {
        val book = _books.value.firstOrNull { it.id == bookId }
        _bookDetailsUiState.value = BookDetailsUiState(book = book)
    }

    fun updateName(name: String) {
        _addEditBookUiState.update { currentState ->
            currentState.copy(name = name)
        }
    }

    fun updateAuthor(author: String) {
        _addEditBookUiState.update { currentState ->
            currentState.copy(author = author)
        }
    }

    fun updateYear(year: String) {
        _addEditBookUiState.update { currentState ->
            currentState.copy(year = year)
        }
    }

    /**
     * Зберігає дані з форми.
     *
     * Якщо isEditMode == false, створюється нова книга.
     * Якщо isEditMode == true, оновлюється існуюча книга.
     */
    fun saveBook() {
        val state = _addEditBookUiState.value

        if (state.name.isBlank() || state.author.isBlank()) {
            return
        }

        if (state.isEditMode) {
            updateExistingBook(state)
        } else {
            addNewBook(state)
        }

        refreshListState()
    }

    /**
     * Оновлює оцынку (рейтинг) на екрані деталей.
     *
     * Slider повертає Float, тому rating теж має тип Float.
     */
    fun updateRating(bookId: Int, rating: Float) {
        _books.update { currentBooks ->
            currentBooks.map { book ->
                if (book.id == bookId) {
                    book.copy(rating = rating)
                } else {
                    book
                }
            }
        }

        prepareDetails(bookId)
        refreshListState()
    }

    /**
     * Видаляє книгу за id.
     */
    fun deleteBook(bookId: Int) {
        _books.update { currentBooks ->
            currentBooks.filterNot { book -> book.id == bookId }
        }

        refreshListState()
    }

    private fun addNewBook(state: AddEditBookUiState) {
        val year = try {
            Year.of(state.year.toInt()) // state.year - це рядок, треба перетворити його на тип Year
        } catch (e: Exception) {
            Year.now() // рік був некоректний, використаємо поточний рік
        }
        val newBook = Book(
            id = generateNextId(),
            name = state.name.trim(),
            author = state.author.trim(),
            year = year,
            rating = 0.0f
        )

        _books.update { currentBooks ->
            currentBooks + newBook
        }
    }

    private fun updateExistingBook(state: AddEditBookUiState) {
        val bookId = state.bookId ?: return
        val year = try {
            Year.of(state.year.toInt()) // state.year - це рядок, треба перетворити його на тип Year
        } catch (e: Exception) {
            Year.now() // рік був некоректний, використаємо поточний рік
        }

        _books.update { currentBooks ->
            currentBooks.map { book ->
                if (book.id == bookId) {
                    book.copy(
                        name = state.name.trim(),
                        author = state.author.trim(),
                        year = year
                    )
                } else {
                    book
                }
            }
        }
    }

    private fun refreshListState() {
        _booksListUiState.value = BooksListUiState(books = _books.value)
    }

    /**
     * Генерує новий id на основі максимального id у списку.
     * У реальних застосунках id часто генерує база даних або сервер.
     */
    private fun generateNextId(): Int {
        return (_books.value.maxOfOrNull { it.id } ?: 0) + 1
    }

}