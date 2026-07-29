package com.brian.campusgig.ui.Screens.Employer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import com.brian.campusgig.data.models.Gig
import com.brian.campusgig.ui.Navigation.CreateGig
import com.brian.campusgig.ui.Navigation.EmployerGigs
import com.brian.campusgig.ui.Navigation.Notifications
import com.brian.campusgig.ui.Screens.Authentication.AuthState
import com.brian.campusgig.ui.Screens.Authentication.AuthViewModel
import com.brian.campusgig.ui.Screens.Notifications.NotificationViewModel
import com.brian.campusgig.ui.components.employerBottomNavigation
import com.brian.campusgig.ui.theme.PrimaryPurple
import com.brian.campusgig.ui.utils.toDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployerDashboard(navHostController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()
    val gigViewModel: GigViewModel = viewModel()
    val notificationViewModel: NotificationViewModel = viewModel()
    var gigToDelete by remember { mutableStateOf<Gig?>(null) }

    val unreadCount by notificationViewModel.unreadCount.collectAsState()
    val authState by authViewModel.authState.collectAsState()
    val gigs by gigViewModel.gigs.collectAsState()
    val isLoading by gigViewModel.loading.collectAsState()
    val username = (authState as? AuthState.Success)?.user?.username ?: "Employer"
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        authViewModel.checkAuthStatus()
    }

    LaunchedEffect(authState) {
        val user = (authState as? AuthState.Success)?.user
        user?.let {
            gigViewModel.loadEmployerGigs()
            notificationViewModel.loadUnreadCount(it.uid)
        }

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
        bottomBar = { employerBottomNavigation(navHostController) }
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
                        withStyle(SpanStyle(color = PrimaryPurple, fontWeight = FontWeight.Bold)) { append(username) }
                    }, fontSize = 24.sp)
                    Text("Manage your gigs and applications.", color = Color.Gray, fontSize = 14.sp)
                }
            }

            item {
                Button(
                    onClick = { navHostController.navigate(CreateGig) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                ) {
                    Icon(painterResource(R.drawable.employee_insurance), null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.size(8.dp))
                    Text("Create New Gig", fontWeight = FontWeight.SemiBold)
                }
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    DashboardCard(Modifier.weight(1f), "Applications", gigs.count { it.status == "Open" }.toString())
                    DashboardCard(Modifier.weight(1f), "Saved Gigs", gigs.sumOf { it.applicants }.toString())
                }
            }

            item {
                DashboardCard(Modifier.fillMaxWidth(), "Closed Gigs", gigs.count { it.status == "Closed" }.toString())
            }

            item {
                Text("Recent Gigs", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
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
                            onCreateGig = {
                                navHostController.navigate(CreateGig)
                            }
                        )

                    }

                }

                else -> {

                    items(gigs.take(5)) { gig ->

                        Card(
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(
                                            PrimaryPurple.copy(0.1f),
                                            RoundedCornerShape(12.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {

                                    Icon(
                                        painter = painterResource(R.drawable.category),
                                        contentDescription = null,
                                        tint = PrimaryPurple,
                                        modifier = Modifier.size(24.dp)
                                    )

                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {

                                    Text(
                                        gig.title,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Text(
                                        "${gig.category} • ${gig.location}",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )

                                    Text(
                                        "Posted on ${gig.postedAt.toDateTime()}",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )

                                }

                                Column(
                                    horizontalAlignment = Alignment.End,
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {

                                    Column(horizontalAlignment = Alignment.End) {

                                        Text(
                                            gig.applicants.toString(),
                                            fontWeight = FontWeight.Bold,
                                            color = PrimaryPurple,
                                            fontSize = 18.sp
                                        )

                                        Text(
                                            "Applications",
                                            fontSize = 10.sp,
                                            color = Color.Gray
                                        )

                                    }

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (gig.status == "Open")
                                            Color(0xFFE8F5E9)
                                        else
                                            Color(0xFFFFEBEE)
                                    ) {

                                        Text(
                                            text = gig.status,
                                            modifier = Modifier.padding(
                                                horizontal = 8.dp,
                                                vertical = 4.dp
                                            ),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = if (gig.status == "Open")
                                                Color(0xFF2E7D32)
                                            else
                                                Color.Red
                                        )

                                    }

                                }

                                GigMenu(
                                    gig = gig,
                                    navHostController = navHostController,
                                    onDeleteClick = {
                                        gigToDelete = gig
                                    }
                                )

                            }

                        }

                    }

                    if (gigs.size > 5) {

                        item {

                            OutlinedButton(
                                onClick = {
                                    navHostController.navigate(EmployerGigs)
                                },
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
    gigToDelete?.let { selectedGig ->
        DeleteGigDialog(
            gig = selectedGig,
            onConfirm = {
                gigViewModel.deleteGig(selectedGig.gigId)
                gigToDelete = null
            },
            onDismiss = {gigToDelete = null}
        )
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
