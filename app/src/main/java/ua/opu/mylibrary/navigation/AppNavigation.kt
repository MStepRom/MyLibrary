package ua.opu.mylibrary.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ua.opu.mylibrary.ui.screens.AddEditBookScreen
import ua.opu.mylibrary.ui.screens.BooksListScreen
import ua.opu.mylibrary.ui.screens.DetailsBookScreen
import ua.opu.mylibrary.ui.viewmodel.MyLibraryViewModel

/**
 * Об'єкт із маршрутами застосунку.
 *
 * Константи потрібні, щоб не писати рядки маршрутів вручну в різних місцях.
 * Це зменшує ризик помилки в назві маршруту.
 */
object AppRoutes {
    const val BOOKS_LIST = "books_list"
    const val BOOK_ADD = "book_add"
    const val BOOK_EDIT = "book_edit/{bookId}"
    const val BOOK_DETAILS = "book_details/{bookId}"

    fun bookEdit(bookId: Int): String {
        return "book_edit/$bookId"
    }

    fun bookDetails(bookId: Int): String {
        return "book_details/$bookId"
    }
}

/**
 * Навігаційний граф застосунку.
 *
 * У цьому файлі описано, які екрани є в застосунку та як між ними переходити.
 * Важливо: між екранами передається тільки id об'єкта.
 */
@Composable
fun AppNavigation(
    viewModel: MyLibraryViewModel,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.BOOKS_LIST
    ) {
        composable(route = AppRoutes.BOOKS_LIST) {
            val uiState by viewModel.booksListUiState.collectAsState()

            BooksListScreen(
                uiState = uiState,
                onAddClick = {
                    viewModel.prepareNewBook()
                    navController.navigate(AppRoutes.BOOK_ADD)
                },
                onBookClick = { bookId ->
                    navController.navigate(AppRoutes.bookDetails(bookId))
                }
            )
        }

        composable(route = AppRoutes.BOOK_ADD) {
            val uiState by viewModel.addEditBookUiState.collectAsState()

            AddEditBookScreen(
                uiState = uiState,
                onNameChange = viewModel::updateName,
                onAuthorChange = viewModel::updateAuthor,
                onYearChange = viewModel::updateYear,
                onSaveClick = {
                    viewModel.saveBook()
                    navController.popBackStack()
                },
                onCancelClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = AppRoutes.BOOK_EDIT,
            arguments = listOf(
                navArgument("bookId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getInt("bookId") //?: 0

            LaunchedEffect(bookId) {
                if (bookId != null) {
                    viewModel.prepareEditBook(bookId)
                }
            }

            val uiState by viewModel.addEditBookUiState.collectAsState()

            AddEditBookScreen(
                uiState = uiState,
                onNameChange = viewModel::updateName,
                onAuthorChange = viewModel::updateAuthor,
                onYearChange = viewModel::updateYear,
                onSaveClick = {
                    viewModel.saveBook()
                    navController.popBackStack()
                },
                onCancelClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = AppRoutes.BOOK_DETAILS,
            arguments = listOf(
                navArgument("bookId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getInt("bookId")

            LaunchedEffect(bookId) {
                if (bookId != null) {
                    viewModel.prepareDetails(bookId)
                }
            }

            val uiState by viewModel.bookDetailsUiState.collectAsState()

            DetailsBookScreen(
                uiState = uiState,
                onBackClick = {
                    navController.popBackStack()
                },
                onEditClick = { editBookId ->
                    navController.navigate(AppRoutes.bookEdit(editBookId))
                },
                onDeleteClick = { deleteBookId ->
                    viewModel.deleteBook(deleteBookId)
                    navController.popBackStack()
                },
                onRatingChange = { editBookId, rating ->
                    viewModel.updateRating(editBookId, rating)
                }
            )
        }
    }
}