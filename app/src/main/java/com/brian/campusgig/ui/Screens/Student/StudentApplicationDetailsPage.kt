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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
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
import com.brian.campusgig.ui.components.studentBottomNavigation
import com.brian.campusgig.ui.theme.PrimaryPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentApplicationDetailsPage(
    applicationId: String,
    navHostController: NavHostController
) {
    val viewModel: StudentApplicationViewModel = viewModel()
    val application by viewModel.selectedApplication.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadApplication(applicationId)
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
                )
            )
        },
        bottomBar = { studentBottomNavigation(navHostController) },
        containerColor = Color(0xFFF8F9FA) // Subtle background to make cards pop
    ) { padding ->

        application?.let { app ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Gig & Status Header Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = app.gigTitle,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            lineHeight = 28.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Status Badge
                        val (statusBgColor, statusTextColor) = when (app.status.lowercase()) {
                            "accepted" -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)
                            "rejected" -> Color(0xFFFFEBEE) to Color(0xFFC62828)
                            else -> PrimaryPurple.copy(alpha = 0.1f) to PrimaryPurple
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Status: ",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Medium
                            )
                            Box(
                                modifier = Modifier
                                    .background(statusBgColor, RoundedCornerShape(20.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = app.status.replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase() else it.toString()
                                    },
                                    color = statusTextColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // 2. Contact Information Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        SectionTitle("Contact Information")
                        Spacer(modifier = Modifier.height(12.dp))

                        InfoRow(
                            icon = painterResource(R.drawable.person),
                            label = "Full Name",
                            value = app.studentName
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        InfoRow(
                            icon = painterResource(R.drawable.email),
                            label = "Email",
                            value = app.studentEmail
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        InfoRow(
                            icon = painterResource(R.drawable.phone),
                            label = "Phone Number",
                            value = app.phoneNumber.ifBlank { "Not Provided" }
                        )
                    }
                }

                // 3. Academic Information Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        SectionTitle("Academic Details")
                        Spacer(modifier = Modifier.height(12.dp))

                        InfoRow(
                            icon = painterResource(R.drawable.category),
                            label = "Course",
                            value = app.course
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        InfoRow(
                            icon = painterResource(R.drawable.duration),
                            label = "Year of Study",
                            value = app.yearOfStudy
                        )
                    }
                }

                // 4. Application Content Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        SectionTitle("Application Content")
                        Spacer(modifier = Modifier.height(16.dp))

                        // Skills
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryPurple.copy(alpha = 0.08f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.skills),
                                    contentDescription = "Skills",
                                    modifier = Modifier.size(18.dp),
                                    tint = PrimaryPurple
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Skills",
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = app.skillsDescription,
                                    fontSize = 15.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.SemiBold,
                                    lineHeight = 20.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Cover Letter
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryPurple.copy(alpha = 0.08f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.description),
                                    contentDescription = "Cover Letter",
                                    modifier = Modifier.size(18.dp),
                                    tint = PrimaryPurple
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Cover Letter",
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = app.coverLetter,
                                    fontSize = 15.sp,
                                    color = Color.DarkGray,
                                    lineHeight = 22.sp
                                )
                            }
                        }
                    }
                }

                // Extra bottom padding so the last card doesn't get hidden behind the bottom navigation
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = PrimaryPurple
    )
}

@Composable
private fun InfoRow(
    icon: Painter,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(PrimaryPurple.copy(alpha = 0.08f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = label,
                modifier = Modifier.size(18.dp),
                tint = PrimaryPurple
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                fontSize = 15.sp,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}