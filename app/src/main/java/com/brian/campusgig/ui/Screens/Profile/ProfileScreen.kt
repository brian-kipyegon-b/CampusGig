package com.brian.campusgig.ui.Screens.Profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.brian.campusgig.R
import com.brian.campusgig.ui.Navigation.CompleteProfile
import com.brian.campusgig.ui.Navigation.Login
import com.brian.campusgig.ui.Screens.Authentication.AuthState
import com.brian.campusgig.ui.Screens.Authentication.AuthViewModel
import com.brian.campusgig.ui.components.*
import com.brian.campusgig.ui.theme.Background
import com.brian.campusgig.ui.theme.LogoutRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController
) {
    val authViewModel: AuthViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()
    val authState by authViewModel.authState.collectAsState()
    val user by authViewModel.user.collectAsState()

    val currentUser = (authState as? AuthState.Success)?.user

    LaunchedEffect(currentUser?.uid) {
        currentUser?.uid?.let {
            authViewModel.loadUser(it)
            profileViewModel.loadProfile(it)
        }
    }

    Scaffold(
        containerColor = Background
    ) { padding ->
        LazyColumn(
            modifier = Modifier

                .fillMaxSize()
                .padding(padding)
        ) {
            // Header
            item {
                ProfileHeader(
                    user = user ?: currentUser,
                    onBackClick = { navController.popBackStack() }
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            // Account Information
            item {
                AccountSection(user ?: currentUser)
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            // App Settings
            item {
                SettingsSection(
                    onEditProfile = {/*TODO */},
                    onNotifications = { /* TODO */ },
                    onSecurity = { /* TODO */ }
                )
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Support
            item {
                SupportSection(
                    onHelpCenter = { /* TODO */ },
                    onPrivacyPolicy = { /* TODO */ }
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            // Logout Button
            item {
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Button(
                        onClick = {
                            authViewModel.logout()
                            navController.navigate(Login) {
                                popUpTo(0)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = LogoutRed),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(R.drawable.logout),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Logout",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}
