package com.brian.campusgig.ui.Screens.Student

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
import com.brian.campusgig.ui.Navigation.ApplicationDetails
import com.brian.campusgig.ui.components.StudentApplicationCard
import com.brian.campusgig.ui.components.studentBottomNavigation
import com.brian.campusgig.ui.theme.PrimaryPurple
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentApplicationsPage(
    navHostController: NavHostController
) {

    val viewModel: StudentApplicationViewModel = viewModel()

    val applications by viewModel.applications.collectAsState()
    val loading by viewModel.loading.collectAsState()

    val currentUser = FirebaseAuth.getInstance().currentUser

    LaunchedEffect(Unit) {
        currentUser?.uid?.let {
            viewModel.loadMyApplications(it)
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
        bottomBar = { studentBottomNavigation(navHostController) }
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
            if (applications.isEmpty()) {
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
                        // Empty Icon (Using logo with low opacity as a placeholder)
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(PrimaryPurple.copy(alpha = 0.05f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.logo),
                                contentDescription = "No applications",
                                modifier = Modifier.size(50.dp),
                                tint = PrimaryPurple.copy(alpha = 0.4f)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            "No applications yet",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            "Start exploring and applying for gigs!",
                            fontSize = 15.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        // Call to Action Button
                        Button(
                            onClick = {
                                // Navigate to your gigs list/home screen here
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryPurple
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                "Find Gigs",
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    }
                }
            } else {
                // Applications List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .background(Color(0xFFF8F9FA)), // Light background to make cards pop
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(applications) { application ->
                        StudentApplicationCard(
                            application = application,
                            onClick = {
                                navHostController.navigate(
                                    ApplicationDetails(application.applicationId)
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}