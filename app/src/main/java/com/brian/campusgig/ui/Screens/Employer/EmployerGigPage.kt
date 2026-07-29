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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.brian.campusgig.R
import com.brian.campusgig.data.models.Gig
import com.brian.campusgig.ui.Navigation.CreateGig
import com.brian.campusgig.ui.Navigation.EditGig
import com.brian.campusgig.ui.Navigation.EmployerApplicants
import com.brian.campusgig.ui.Navigation.GigDetails
import com.brian.campusgig.ui.Navigation.Notifications
import com.brian.campusgig.ui.Screens.Authentication.AuthState
import com.brian.campusgig.ui.Screens.Authentication.AuthViewModel
import com.brian.campusgig.ui.Screens.Notifications.NotificationViewModel
import com.brian.campusgig.ui.components.employerBottomNavigation
import com.brian.campusgig.ui.theme.PrimaryPurple
import com.brian.campusgig.ui.utils.toDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployerGigPage(navHostController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()
    val gigViewModel: GigViewModel = viewModel()
    val notificationViewModel: NotificationViewModel = viewModel()

    val authState by authViewModel.authState.collectAsState()
    val gigs by gigViewModel.gigs.collectAsState()
    val isLoading by gigViewModel.loading.collectAsState()
    val unreadCount by notificationViewModel.unreadCount.collectAsState()

    var gigToDelete by remember { mutableStateOf<Gig?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    var isSearchActive by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        authViewModel.checkAuthStatus()
        gigViewModel.loadEmployerGigs()
    }
    LaunchedEffect(authState) {
        val user = (authState as? AuthState.Success)?.user
        user?.let {
            notificationViewModel.loadUnreadCount(it.uid)
        }
    }

    val filteredGigs = gigs.filter { gig ->
        val matchesSearch = searchQuery.isBlank() ||
                gig.title.contains(searchQuery, ignoreCase = true) ||
                gig.category.contains(searchQuery, ignoreCase = true) ||
                gig.location.contains(searchQuery, ignoreCase = true)
        val matchesFilter = when (selectedFilter) {
            "Open" -> gig.status == "Open"
            "Closed" -> gig.status == "Closed"
            else -> true
        }
        matchesSearch && matchesFilter
    }

    Scaffold(
        topBar = {
            // FIX: Wrapped in a Column so they stack vertically instead of overlapping
            Column {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(painterResource(R.drawable.logo), "Logo", modifier = Modifier.size(32.dp), tint = PrimaryPurple)
                            Spacer(Modifier.width(8.dp))
                            Text(buildAnnotatedString {
                                append("Campus")
                                withStyle(SpanStyle(color = PrimaryPurple, fontWeight = FontWeight.Bold)) { append("Gig") }
                            }, fontSize = 20.sp)
                        }
                    },
                    actions = {
                        IconButton(onClick = { isSearchActive = !isSearchActive }) {
                            Icon(
                                painter = painterResource(if (isSearchActive) R.drawable.delete else R.drawable.search),
                                contentDescription = if (isSearchActive) "Close Search" else "Search",
                                tint = PrimaryPurple
                            )
                        }
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

                // Search and Filters Section
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 2.dp // Adds a clean, professional separation line
                ) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                        if (isSearchActive) {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                placeholder = { Text("Search gigs...", fontSize = 14.sp) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                trailingIcon = {
                                    if (searchQuery.isNotEmpty()) {
                                        IconButton(onClick = { searchQuery = "" }) {
                                            Icon(painterResource(R.drawable.delete), "Clear", tint = Color.Gray, modifier = Modifier.size(20.dp))
                                        }
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryPurple,
                                    unfocusedBorderColor = Color.LightGray
                                )
                            )
                            Spacer(Modifier.height(8.dp))
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                            FilterChip(
                                selected = selectedFilter == "All",
                                onClick = { selectedFilter = "All" },
                                label = { Text("All (${gigs.size})") }
                            )
                            FilterChip(
                                selected = selectedFilter == "Open",
                                onClick = { selectedFilter = "Open" },
                                label = { Text("Open (${gigs.count { it.status == "Open" }})") }
                            )
                            FilterChip(
                                selected = selectedFilter == "Closed",
                                onClick = { selectedFilter = "Closed" },
                                label = { Text("Closed (${gigs.count { it.status == "Closed" }})") }
                            )
                        }
                    }
                }
            }
        },
        bottomBar = { employerBottomNavigation(navHostController) }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when {
                    isLoading -> item {
                        Box(Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = PrimaryPurple)
                        }
                    }
                    gigs.isEmpty() -> item { EmptyGigState { navHostController.navigate(CreateGig) } }
                    else -> items(filteredGigs) { gig -> GigCard(gig, navHostController) { gigToDelete = gig } }
                }
            }
        }
    }

    gigToDelete?.let { gig ->
        DeleteGigDialog(gig,
            onConfirm = { gigViewModel.deleteGig(gig.gigId); gigToDelete = null },
            onDismiss = { gigToDelete = null }
        )
    }
}

@Composable
fun GigCard(gig: Gig, navHostController: NavHostController, onDeleteClick: () -> Unit) {
    Card(shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(2.dp)) {
        Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(48.dp).background(PrimaryPurple.copy(0.1f), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                Icon(painterResource(R.drawable.category), null, tint = PrimaryPurple, modifier = Modifier.size(24.dp))
            }
            Spacer(Modifier.width(16.dp))

            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(gig.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("${gig.category} • ${gig.location}", fontSize = 12.sp, color = Color.Gray)
                Text("Posted on ${gig.postedAt.toDateTime()}", fontSize = 12.sp, color = Color.Gray)
            }

            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(gig.applicants.toString(), fontWeight = FontWeight.Bold, color = PrimaryPurple, fontSize = 18.sp)
                    Text("Applications", fontSize = 10.sp, color = Color.Gray)
                }
                Surface(shape = RoundedCornerShape(8.dp), color = if (gig.status == "Open") Color(0xFFE8F5E9) else Color(0xFFFFEBEE)) {
                    Text(gig.status, Modifier.padding(horizontal = 8.dp, vertical = 4.dp), fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = if (gig.status == "Open") Color(0xFF2E7D32) else Color.Red)
                }
                Button(
                    onClick = {
                        navHostController.navigate(
                            EmployerApplicants(gig.gigId)
                        )
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryPurple
                    )
                ) {
                    Text("View Applicants")
                }
            }
            }
            GigMenu(gig, navHostController, onDeleteClick)
        }
    }


@Composable
fun GigMenu(gig: Gig, navHostController: NavHostController, onDeleteClick: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(painterResource(R.drawable.more), "More", tint = Color.Gray, modifier = Modifier.size(20.dp))
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text("View Gig") }, leadingIcon = { Icon(painterResource(R.drawable.visibility), null) }, onClick = { expanded = false; navHostController.navigate(GigDetails(gig.gigId)) })
            DropdownMenuItem(text = { Text("Edit Gig") }, leadingIcon = { Icon(painterResource(R.drawable.edit), null) }, onClick = { expanded = false; navHostController.navigate(EditGig(gig.gigId)) })
            HorizontalDivider()
            DropdownMenuItem(text = { Text("Delete Gig", color = Color.Red) }, leadingIcon = { Icon(painterResource(R.drawable.delete), null, tint = Color.Red) }, onClick = { expanded = false; onDeleteClick() })
        }
    }
}

@Composable
fun DeleteGigDialog(gig: Gig, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(onDismissRequest = onDismiss, title = { Text("Delete Gig") }, text = { Text("Are you sure you want to delete \"${gig.title}\"?") },
        confirmButton = { Button(onClick = onConfirm, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) { Text("Delete") } },
        dismissButton = { OutlinedButton(onClick = onDismiss) { Text("Cancel") } })
}

@Composable
fun EmptyGigState(onCreateGig: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = PrimaryPurple.copy(alpha = 0.05f))) {
        Column(Modifier.fillMaxWidth().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Icon(painterResource(R.drawable.employee_insurance), null, modifier = Modifier.size(70.dp), tint = PrimaryPurple)
            Text("No gigs yet", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("You haven't posted any gigs yet.\nCreate your first opportunity and start receiving applications.", textAlign = TextAlign.Center, color = Color.Gray)
            Button(onClick = onCreateGig, colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)) {
                Icon(painterResource(R.drawable.employee_insurance), null)
                Spacer(Modifier.width(8.dp))
                Text("Create First Gig")
            }
        }
    }
}