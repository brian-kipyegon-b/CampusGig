package com.brian.campusgig.ui.Screens.Splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
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
import com.brian.campusgig.ui.Navigation.Landing // Adjust this to your actual starting destination
import com.brian.campusgig.ui.Navigation.Splash
import com.brian.campusgig.ui.Screens.Authentication.AuthViewModel
import com.brian.campusgig.ui.theme.Background
import com.brian.campusgig.ui.theme.PrimaryPurple
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navHostController: NavHostController,
    authViewModel: AuthViewModel = viewModel()
) {
    val authState by authViewModel.authState.collectAsState()
    // Animation for a subtle zoom-in effect on the logo
    val scale = remember { Animatable(0.8f) }
    // State to hold the changing "Loading..." text
    val loadingText = remember { mutableStateOf("Loading...") }

    LaunchedEffect(key1 = true) {
        // Animate scale
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(1500)
        )

        // Wait for the remaining duration before navigating
        delay(2000)

        // Navigate to the main screen (e.g., Landing, Login, or Home)
        navHostController.navigate(Landing) {
            // Clear the splash screen from the back stack so user can't go back to it
            popUpTo(Splash) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Logo with animation
            Icon(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "CampusGig Logo",
                tint = PrimaryPurple,
                modifier = Modifier
                    .size(100.dp)
                    .scale(scale.value)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // App Name
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Black, fontSize = 36.sp, fontWeight = FontWeight.Bold)) {
                        append("Campus")
                    }
                    withStyle(style = SpanStyle(color = PrimaryPurple, fontSize = 36.sp, fontWeight = FontWeight.Bold)) {
                        append("Gig")
                    }
                }
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Loading Indicator
            CircularProgressIndicator(
                color = PrimaryPurple,
                modifier = Modifier.size(40.dp),
                strokeWidth = 4.dp
            )

            // Animated Loading Text
            Text(
                text = loadingText.value,
                fontSize = 16.sp,
                color = PrimaryPurple, // Keeping the purple theme
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.sp // Adds a little breathing room to the text
            )
        }
    }
}