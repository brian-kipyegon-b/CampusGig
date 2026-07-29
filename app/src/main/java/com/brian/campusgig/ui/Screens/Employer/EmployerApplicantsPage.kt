package com.brian.campusgig.ui.Screens.Employer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.brian.campusgig.ui.Navigation.EmployerApplicationDetails
import com.brian.campusgig.ui.components.EmployerApplicantCard
import com.brian.campusgig.ui.theme.PrimaryPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployerApplicantsPage(
    gigId: String,
    navHostController: NavHostController
) {

    val viewModel: EmployerApplicationViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

    val applicants by viewModel.applications.collectAsState()

    val loading by viewModel.loading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadApplicants(gigId)
    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Text(
                        "Applicants",
                        color = Color.White
                    )

                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryPurple
                )

            )

        }

    ) { padding ->

        if (loading) {

            CircularProgressIndicator()

        } else {

            LazyColumn(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),

                verticalArrangement = Arrangement.spacedBy(12.dp)

            ) {

                items(applicants) { application ->

                    EmployerApplicantCard(
                        application = application,
                        onClick = {
                            navHostController.navigate(
                                EmployerApplicationDetails(application.applicationId)
                            )

                        }
                    )

                }

            }

        }

    }

}