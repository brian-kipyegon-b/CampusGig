package com.brian.campusgig.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.brian.campusgig.R
import com.brian.campusgig.data.models.studentNavItem
import com.brian.campusgig.ui.Navigation.CreateGig
import com.brian.campusgig.ui.Navigation.EmployerApplicants
import com.brian.campusgig.ui.Navigation.EmployerApplications
import com.brian.campusgig.ui.Navigation.EmployerDashboard
import com.brian.campusgig.ui.Navigation.EmployerGigs
import com.brian.campusgig.ui.Navigation.EmployerProfile
import com.brian.campusgig.ui.theme.PrimaryPurple

@Composable
fun employerBottomNavigation(
    navHostController: NavHostController
){
    val routes = listOf(
        studentNavItem("Home", EmployerDashboard, R.drawable.home),
        studentNavItem("Add Gig", CreateGig, R.drawable.add),
        studentNavItem("My Gigs", EmployerGigs, R.drawable.gigs),
        studentNavItem("Applications", EmployerApplications, R.drawable.applications_icon),
        studentNavItem("Profile", EmployerProfile, R.drawable.user),
    )

    NavigationBar() {
        routes.forEach { route ->
            NavigationBarItem(
                onClick = {
                    navHostController.navigate(route.route)
                },
                label = {
                    Text(
                        text = route.name,
                        style = TextStyle(
                            color = Color.Black
                        )
                    )
                },
                selected = false,
                icon = {
                    Icon(
                        painter = painterResource(
                            route.icon
                        ),
                        tint = PrimaryPurple,
                        contentDescription = "Navigate to ${route.name}",
                        modifier = Modifier.size(32.dp)
                    )
                }
            )
        }

    }
}