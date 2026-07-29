package com.brian.campusgig.ui.Screens.Student

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
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
import com.brian.campusgig.ui.Navigation.ApplyGig
import com.brian.campusgig.ui.Navigation.Notifications
import com.brian.campusgig.ui.Screens.Authentication.AuthState
import com.brian.campusgig.ui.Screens.Authentication.AuthViewModel
import com.brian.campusgig.ui.Screens.Notifications.NotificationViewModel
import com.brian.campusgig.ui.components.studentBottomNavigation
import com.brian.campusgig.ui.theme.PrimaryPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentGigDetailsPage(
    gigId: String,
    navHostController: NavHostController
) {
    val viewModel: StudentGigViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.authState.collectAsState()
    val notificationViewModel: NotificationViewModel = viewModel()
    val gigState = viewModel.selectedGig.collectAsState()
    val loadingState = viewModel.loading.collectAsState()
    val unreadCount by notificationViewModel.unreadCount.collectAsState()

    val gig = gigState.value
    val loading = loadingState.value

    LaunchedEffect(Unit) {
        authViewModel.checkAuthStatus()
        viewModel.loadGig(gigId)
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
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(
                            painter = painterResource(R.drawable.back_arrow),
                            contentDescription = "Navigate back",
                            tint = Color.White
                        )
                    }
                },
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
        bottomBar = { studentBottomNavigation(navHostController) },
        // Subtle background to make white cards pop
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->

        if (loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = PrimaryPurple,
                    strokeWidth = 4.dp
                )
            }
        } else {
            gig?.let { currentGig ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Header Section with Gradient Background
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        PrimaryPurple,
                                        PrimaryPurple.copy(alpha = 0.7f),
                                        Color(0xFFF8F9FA) // Fades into the scaffold background
                                    )
                                )
                            )
                            .padding(horizontal = 20.dp, vertical = 24.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Category Chip
                            Box(
                                modifier = Modifier
                                    .background(
                                        Color.White,
                                        RoundedCornerShape(20.dp)
                                    )
                                    .padding(horizontal = 16.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    currentGig.category,
                                    color = PrimaryPurple,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                )
                            }

                            // Title
                            Text(
                                currentGig.title,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                lineHeight = 34.sp
                            )
                        }
                    }

                    // Main Content
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // Details Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                DetailRow(
                                    icon = R.drawable.user,
                                    label = "Employer",
                                    value = currentGig.employerName
                                )

                                DetailRow(
                                    icon = R.drawable.pay,
                                    label = "Payment",
                                    value = "KES ${currentGig.pay}",
                                    highlightValue = true
                                )

                                DetailRow(
                                    icon = R.drawable.location,
                                    label = "Location",
                                    value = currentGig.location
                                )

                                DetailRow(
                                    icon = R.drawable.duration,
                                    label = "Duration",
                                    value = currentGig.duration
                                )

                                // Divider
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(PrimaryPurple.copy(alpha = 0.1f))
                                )

                                // Deadline and Applicants Row
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            "Deadline",
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )
                                        Text(
                                            currentGig.deadline,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 14.sp,
                                            color = Color.Black
                                        )
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            "Applicants",
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )
                                        Text(
                                            "${currentGig.applicants}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = PrimaryPurple
                                        )
                                    }
                                }
                            }
                        }

                        // Description Section
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                "Description",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.Black
                            )
                            Text(
                                currentGig.description,
                                fontSize = 15.sp,
                                color = Color.DarkGray,
                                lineHeight = 22.sp
                            )
                        }

                        // Skills Section
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(
                                "Required Skills",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.Black
                            )

                            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                items(currentGig.skills) { skill ->
                                    ElevatedAssistChip(
                                        onClick = {},
                                        label = {
                                            Text(
                                                skill,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 13.sp
                                            )
                                        },
                                        colors = AssistChipDefaults.elevatedAssistChipColors(
                                            containerColor = PrimaryPurple.copy(alpha = 0.1f),
                                            labelColor = PrimaryPurple
                                        ),
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Apply Button
                        Button(
                            onClick = {
                                navHostController.navigate(ApplyGig(currentGig.gigId))
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .shadow(
                                    elevation = 8.dp,
                                    shape = RoundedCornerShape(14.dp),
                                    ambientColor = PrimaryPurple.copy(alpha = 0.3f),
                                    spotColor = PrimaryPurple.copy(alpha = 0.3f)
                                ),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                "Apply Now",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        // Extra bottom padding so the button doesn't overlap the bottom navigation
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(
    icon: Int,
    label: String,
    value: String,
    highlightValue: Boolean = false
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(PrimaryPurple.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = label,
                modifier = Modifier.size(20.dp),
                tint = PrimaryPurple
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Added weight(1f) to prevent long text from pushing off-screen
        Column(modifier = Modifier.weight(1f)) {
            Text(
                label,
                fontSize = 12.sp,
                color = Color.Gray
            )
            Text(
                value,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = if (highlightValue) PrimaryPurple else Color.Black,
                maxLines = 2 // Prevents extremely long names from breaking layout
            )
        }
    }
}