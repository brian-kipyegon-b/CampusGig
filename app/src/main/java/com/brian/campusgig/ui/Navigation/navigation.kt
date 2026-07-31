package com.brian.campusgig.ui.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.brian.campusgig.ui.Screens.Authentication.LogIn.LogInPage
import com.brian.campusgig.ui.Screens.Authentication.Register.RegisterPage
import com.brian.campusgig.ui.Screens.Employer.EditGigScreen
import com.brian.campusgig.ui.Screens.Employer.EmployerApplicantsPage
import com.brian.campusgig.ui.Screens.Employer.EmployerApplicationDetailsPage
import com.brian.campusgig.ui.Screens.Employer.EmployerApplicationsScreen
import com.brian.campusgig.ui.Screens.Employer.EmployerDashboard
import com.brian.campusgig.ui.Screens.Employer.EmployerGigDetailsScreen
import com.brian.campusgig.ui.Screens.Employer.EmployerGigPage
import com.brian.campusgig.ui.Screens.Employer.createGigScreen
import com.brian.campusgig.ui.Screens.Landing.LandingPage
import com.brian.campusgig.ui.Screens.Notifications.NotificationPage
import com.brian.campusgig.ui.Screens.Profile.ProfileScreen
import com.brian.campusgig.ui.Screens.Splash.SplashScreen
import com.brian.campusgig.ui.Screens.Student.ApplicationPage
import com.brian.campusgig.ui.Screens.Student.BrowseGigsPage
import com.brian.campusgig.ui.Screens.Student.StudentApplicationDetailsPage
import com.brian.campusgig.ui.Screens.Student.StudentApplicationsPage
import com.brian.campusgig.ui.Screens.Student.StudentDashboardPage
import com.brian.campusgig.ui.Screens.Student.StudentGigDetailsPage

@Composable
fun Navigation(
    modifier: Modifier,
    navHostController: NavHostController
){
    NavHost(
        navController = navHostController,
        startDestination = Splash
    ) {
        composable<Landing>{LandingPage(modifier, navHostController)}
        composable<Splash>{ SplashScreen(navHostController) }
        composable<Contact>{}
        composable<Register>{RegisterPage(modifier, navHostController)}
        composable<Login>{LogInPage(modifier, navHostController)}
        composable<StudentDashboard> { StudentDashboardPage(navHostController) }
        composable<EmployerDashboard> { EmployerDashboard(navHostController) }
        composable<StudentMessages>{}
        composable<MyApplications>{}
        composable<EmployerGigs> {
            EmployerGigPage(
                navHostController = navHostController
            )
        }
        composable<EditGig> { backStackEntry ->
            val args = backStackEntry.toRoute<EditGig>()
            EditGigScreen(
                navHostController = navHostController,
                gigId = args.gigId
            )
        }
        composable<GigDetails> { backStackEntry ->
            val args = backStackEntry.toRoute<GigDetails>()
            EmployerGigDetailsScreen(navHostController = navHostController, gigId = args.gigId)
        }

        //student dashboard features
        composable<BrowseGigs>{BrowseGigsPage(navHostController)}

        //Applications
        composable<ApplyGig> { backStackEntry ->
            val args = backStackEntry.toRoute<ApplyGig>()
            ApplicationPage(
                gigId = args.gigId,
                navHostController = navHostController
            )
        }

        composable<ViewGig> { backStackEntry ->
            val args = backStackEntry.toRoute<ViewGig>()
            StudentGigDetailsPage(
                gigId = args.gigId,
                navHostController = navHostController
            )

        }

        composable<StudentApplications> {
            StudentApplicationsPage(navHostController)
        }
        composable <CreateGig>{createGigScreen(navHostController)}

        composable<ApplicationDetails> { backStackEntry ->
            val args = backStackEntry.toRoute<ApplicationDetails>()
            StudentApplicationDetailsPage(
                applicationId = args.applicationId,
                navHostController = navHostController
            )

        }
        composable<EmployerApplicants> { backStackEntry ->

            val args = backStackEntry.toRoute<EmployerApplicants>()

            EmployerApplicantsPage(
                gigId = args.gigId,
                navHostController = navHostController
            )

        }

        composable<EmployerApplications> {
            EmployerApplicationsScreen(
                navHostController = navHostController,
                onApplicationClick = { applicationId ->
                    navHostController.navigate(
                        EmployerApplicationDetails(applicationId)
                    )
                }
            )

        }
        composable<EmployerApplicationDetails> { backStackEntry ->
            val args = backStackEntry.toRoute<EmployerApplicationDetails>()
            EmployerApplicationDetailsPage(
                applicationId = args.applicationId,
                navHostController = navHostController
            )

        }
        composable<Notifications> {
            NotificationPage(navHostController)
        }
        composable<Profile> {
            ProfileScreen(
                navController = navHostController
            )
        }
    }
}
