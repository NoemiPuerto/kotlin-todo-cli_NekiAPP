package com.neki.app.core.navigation

import com.neki.app.R
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.neki.app.core.components.BottomNavigationBar
import com.neki.app.features.focus.presentation.FocusScreen
import com.neki.app.features.notes.presentation.NotesScreen
import com.neki.app.features.tasks.presentation.TaskScreen
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.padding

@Composable
fun NekiNavigation() {
    val navController = rememberNavController()

    val items = listOf(
        BottomNavItem(Routes.TASKS, "Tasks", R.drawable.ic_bulletlist),
        BottomNavItem(Routes.FOCUS, "Focus", R.drawable.ic_clock),
        BottomNavItem(Routes.NOTES, "Notes", R.drawable.ic_note)
    )

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            BottomNavigationBar(
                items = items,
                currentRoute = currentRoute,
                onItemClick = { item ->
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Routes.TASKS,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
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