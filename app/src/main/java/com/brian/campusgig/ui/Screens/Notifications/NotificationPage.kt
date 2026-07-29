package com.brian.campusgig.ui.Screens.Notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import com.brian.campusgig.ui.Screens.Authentication.AuthState
import com.brian.campusgig.ui.Screens.Authentication.AuthViewModel
import com.brian.campusgig.ui.theme.PrimaryPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationPage(
    navHostController: NavHostController
) {

    val authViewModel: AuthViewModel = viewModel()
    val notificationViewModel: NotificationViewModel = viewModel()

    val authState by authViewModel.authState.collectAsState()
    val notifications by notificationViewModel.notifications.collectAsState()
    val loading by notificationViewModel.loading.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.checkAuthStatus()
    }

    LaunchedEffect(authState) {
        val currentUser = (authState as? AuthState.Success)?.user
        currentUser?.let { user ->
            notificationViewModel.loadNotifications(user.uid)
            notificationViewModel.loadUnreadCount(user.uid)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Logo Container
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

                        // App Name
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
                )
            )
        },
        // Subtle background to make white cards pop
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->

        if (loading) {
            // Professional Loading State
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = PrimaryPurple,
                    modifier = Modifier.size(48.dp),
                    strokeWidth = 4.dp
                )
            }
        } else {
            if (notifications.isEmpty()) {
                // Empty State
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .background(Color(0xFFF8F9FA)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Empty Icon
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(PrimaryPurple.copy(alpha = 0.05f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.notifications),
                                contentDescription = "No notifications",
                                modifier = Modifier.size(50.dp),
                                tint = PrimaryPurple.copy(alpha = 0.4f)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            "No notifications yet",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            "You're all caught up! Check back later.",
                            fontSize = 15.sp,
                            color = Color.Gray
                        )
                    }
                }
            } else {
                // Notifications List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .background(Color(0xFFF8F9FA)),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val currentUser = (authState as? AuthState.Success)?.user

                    items(notifications) { notification ->
                        NotificationCard(
                            notification = notification,
                            onClick = {
                                currentUser?.let { user ->
                                    notificationViewModel.markAsRead(
                                        notificationId = notification.notificationId,
                                        receiverId = user.uid
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}