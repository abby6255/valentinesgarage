package com.example.valentinesgarage.ui.theme.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.valentinesgarage.ui.theme.checkin.CheckInScreen
import com.example.valentinesgarage.ui.theme.jobs.ActiveJobsScreen
import com.example.valentinesgarage.ui.theme.jobs.JobDetailScreen
import com.example.valentinesgarage.ui.theme.reports.EmployeeReportScreen

sealed class Screen(val route: String) {
    object ActiveJobs : Screen("activeJobs")
    object CheckIn : Screen("checkIn")
    object Reports : Screen("reports")
    object JobDetail : Screen("jobDetail/{jobId}") {
        fun passJobId(jobId: String) = "jobDetail/$jobId"
    }
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.ActiveJobs.route,
        modifier = modifier
    ) {
        composable(Screen.ActiveJobs.route) {
            ActiveJobsScreen(
                onJobClick = { jobId ->
                    navController.navigate(Screen.JobDetail.passJobId(jobId))
                }
            )
        }
        composable(Screen.CheckIn.route) {
            CheckInScreen(
                onCheckInSuccess = {
                    navController.navigate(Screen.ActiveJobs.route) {
                        popUpTo(Screen.ActiveJobs.route) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Screen.Reports.route) {
            EmployeeReportScreen()
        }
        composable(
            route = Screen.JobDetail.route,
            arguments = listOf(
                navArgument("jobId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId") ?: return@composable
            JobDetailScreen(jobId = jobId)
        }
    }
}