package com.ahmed.vodafonerepos.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ahmed.vodafonerepos.data.models.dto.RepoDetailsRequest
import com.ahmed.vodafonerepos.ui.repodetails.RepoDetailsScreen
import com.ahmed.vodafonerepos.ui.repoissues.RepoIssuesListScreen
import com.ahmed.vodafonerepos.ui.reposlist.ReposListScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = NavigationItem.RepoList.route,
) {
    NavHost(
        modifier = modifier, navController = navController, startDestination = startDestination
    ) {
        composable(NavigationItem.RepoList.route) {
            ReposListScreen(navHostController = navController)
        }
        composable(
            route = "${NavigationItem.RepoDetails.route}/{repoDetailsRequest}",
            arguments = listOf(navArgument("repoDetailsRequest") {
                type = NavType.StringType
            })
        ) {
            RepoDetailsScreen(navHostController = navController)
        }
        composable(
            route = "${NavigationItem.RepoIssues.route}/{repoDetailsRequest}",
            arguments = listOf(navArgument("repoDetailsRequest") {
                type = NavType.StringType
            })
        ) {
            RepoIssuesListScreen(navHostController = navController)
        }
    }
}

enum class Screen {
    REPO_LIST, REPO_DETAILS, REPO_ISSUES,
}

sealed class NavigationItem(val route: String) {
    object RepoList : NavigationItem(Screen.REPO_LIST.name)
    object RepoDetails : NavigationItem(Screen.REPO_DETAILS.name)
    object RepoIssues : NavigationItem(Screen.REPO_ISSUES.name)
}