package com.brian.campusgig.ui.Screens.Employer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.brian.campusgig.R
import com.brian.campusgig.ui.Navigation.Notifications
import com.brian.campusgig.ui.components.EmployerApplicantCard
import com.brian.campusgig.ui.components.employerBottomNavigation
import com.brian.campusgig.ui.theme.PrimaryPurple
import com.google.firebase.auth.FirebaseAuth
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployerApplicationsScreen(
    navHostController: NavHostController,
    viewModel: EmployerApplicationViewModel = viewModel(),
    onApplicationClick: (String) -> Unit = {}
) {

    val applications by viewModel.applications.collectAsState()
    val loading by viewModel.loading.collectAsState()

    val employerId =
        FirebaseAuth.getInstance().currentUser?.uid ?: ""

    LaunchedEffect(Unit) {
        viewModel.loadEmployerApplications(employerId)
    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            painter = painterResource(R.drawable.logo),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = PrimaryPurple
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(

                            buildAnnotatedString {

                                append("Campus")

                                withStyle(
                                    SpanStyle(
                                        color = PrimaryPurple,
                                        fontWeight = FontWeight.Bold
                                    )
                                ) {
                                    append("Gig")
                                }

                            },

                            fontSize = 20.sp

                        )

                    }

                },

                actions = {

                    IconButton(
                        onClick = {
                            navHostController.navigate(Notifications)
                        }
                    ) {

                        Icon(
                            painter = painterResource(R.drawable.notifications),
                            contentDescription = "Notifications"
                        )

                    }

                },

                colors = TopAppBarDefaults.topAppBarColors()

            )

        },

        bottomBar = {

            employerBottomNavigation(navHostController)

        }

    ) { padding ->

        when {

            loading -> {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

            }

            applications.isEmpty() -> {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "No applications yet.",
                        style = MaterialTheme.typography.bodyLarge
                    )

                }

            }

            else -> {

                LazyColumn(

                    modifier = Modifier
                        .fillMaxSize(),

                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = padding.calculateTopPadding() + 16.dp,
                        bottom = padding.calculateBottomPadding() + 16.dp
                    ),

                    verticalArrangement = Arrangement.spacedBy(12.dp)

                ) {

                    items(applications) { application ->

                        EmployerApplicantCard(

                            application = application,

                            onClick = {

                                onApplicationClick(
                                    application.applicationId
                                )

                            }

                        )

                    }

                }

            }

        }

    }

}