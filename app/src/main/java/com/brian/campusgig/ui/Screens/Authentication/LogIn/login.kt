package com.brian.campusgig.ui.Screens.Authentication.LogIn

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.brian.campusgig.R
import com.brian.campusgig.ui.Navigation.Landing
import com.brian.campusgig.ui.Navigation.Register
import com.brian.campusgig.ui.Navigation.StudentDashboard
import com.brian.campusgig.ui.Navigation.EmployerDashboard
import com.brian.campusgig.ui.Screens.Authentication.AuthState
import com.brian.campusgig.ui.Screens.Authentication.AuthViewModel
import com.brian.campusgig.ui.theme.Background
import com.brian.campusgig.ui.theme.PrimaryPurple

@Composable
fun LogInPage(
    modifier: Modifier,
    navHostController: NavHostController,
    authViewModel: AuthViewModel = viewModel()
) {
    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    var passwordVisible by remember { mutableStateOf(false) }

    val authState by authViewModel.authState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.Success -> {
                val user = state.user
                
                val destination = if (user.role == "Student") StudentDashboard else EmployerDashboard

                navHostController.navigate(destination) {
                    popUpTo(navHostController.graph.startDestinationId) { inclusive = true }
                    launchSingleTop = true
                }
            }
            is AuthState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                authViewModel.resetState()
            }

            else -> {}
        }
    }


    Column(
        modifier = modifier
            .background(color = Background)
            .fillMaxSize()
            .verticalScroll(scrollState)
            .imePadding()
    ) {
        // Top Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            // Back Arrow
            Icon(
                painter = painterResource(R.drawable.left_arrow),
                contentDescription = "Back",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .clickable { navHostController.navigate(Landing) }
                    .padding(8.dp)
            )

            // Header Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo with graduation caps
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = null,
                        tint = PrimaryPurple,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = Color.Black,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("Campus")
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = PrimaryPurple,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("Gig")
                            }
                        }
                    )

                }

                Spacer(Modifier.height(24.dp))

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
                            model = R.drawable.register,
                            contentDescription = "Register illustration",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

        }

        // Form Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Email Field
                OutlinedTextField(
                    value = emailInput,
                    onValueChange = { emailInput = it },
                    label = { Text("Email/Username") },
                    placeholder = { Text("Enter your email or username", color = Color.Gray) },
                    shape = RoundedCornerShape(12.dp),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.email),
                            contentDescription = "Email icon",
                            tint = PrimaryPurple
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryPurple,
                        focusedLeadingIconColor = PrimaryPurple
                    )
                )

                Spacer(Modifier.height(16.dp))

                // Password Field
                OutlinedTextField(
                    value = passwordInput,
                    onValueChange = { passwordInput = it },
                    label = { Text("Password") },
                    placeholder = { Text("Enter your password", color = Color.Gray) },
                    shape = RoundedCornerShape(12.dp),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.lock),
                            contentDescription = "Password icon",
                            tint = PrimaryPurple
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                painter = painterResource(
                                    if (passwordVisible) R.drawable.visibility_off else R.drawable.visibility
                                ),
                                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                tint = Color.Gray
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryPurple,
                        focusedLeadingIconColor = PrimaryPurple
                    )
                )

                Spacer(Modifier.height(24.dp))

                // Login Button
                Button(
                    onClick = {
                        authViewModel.loginUser(
                            input = emailInput,
                            password = passwordInput
                        )
                    },
                    enabled = emailInput.isNotBlank() && passwordInput.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryPurple,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    if (authState is AuthState.Loading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "LogIn",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(Modifier.width(8.dp))

                        Icon(
                            painterResource(R.drawable.forward_arrow),
                            contentDescription = "forward arrow to login"
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                // OR Divider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Divider(
                        modifier = Modifier.weight(1f),
                        color = Color.LightGray,
                        thickness = 1.dp
                    )
                    Text(
                        text = "OR",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                    Divider(
                        modifier = Modifier.weight(1f),
                        color = Color.LightGray,
                        thickness = 1.dp
                    )
                }

                Spacer(Modifier.height(20.dp))

                // Google Sign Up Button
                OutlinedButton(
                    onClick = { /* TODO: Handle Google sign up */ },
                    border = BorderStroke(1.5.dp, Color.LightGray),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.google_logo),
                        contentDescription = "Google logo",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = "Sign up with Google",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Login Link
        Text(
            text = buildAnnotatedString {
                append("Don't have an account? ")
                withStyle(
                    style = SpanStyle(
                        color = PrimaryPurple,
                        fontWeight = FontWeight.SemiBold
                    )
                ) {
                    append("Sign Up?")
                }
            },
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navHostController.navigate(Register) }
                .padding(16.dp)
        )

        Spacer(Modifier.height(16.dp))
    }
}
