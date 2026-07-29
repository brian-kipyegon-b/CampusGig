package com.brian.campusgig.ui.Screens.Student

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.brian.campusgig.R
import com.brian.campusgig.data.models.Application
import com.brian.campusgig.ui.Screens.Authentication.AuthViewModel
import com.brian.campusgig.ui.theme.PrimaryPurple
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationPage(
    gigId: String,
    navHostController: NavHostController
) {
    val authViewModel: AuthViewModel = viewModel()
    val studentGigViewModel: StudentGigViewModel = viewModel()
    val applicationViewModel: StudentApplicationViewModel = viewModel()

    val authState by authViewModel.authState.collectAsState()
    val user = (authState as? com.brian.campusgig.ui.Screens.Authentication.AuthState.Success)?.user
    val gig by studentGigViewModel.selectedGig.collectAsState()
    val isLoading by applicationViewModel.loading.collectAsState()
    val applicationSuccess by applicationViewModel.applicationSuccess.collectAsState()

    var fullName by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("") }
    var yearOfStudy by remember { mutableStateOf("") }
    var skillsDescription by remember { mutableStateOf("") }
    var applicationMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        authViewModel.checkAuthStatus()
        studentGigViewModel.loadGig(gigId)
    }

    LaunchedEffect(applicationSuccess) {
        if (applicationSuccess) {
            applicationViewModel.resetSuccess()
            navHostController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Apply for Gig", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(painter = painterResource(R.drawable.back_arrow), "Back", tint = PrimaryPurple)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Create Account",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Text(
                        text = "Join CampusGig and discover amazing opportunities.",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(end = 32.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .background(
                            color = PrimaryPurple.copy(alpha = 0.1f),
                            shape = CircleShape
                        )
                        .padding(16.dp)
                ) {
                    AsyncImage(
                        model = R.drawable.application_image,
                        contentDescription = "Register illustration",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            // 1. Gig Context Card (So the user knows what they are applying for)
            gig?.let { currentGig ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = PrimaryPurple.copy(0.08f)),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Applying for:", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                        Spacer(Modifier.height(4.dp))
                        Text(currentGig.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = PrimaryPurple)
                        Spacer(Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(painterResource(R.drawable.pay), null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("KES ${currentGig.pay.toInt()}", fontSize = 14.sp, color = Color.DarkGray)
                            Spacer(Modifier.width(16.dp))
                            Icon(painterResource(R.drawable.location), null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(currentGig.location, fontSize = 14.sp, color = Color.DarkGray)
                        }
                    }
                }
            }

            // 2. Grouped Input Card
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Application Details", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PrimaryPurple)

                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full Name") },
                        placeholder = { Text("e.g. John Doe") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(painter = painterResource(R.drawable.fullname), null, modifier = Modifier.size(24.dp), tint = PrimaryPurple) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple,
                            focusedLabelColor = PrimaryPurple,
                            cursorColor = PrimaryPurple
                        )
                    )

                    OutlinedTextField(
                        value = course,
                        onValueChange = { course = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Course of Study") },
                        leadingIcon = { Icon(painter = painterResource(R.drawable.logo), null, modifier = Modifier.size(24.dp), tint = PrimaryPurple) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple,
                            focusedLabelColor = PrimaryPurple,
                            cursorColor = PrimaryPurple
                        )
                    )

                    OutlinedTextField(
                        value = yearOfStudy,
                        onValueChange = { yearOfStudy = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Year of Study") },
                        placeholder = { Text("e.g., Year 2, Year 3") },
                        leadingIcon = { Icon(painter = painterResource(R.drawable.duration), null, modifier = Modifier.size(24.dp), tint = PrimaryPurple) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple,
                            focusedLabelColor = PrimaryPurple,
                            cursorColor = PrimaryPurple
                        )
                    )

                    OutlinedTextField(
                        value = skillsDescription,
                        onValueChange = { skillsDescription = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Skills & Experience") },
                        placeholder = { Text("Briefly describe your relevant skills...") },
                        leadingIcon = { Icon(painter = painterResource(R.drawable.description), null, tint = PrimaryPurple) },
                        minLines = 4,
                        maxLines = 6,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple,
                            focusedLabelColor = PrimaryPurple,
                            cursorColor = PrimaryPurple
                        )
                    )

                    OutlinedTextField(
                        value = applicationMessage,
                        onValueChange = { applicationMessage = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Cover Letter / Message") },
                        placeholder = { Text("Tell the employer why you're a great fit...") },
                        leadingIcon = { Icon(painter = painterResource(R.drawable.email), null, tint = PrimaryPurple) },
                        minLines = 4,
                        maxLines = 6,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple,
                            focusedLabelColor = PrimaryPurple,
                            cursorColor = PrimaryPurple
                        )
                    )
                }
            }

            // 3. Submit Button
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                onClick = {
                    val currentGig = gig ?: return@Button
                    val currentUser = user ?: return@Button

                    // Basic validation
                    if (course.isBlank() || yearOfStudy.isBlank() || skillsDescription.isBlank() || applicationMessage.isBlank()) {
                        // You can trigger a Snackbar or Toast here
                        return@Button
                    }

                    val application = Application(
                        applicationId = UUID.randomUUID().toString(),
                        gigId = currentGig.gigId,
                        gigTitle = currentGig.title,
                        employerId = currentGig.employerId,
                        studentId = currentUser.uid,
                        studentName = fullName,
                        studentEmail = currentUser.email,
                        phoneNumber = currentUser.phoneNumber,
                        course = course,
                        yearOfStudy = yearOfStudy,
                        skillsDescription = skillsDescription,
                        coverLetter = applicationMessage,
                        status = "Pending"
                    )
                    applicationViewModel.applyForGig(application)
                },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                if (isLoading) {

                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )

                } else {

                    Text(
                        text = "Submit Application",
                        fontWeight = FontWeight.SemiBold
                    )

                }
            }

            Spacer(Modifier.height(24.dp)) // Bottom padding for scroll
        }
    }
}