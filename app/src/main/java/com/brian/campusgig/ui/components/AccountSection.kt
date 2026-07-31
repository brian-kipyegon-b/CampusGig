package com.brian.campusgig.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.brian.campusgig.R
import com.brian.campusgig.data.models.User
import com.brian.campusgig.ui.Screens.Authentication.AuthState
import com.brian.campusgig.ui.Screens.Authentication.AuthViewModel
import com.brian.campusgig.ui.Screens.Profile.ProfileViewModel
import com.brian.campusgig.ui.theme.PrimaryPurple
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AccountSection(
    user: User?
) {
    //val authViewModel: AuthViewModel = viewModel()
    //val profileViewModel: ProfileViewModel = viewModel()
    //val authState by authViewModel.authState.collectAsState()
    //val user by authViewModel.user.collectAsState()
    //val currentUser = (authState as? AuthState.Success)?.user

    val user = FirebaseAuth.getInstance().currentUser


//    LaunchedEffect(currentUser?.uid) {
//        currentUser?.uid?.let {
//            authViewModel.loadUser(it)
//            profileViewModel.loadProfile(it)
//        }
//    }

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Account Information",
            color = PrimaryPurple,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = MaterialTheme.shapes.large
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                AccountItem(
                    icon = R.drawable.email,
                    label = "Email",
                    value = user?.email ?: "No Email"
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                AccountItem(
                    icon = R.drawable.phone,
                    label = "Phone",
                    value = user?.phoneNumber ?: "No Phone Number"
                )
            }
        }
    }
}

@Composable
private fun AccountItem(
    icon: Int,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column {
            Text(
                text = label,
                color = Color.Gray,
                fontSize = 12.sp
            )
            Text(
                text = value,
                color = PrimaryPurple,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}
