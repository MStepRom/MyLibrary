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
import ua.opu.mylibrary.ui.screens.AddEditPlaceScreen
import ua.opu.mylibrary.ui.screens.PlaceDetailsScreen
import ua.opu.mylibrary.ui.screens.PlacesListScreen
import ua.opu.mylibrary.ui.viewmodel.TravelPlacesViewModel

/**
 * Об'єкт із маршрутами застосунку.
 *
 * Константи потрібні, щоб не писати рядки маршрутів вручну в різних місцях.
 * Це зменшує ризик помилки в назві маршруту.
 */
object AppRoutes {
    const val PLACES_LIST = "places_list"
    const val PLACE_ADD = "place_add"
    const val PLACE_EDIT = "place_edit/{placeId}"
    const val PLACE_DETAILS = "place_details/{placeId}"

    fun placeEdit(placeId: Int): String {
        return "place_edit/$placeId"
    }

    fun placeDetails(placeId: Int): String {
        return "place_details/$placeId"
    }
}

/**
 * Навігаційний граф застосунку.
 *
 * У цьому файлі описано, які екрани є в застосунку та як між ними переходити.
 * Важливо: між екранами передається тільки id об'єкта, а не весь TravelPlace.
 */
@Composable
fun AppNavigation(
    viewModel: TravelPlacesViewModel,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.PLACES_LIST
    ) {
        composable(route = AppRoutes.PLACES_LIST) {
            val uiState by viewModel.placesListUiState.collectAsState()

            PlacesListScreen(
                uiState = uiState,
                onAddClick = {
                    viewModel.prepareNewPlace()
                    navController.navigate(AppRoutes.PLACE_ADD)
                },
                onPlaceClick = { placeId ->
                    navController.navigate(AppRoutes.placeDetails(placeId))
                }
            )
        }

        composable(route = AppRoutes.PLACE_ADD) {
            val uiState by viewModel.addEditPlaceUiState.collectAsState()

            AddEditPlaceScreen(
                uiState = uiState,
                onNameChange = viewModel::updateName,
                onCountryChange = viewModel::updateCountry,
                onDescriptionChange = viewModel::updateDescription,
                onCategoryChange = viewModel::updateCategory,
                onSaveClick = {
                    viewModel.savePlace()
                    navController.popBackStack()
                },
                onCancelClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = AppRoutes.PLACE_EDIT,
            arguments = listOf(
                navArgument("placeId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val placeId = backStackEntry.arguments?.getInt("placeId")

            LaunchedEffect(placeId) {
                if (placeId != null) {
                    viewModel.prepareEditPlace(placeId)
                }
            }

            val uiState by viewModel.addEditPlaceUiState.collectAsState()

            AddEditPlaceScreen(
                uiState = uiState,
                onNameChange = viewModel::updateName,
                onCountryChange = viewModel::updateCountry,
                onDescriptionChange = viewModel::updateDescription,
                onCategoryChange = viewModel::updateCategory,
                onSaveClick = {
                    viewModel.savePlace()
                    navController.popBackStack()
                },
                onCancelClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = AppRoutes.PLACE_DETAILS,
            arguments = listOf(
                navArgument("placeId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val placeId = backStackEntry.arguments?.getInt("placeId")

            LaunchedEffect(placeId) {
                if (placeId != null) {
                    viewModel.prepareDetails(placeId)
                }
            }

            val uiState by viewModel.placeDetailsUiState.collectAsState()

            PlaceDetailsScreen(
                uiState = uiState,
                onBackClick = {
                    navController.popBackStack()
                },
                onEditClick = { editPlaceId ->
                    navController.navigate(AppRoutes.placeEdit(editPlaceId))
                },
                onDeleteClick = { deletePlaceId ->
                    viewModel.deletePlace(deletePlaceId)
                    navController.popBackStack()
                },
                onInterestLevelChange = { editPlaceId, level ->
                    viewModel.updateInterestLevel(editPlaceId, level)
                },
                onVisitedChange = { editPlaceId, visited ->
                    viewModel.updateVisited(editPlaceId, visited)
                }
            )
        }
    }
}
