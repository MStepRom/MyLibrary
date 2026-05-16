package ua.opu.mylibrary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import ua.opu.mylibrary.navigation.AppNavigation
import ua.opu.mylibrary.ui.theme.MyLibraryTheme
import ua.opu.mylibrary.ui.viewmodel.MyLibraryViewModel

/**
 * Головна Activity застосунку.
 *
 * У Compose-застосунку Activity зазвичай тільки запускає тему, створює ViewModel
 * і передає її в навігаційний граф. Основна логіка розміщується не тут,
 * а у ViewModel та composable-екранах.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyLibraryTheme {
                val myLibraryViewModel: MyLibraryViewModel = viewModel()
                AppNavigation(
                    viewModel = myLibraryViewModel
                )
            }
        }
    }
}
