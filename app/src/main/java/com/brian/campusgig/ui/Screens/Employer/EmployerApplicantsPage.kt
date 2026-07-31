package com.brian.campusgig.ui.Screens.Employer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.brian.campusgig.R
import com.brian.campusgig.ui.Navigation.EmployerApplicationDetails
import com.brian.campusgig.ui.Navigation.Notifications
import com.brian.campusgig.ui.Screens.Authentication.AuthState
import com.brian.campusgig.ui.Screens.Authentication.AuthViewModel
import com.brian.campusgig.ui.Screens.Notifications.NotificationViewModel
import com.brian.campusgig.ui.components.EmployerApplicantCard
import com.brian.campusgig.ui.components.employerBottomNavigation
import com.brian.campusgig.ui.theme.PrimaryPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployerApplicantsPage(
    gigId: String,
    navHostController: NavHostController
) {

    val viewModel: EmployerApplicationViewModel = viewModel()
    val notificationViewModel: NotificationViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.authState.collectAsState()

    val currentUser = (authState as? AuthState.Success)?.user
    val applicants by viewModel.applications.collectAsState()
    val unreadCount by notificationViewModel.unreadCount.collectAsState()

    val loading by viewModel.loading.collectAsState()

    LaunchedEffect(gigId, currentUser?.uid) {
        viewModel.loadApplicants(gigId)
        currentUser?.uid?.let {

            notificationViewModel.loadUnreadCount(it)

        }

    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(painterResource(com.brian.campusgig.R.drawable.logo), "Logo", modifier = Modifier.size(32.dp), tint = PrimaryPurple)
                        Spacer(Modifier.width(8.dp))
                        Text(buildAnnotatedString {
                            append("Campus")
                            withStyle(SpanStyle(color = PrimaryPurple, fontWeight = FontWeight.Bold)) { append("Gig") }
                        }, fontSize = 20.sp)
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { navHostController.navigate(Notifications) }) {
                            Icon(painterResource(R.drawable.notifications), "Notifications")
                        }
                        if (unreadCount > 0) {
                            Badge(
                                modifier = Modifier.align(Alignment.TopEnd).padding(4.dp),
                                containerColor = Color.Red,
                                contentColor = Color.White
                            ) {
                                Text(unreadCount.toString(), fontSize = 10.sp)
                            }
                        }
                    }
                }
            )
        },
        bottomBar = { employerBottomNavigation(navHostController) }
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