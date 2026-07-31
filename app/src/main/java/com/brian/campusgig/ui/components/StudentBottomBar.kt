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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.brian.campusgig.R
import com.brian.campusgig.data.models.studentNavItem
import com.brian.campusgig.ui.Navigation.BrowseGigs
import com.brian.campusgig.ui.Navigation.Profile
import com.brian.campusgig.ui.Navigation.StudentApplications
import com.brian.campusgig.ui.Navigation.StudentDashboard
import com.brian.campusgig.ui.Navigation.StudentMessages
import com.brian.campusgig.ui.theme.PrimaryPurple

@Composable
fun studentBottomNavigation(
    navHostController: NavHostController
){
    val routes = listOf(
        studentNavItem("Home", StudentDashboard, R.drawable.home),
        studentNavItem("Browse Gigs", BrowseGigs, R.drawable.all_gigs),
        studentNavItem("Applications", StudentApplications, R.drawable.applications),
        studentNavItem("Profile", Profile, R.drawable.user),
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
                            color = Color.Black,
                            fontSize = 16.sp
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
                        modifier = Modifier.size(28.dp)
                    )
                }
            )
        }

    }
}