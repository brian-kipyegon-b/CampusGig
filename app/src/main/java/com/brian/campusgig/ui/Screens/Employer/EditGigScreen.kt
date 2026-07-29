package com.brian.campusgig.ui.Screens.Employer

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.brian.campusgig.ui.components.employerBottomNavigation
import com.brian.campusgig.ui.theme.DarkPurple
import com.brian.campusgig.ui.theme.PrimaryPurple
import com.google.firebase.auth.FirebaseAuth
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGigScreen(navHostController: NavHostController, gigId: String, gigViewModel: GigViewModel = viewModel()){
    val user = FirebaseAuth.getInstance().currentUser
    val loading by gigViewModel.loading.collectAsState()
    val success by gigViewModel.success.collectAsState()
    val message by gigViewModel.message.collectAsState()
    val gig by gigViewModel.selectedGig.collectAsState()
    LaunchedEffect(Unit) { gigViewModel.getGigById(gigId) }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var pay by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf("") }
    var skills by remember { mutableStateOf("") }

    val categories = listOf("Programming", "Tutoring", "Graphic Design", "Delivery", "Photography", "Writing", "Cleaning", "Other")
    val durations = listOf("1 Hour", "2 Hours", "Half Day", "Full Day", "Weekend", "1 Week", "1 Month")
    val statuses = listOf("Open", "Closed")

    var selectedCategory by remember { mutableStateOf(categories[0]) }
    var selectedDuration by remember { mutableStateOf(durations[0]) }
    var selectedStatus by remember { mutableStateOf(statuses[0]) }

    var categoryExpanded by remember { mutableStateOf(false) }
    var durationExpanded by remember { mutableStateOf(false) }
    var statusExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(gig) {
        gig?.let {
            title = it.title
            description = it.description
            location = it.location
            pay = it.pay.toString()
            deadline = it.deadline
            skills = it.skills.joinToString(", ")

            selectedCategory = it.category
            selectedDuration = it.duration
            selectedStatus = it.status
        }
    }

    LaunchedEffect(success) {
        if (success) {
            Toast.makeText(
                navHostController.context,
                "Gig updated successfully",
                Toast.LENGTH_SHORT
            ).show()
            navHostController.popBackStack()
            gigViewModel.resetState()
        }

    }

    if (gig == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.logo), // Replace with your logo name
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
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(R.drawable.notifications),
                            contentDescription = "Notifications",
                            tint = PrimaryPurple
                        )
                    }
                }
            )
        },
        bottomBar = { employerBottomNavigation(navHostController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Header Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkPurple.copy(alpha = 0.08f))
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Text(
                    text = "Edit Gig",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryPurple
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Update your gig details below",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Basic Information Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    .padding(20.dp)
            ) {
                Text(
                    text = "Basic Information",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryPurple
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Gig Title") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.title),
                            contentDescription = null,
                            tint = PrimaryPurple,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DarkPurple,
                        focusedLabelColor = DarkPurple,
                        cursorColor = PrimaryPurple
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.description),
                            contentDescription = null,
                            tint = PrimaryPurple,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    minLines = 4,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DarkPurple,
                        focusedLabelColor = DarkPurple,
                        cursorColor = PrimaryPurple
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.location),
                            contentDescription = "location logo",
                            tint = PrimaryPurple,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DarkPurple,
                        focusedLabelColor = DarkPurple,
                        cursorColor = PrimaryPurple
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Gig Details Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    .padding(20.dp)
            ) {
                Text(
                    text = "Gig Details",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryPurple
                )
                Spacer(modifier = Modifier.height(16.dp))

                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.category),
                                contentDescription = null,
                                tint = PrimaryPurple,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple,
                            focusedLabelColor = PrimaryPurple
                        ),
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        categories.forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    selectedCategory = it
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = pay,
                    onValueChange = { pay = it },
                    label = { Text("Pay (KES)") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.pay),
                            contentDescription = null,
                            tint = PrimaryPurple,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryPurple,
                        focusedLabelColor = PrimaryPurple,
                        cursorColor = PrimaryPurple
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                ExposedDropdownMenuBox(
                    expanded = durationExpanded,
                    onExpandedChange = { durationExpanded = !durationExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedDuration,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Duration") },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.duration),
                                contentDescription = null,
                                tint = PrimaryPurple,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = durationExpanded) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple,
                            focusedLabelColor = PrimaryPurple
                        ),
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = durationExpanded,
                        onDismissRequest = { durationExpanded = false }
                    ) {
                        durations.forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    selectedDuration = it
                                    durationExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = deadline,
                    onValueChange = { deadline = it },
                    label = { Text("Application Deadline") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.deadline),
                            contentDescription = null,
                            tint = PrimaryPurple,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryPurple,
                        focusedLabelColor = PrimaryPurple,
                        cursorColor = PrimaryPurple
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Requirements Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    .padding(20.dp)
            ) {
                Text(
                    text = "Requirements",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryPurple
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = skills,
                    onValueChange = { skills = it },
                    label = { Text("Required Skills") },
                    placeholder = { Text("Kotlin, Firebase, Communication") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.skills),
                            contentDescription = null,
                            tint = PrimaryPurple,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryPurple,
                        focusedLabelColor = PrimaryPurple,
                        cursorColor = PrimaryPurple
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                ExposedDropdownMenuBox(
                    expanded = statusExpanded,
                    onExpandedChange = { statusExpanded = !statusExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedStatus,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Status") },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.status),
                                contentDescription = null,
                                tint = PrimaryPurple,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple,
                            focusedLabelColor = PrimaryPurple
                        ),
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = statusExpanded,
                        onDismissRequest = { statusExpanded = false }
                    ) {
                        statuses.forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    selectedStatus = it
                                    statusExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {

                    if (
                        title.isBlank() ||
                        description.isBlank() ||
                        location.isBlank() ||
                        pay.isBlank() ||
                        deadline.isBlank() ||
                        skills.isBlank()
                    ) {

                        Toast.makeText(
                            navHostController.context,
                            "Please fill all fields",
                            Toast.LENGTH_SHORT
                        ).show()

                        return@Button
                    }

                    val currentUser = FirebaseAuth.getInstance().currentUser

                    if (currentUser == null) {

                        Toast.makeText(
                            navHostController.context,
                            "Please login first",
                            Toast.LENGTH_SHORT
                        ).show()

                        return@Button
                    }

                    val currentGig = gig

                    if (currentGig == null) {

                        Toast.makeText(
                            navHostController.context,
                            "Gig not loaded yet",
                            Toast.LENGTH_SHORT
                        ).show()

                        return@Button
                    }

                    val updatedGig = currentGig.copy(

                        title = title,

                        description = description,

                        category = selectedCategory,

                        location = location,

                        pay = pay.toDoubleOrNull() ?: 0.0,

                        deadline = deadline,

                        duration = selectedDuration,

                        skills = skills
                            .split(",")
                            .map { it.trim() }
                            .filter { it.isNotEmpty() },

                        status = selectedStatus

                    )

                    gigViewModel.updateGig(updatedGig)

                },

                enabled = !loading,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp),

                shape = RoundedCornerShape(14.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryPurple,
                    contentColor = Color.White
                )

            ) {

                if (loading) {

                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Updating...",
                        fontWeight = FontWeight.Bold
                    )

                } else {

                    Text(
                        text = "UPDATE GIG",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                }

            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}