package com.brian.campusgig.ui.Screens.Student

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.brian.campusgig.R
import com.brian.campusgig.ui.Navigation.ApplyGig
import com.brian.campusgig.ui.Navigation.BrowseGigs
import com.brian.campusgig.ui.Navigation.CreateGig
import com.brian.campusgig.ui.Navigation.EmployerGigs
import com.brian.campusgig.ui.Navigation.Notifications
import com.brian.campusgig.ui.Navigation.ViewGig
import com.brian.campusgig.ui.Screens.Authentication.AuthState
import com.brian.campusgig.ui.Screens.Authentication.AuthViewModel
import com.brian.campusgig.ui.Screens.Notifications.NotificationViewModel
import com.brian.campusgig.ui.components.StudentGigCard
import com.brian.campusgig.ui.components.studentBottomNavigation
import com.brian.campusgig.ui.theme.PrimaryPurple
import com.brian.campusgig.ui.utils.toDateTime
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDashboardPage(navHostController: NavHostController){

    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.authState.collectAsState()
    val username = (authState as? AuthState.Success)?.user?.username ?: "Student"

    val studentGigViewModel: StudentGigViewModel = viewModel()
    val applicationViewModel: StudentApplicationViewModel = viewModel()
    val notificationViewModel: NotificationViewModel = viewModel()
    val gigs by studentGigViewModel.gigs.collectAsState()
    val appliedGigIds by applicationViewModel.appliedGigIds.collectAsState()
    val unreadCount by notificationViewModel.unreadCount.collectAsState()

    val isLoading by studentGigViewModel.loading.collectAsState()

    LaunchedEffect(authState) {
        val user = (authState as? AuthState.Success)?.user
        user?.let {
            applicationViewModel.loadAppliedGigIds(it.uid)
            notificationViewModel.loadUnreadCount(it.uid)
        }

    }
    LaunchedEffect(Unit) {
        authViewModel.checkAuthStatus()
        studentGigViewModel.loadLatestGigs()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.logo),
                                contentDescription = "CampusGig Logo",
                                modifier = Modifier.size(28.dp),
                                tint = PrimaryPurple
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            buildAnnotatedString {
                                withStyle(
                                    SpanStyle(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                ) {
                                    append("Campus")
                                }
                                withStyle(
                                    SpanStyle(
                                        color = Color.White.copy(alpha = 0.8f),
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryPurple
                ),

                actions = {
                    Box {

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

                        if (unreadCount > 0) {

                            Badge(
                                modifier = Modifier.align(Alignment.TopEnd),
                                containerColor = Color.Red,
                                contentColor = Color.White
                            ) {
                                Text(unreadCount.toString())
                            }

                        }

                    }
                }
            )
        },
        bottomBar = { studentBottomNavigation(navHostController) }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column {
                    Text(buildAnnotatedString {
                        append("Welcome Back, ")
                        withStyle(
                            SpanStyle(
                                color = PrimaryPurple,
                                fontWeight = FontWeight.Bold
                            )
                        ) { append(username) }
                    }, fontSize = 24.sp)
                    Text("Manage your gigs and applications.", color = Color.Gray, fontSize = 14.sp)
                }
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    DashboardCard(
                        modifier = Modifier.weight(1f),
                        title = "Available",
                        value = gigs.size.toString()
                    )

                    DashboardCard(
                        modifier = Modifier.weight(1f),
                        title = "Applied",
                        value = appliedGigIds.size.toString()
                    )
                }
            }

            item {
                Text(
                    "Latest Gigs",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            when {

                isLoading -> {

                    item {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 70.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            CircularProgressIndicator(
                                color = PrimaryPurple
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Loading your dashboard...",
                                color = Color.Gray
                            )

                        }

                    }

                }

                gigs.isEmpty() -> {

                    item {

                        EmptyGigState(
                            onRefresh = {
                                studentGigViewModel.loadLatestGigs()
                            }
                        )

                    }

                }

                else -> {

                    items(gigs.take(1)) { gig ->
                        StudentGigCard(
                            gig = gig,
                            hasApplied = gig.gigId in appliedGigIds,
                            onCardClick = {
                                navHostController.navigate(
                                    ViewGig(gig.gigId))
                            },
                            onApplyClick = {
                                navHostController.navigate(
                                    ApplyGig(gig.gigId)
                                )
                            }

                        )

                    }
                    if (gigs.size > 1) {
                        item {
                            OutlinedButton(
                                onClick = {navHostController.navigate(BrowseGigs)},
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            ) {

                                Text(
                                    "View All Gigs",
                                    color = PrimaryPurple,
                                    fontWeight = FontWeight.Bold
                                )

                            }

                        }

                    }

                }

            }
        }
    }
}

@Composable
fun DashboardCard(modifier: Modifier = Modifier, title: String, value: String) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PrimaryPurple.copy(0.08f)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(value, style = MaterialTheme.typography.headlineMedium, color = PrimaryPurple, fontWeight = FontWeight.Bold)
            Text(title, style = MaterialTheme.typography.bodyMedium, color = Color.DarkGray)
        }
    }
}

@Composable
fun EmptyGigState(
    onRefresh: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = PrimaryPurple.copy(alpha = 0.05f)
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 40.dp, horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Icon(
                painter = painterResource(R.drawable.empty),
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                tint = PrimaryPurple
            )

            Text(
                text = "No gigs available",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "There are currently no open gigs.\nCheck back later for new opportunities.",
                textAlign = TextAlign.Center,
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )

            Button(
                onClick = onRefresh,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryPurple
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.refresh),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Refresh")
            }
        }
    }
}