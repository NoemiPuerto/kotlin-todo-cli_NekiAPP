package com.neki.app.core.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.neki.app.R
import com.neki.app.core.components.BottomNavigationBar
import com.neki.app.features.focus.presentation.FocusScreen
import com.neki.app.features.notes.presentation.NotesScreen
import com.neki.app.features.tasks.presentation.TaskScreen
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import com.neki.app.features.loading.presentation.LoadingScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NekiNavigation() {
    val navController = rememberNavController()

    val items = listOf(
        BottomNavItem(Routes.TASKS, "Tasks", R.drawable.ic_bulletlist),
        BottomNavItem(Routes.FOCUS, "Focus", R.drawable.ic_clock),
        BottomNavItem(Routes.NOTES, "Notes", R.drawable.ic_note)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            if (currentRoute != null && currentRoute != Routes.LOADING) {
                BottomNavigationBar(
                    items = items,
                    currentRoute = currentRoute,
                    onItemClick = { item ->
                        navController.navigate(item.route) {
                            popUpTo(Routes.TASKS) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Routes.LOADING,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(Routes.LOADING) {
                LaunchedEffect(Unit) {
                    delay(2500)
                    navController.navigate(Routes.TASKS) {
                        popUpTo(Routes.LOADING) {
                            inclusive = true
                        }
                    }
                }

                LoadingScreen()
            }

            composable(Routes.TASKS) {
                TaskScreen()
            }

            composable(Routes.FOCUS) {
                FocusScreen()
            }

            composable(Routes.NOTES) {
                NotesScreen()
            }
        }
    }
}