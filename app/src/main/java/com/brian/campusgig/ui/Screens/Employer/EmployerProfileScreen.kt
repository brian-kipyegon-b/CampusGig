package com.brian.campusgig.ui.Screens.Employer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.brian.campusgig.R
import com.brian.campusgig.ui.components.employerBottomNavigation
import com.brian.campusgig.ui.theme.PrimaryPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployerProfilePage(navHostController: NavHostController){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            painter = painterResource(R.drawable.logo), // Replace with your logo name
                            contentDescription = "CampusGig Logo",
                            modifier = Modifier.size(38.dp),
                            tint = PrimaryPurple
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            buildAnnotatedString {

                                withStyle(
                                    SpanStyle(
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold
                                    )
                                ) {
                                    append("Campus")
                                }

                                withStyle(
                                    SpanStyle(
                                        color = PrimaryPurple,
                                        fontWeight = FontWeight.Bold
                                    )
                                ) {
                                    append("Gig")
                                }

                            },
                            fontSize = 22.sp
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(R.drawable.notifications),
                            contentDescription = "Notifications",
                            tint = PrimaryPurple
                        )
                    }
                }
            )
        },
        bottomBar = { employerBottomNavigation(navHostController) }
    ) { paddingValues ->

        Column(
            modifier = Modifier.padding(paddingValues).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Text("Welcome to the Employer profile page")
        }

    }
}