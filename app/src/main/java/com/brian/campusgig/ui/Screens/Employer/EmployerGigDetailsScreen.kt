package com.brian.campusgig.ui.Screens.Employer

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.brian.campusgig.ui.Navigation.Notifications
import com.brian.campusgig.ui.Screens.Authentication.AuthState
import com.brian.campusgig.ui.Screens.Authentication.AuthViewModel
import com.brian.campusgig.ui.Screens.Notifications.NotificationViewModel
import com.brian.campusgig.ui.components.employerBottomNavigation
import com.brian.campusgig.ui.theme.PrimaryPurple
import com.brian.campusgig.ui.utils.toDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployerGigDetailsScreen(
    navHostController: NavHostController,
    gigId: String,
    gigViewModel: GigViewModel = viewModel()
) {
    val notificationViewModel: NotificationViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.authState.collectAsState()
    val gig by gigViewModel.selectedGig.collectAsState()
    val loading by gigViewModel.loading.collectAsState()
    val unreadCount by notificationViewModel.unreadCount.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.checkAuthStatus()
        gigViewModel.loadGig(gigId)
    }

    LaunchedEffect(authState) {
        val user = (authState as? AuthState.Success)?.user
        user?.let {
            notificationViewModel.loadUnreadCount(it.uid)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.logo),
                            contentDescription = "CampusGig Logo",
                            modifier = Modifier.size(32.dp),
                            tint = PrimaryPurple
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            buildAnnotatedString {
                                append("Campus")
                                withStyle(SpanStyle(color = PrimaryPurple, fontWeight = FontWeight.Bold)) { append("Gig") }
                            },
                            fontSize = 20.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(painter = painterResource(R.drawable.back_arrow), "Back", tint = PrimaryPurple, modifier = Modifier.size(24.dp))
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
    ) { padding ->
        if (loading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryPurple)
            }
        } else {
            gig?.let { currentGig ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // 1. Header Card (Title, Status, Description)
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = PrimaryPurple.copy(0.08f)),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = currentGig.title,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryPurple,
                                    modifier = Modifier.weight(1f)
                                )
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (currentGig.status == "Open") Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                                ) {
                                    Text(
                                        text = currentGig.status,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (currentGig.status == "Open") Color(0xFF2E7D32) else Color.Red
                                    )
                                }
                            }
                            Spacer(Modifier.height(12.dp))
                            Text(
                                text = currentGig.description,
                                fontSize = 15.sp,
                                color = Color.DarkGray,
                                lineHeight = 22.sp
                            )
                        }
                    }

                    // 2. Details Card
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            InfoRow(R.drawable.category, "Category", currentGig.category)
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.LightGray.copy(0.5f))

                            InfoRow(R.drawable.location, "Location", currentGig.location)
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.LightGray.copy(0.5f))

                            InfoRow(R.drawable.pay, "Pay", "KES ${currentGig.pay}")
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.LightGray.copy(0.5f))

                            InfoRow(R.drawable.duration, "Duration", currentGig.duration)
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.LightGray.copy(0.5f))

                            InfoRow(R.drawable.deadline, "Deadline", currentGig.deadline)
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.LightGray.copy(0.5f))

                            InfoRow(R.drawable.employee_insurance, "Applications", "${currentGig.applicants} Students")
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.LightGray.copy(0.5f))

                            InfoRow(R.drawable.duration, "Posted On", currentGig.postedAt.toDateTime())
                        }
                    }

                    // 3. Skills Section
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            "Required Skills",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryPurple
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            currentGig.skills.forEach { skill ->
                                AssistChip(
                                    onClick = {},
                                    label = { Text(skill, fontSize = 13.sp, fontWeight = FontWeight.Medium) },
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = PrimaryPurple.copy(0.1f),
                                        labelColor = PrimaryPurple
                                    ),
                                    border = null
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun InfoRow(icon: Any, title: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (icon) {
            is Int -> Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = PrimaryPurple,
                modifier = Modifier.size(22.dp)
            )
            is androidx.compose.ui.graphics.vector.ImageVector -> Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryPurple,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
            Text(value, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.DarkGray)
        }
    }
}