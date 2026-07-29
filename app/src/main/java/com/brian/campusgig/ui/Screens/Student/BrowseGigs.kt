package com.brian.campusgig.ui.Screens.Student

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import com.brian.campusgig.ui.Navigation.ViewGig
import com.brian.campusgig.ui.Screens.Authentication.AuthState
import com.brian.campusgig.ui.Screens.Authentication.AuthViewModel
import com.brian.campusgig.ui.Screens.Notifications.NotificationViewModel
import com.brian.campusgig.ui.components.StudentGigCard
import com.brian.campusgig.ui.components.employerBottomNavigation
import com.brian.campusgig.ui.components.studentBottomNavigation
import com.brian.campusgig.ui.theme.PrimaryPurple
import kotlin.text.contains

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseGigsPage(navHostController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()
    val studentGigViewModel: StudentGigViewModel = viewModel()
    val applicationViewModel: StudentApplicationViewModel = viewModel()
    val notificationViewModel: NotificationViewModel = viewModel()
    val gigs by studentGigViewModel.gigs.collectAsState()
    val applications by applicationViewModel.applications.collectAsState()
    val isLoading by studentGigViewModel.loading.collectAsState()
    val authState by authViewModel.authState.collectAsState()
    val unreadCount by notificationViewModel.unreadCount.collectAsState()
    val user =
        (authState as? AuthState.Success)?.user

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    var isSearchActive by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        authViewModel.checkAuthStatus()
        studentGigViewModel.loadLatestGigs()
    }
    LaunchedEffect(user) {
        user?.let {
            applicationViewModel.loadMyApplications(it.uid)

            notificationViewModel.loadUnreadCount(it.uid)
        }
    }
    val appliedGigIds = remember(applications) {
        applications.map { it.gigId }.toSet()
    }

    val filteredGigs = gigs.filter { gig ->
        val matchesSearch = searchQuery.isBlank() ||
                gig.title.contains(searchQuery, ignoreCase = true) ||
                gig.category.contains(searchQuery, ignoreCase = true) ||
                gig.location.contains(searchQuery, ignoreCase = true)
        val matchesFilter = when (selectedFilter) {
            "Tutoring" ->
                gig.category.equals("Tutoring", true)
            "Programming" ->
                gig.category.equals("Programming", true)
            "Writing" ->
                gig.category.equals("Writing", true)
            "Other" ->
                gig.category.equals("Other", true)
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

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp)
                        ) {

                            item {
                                FilterChip(
                                    selected = selectedFilter == "All",
                                    onClick = { selectedFilter = "All" },
                                    label = {
                                        Text("All")
                                    }
                                )
                            }

                            item {
                                FilterChip(
                                    selected = selectedFilter == "Tutoring",
                                    onClick = { selectedFilter = "Tutoring" },
                                    label = {
                                        Text("Tutoring")
                                    }
                                )
                            }

                            item {
                                FilterChip(
                                    selected = selectedFilter == "Programming",
                                    onClick = { selectedFilter = "Programming" },
                                    label = {
                                        Text("Programming")
                                    }
                                )
                            }

                            item {
                                FilterChip(
                                    selected = selectedFilter == "Writing",
                                    onClick = { selectedFilter = "Writing" },
                                    label = {
                                        Text("Writing")
                                    }
                                )
                            }

                            item {
                                FilterChip(
                                    selected = selectedFilter == "Other",
                                    onClick = { selectedFilter = "Other" },
                                    label = {
                                        Text("Other")
                                    }
                                )
                            }

                        }
                    }
                }
            }
        },
        bottomBar = { studentBottomNavigation(navHostController) }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
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

                        items(filteredGigs) { gig ->
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
                    }

                }
            }
        }
    }

}