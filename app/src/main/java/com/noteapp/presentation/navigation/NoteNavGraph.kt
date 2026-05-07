package com.noteapp.presentation.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.noteapp.presentation.screens.detail.NoteDetailScreen
import com.noteapp.presentation.screens.edit.NoteEditScreen
import com.noteapp.presentation.screens.list.NoteListScreen

sealed class Screen(val route: String) {
    object NoteList : Screen("note_list")
    object NoteDetail : Screen("note_detail/{noteId}") {
        fun createRoute(noteId: Int) = "note_detail/$noteId"
    }
    object NoteEdit : Screen("note_edit?noteId={noteId}") {
        fun createRoute(noteId: Int? = null) =
            if (noteId != null) "note_edit?noteId=$noteId" else "note_edit"
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NoteNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.NoteList.route
    ) {
        composable(
            route = Screen.NoteList.route,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            NoteListScreen(
                onNoteClick = { noteId ->
                    navController.navigate(Screen.NoteDetail.createRoute(noteId))
                },
                onCreateNote = {
                    navController.navigate(Screen.NoteEdit.createRoute())
                }
            )
        }

        composable(
            route = Screen.NoteDetail.route,
            arguments = listOf(navArgument("noteId") { type = NavType.IntType }),
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(350))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(350))
            }
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getInt("noteId") ?: return@composable
            NoteDetailScreen(
                noteId = noteId,
                onEditNote = { id ->
                    navController.navigate(Screen.NoteEdit.createRoute(id))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.NoteEdit.route,
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            ),
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, tween(350))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, tween(350))
            }
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getInt("noteId")?.takeIf { it != -1 }
            NoteEditScreen(
                noteId = noteId,
                onSave = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
