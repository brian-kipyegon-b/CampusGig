package com.brian.campusgig.ui.Screens.Employer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.brian.campusgig.ui.components.EmployerApplicantCard
import com.brian.campusgig.ui.components.StudentApplicationCard
import com.google.firebase.auth.FirebaseAuth

@Composable
fun EmployerApplicationsScreen(
    viewModel: EmployerApplicationViewModel = viewModel(),
    onApplicationClick: (String) -> Unit = {}
) {

    val applications by viewModel.applications.collectAsState()
    val loading by viewModel.loading.collectAsState()

    val employerId =
        FirebaseAuth.getInstance().currentUser?.uid ?: ""

    LaunchedEffect(Unit) {
        viewModel.loadEmployerApplications(employerId)
    }

    when {

        loading -> {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

        }

        applications.isEmpty() -> {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "No applications yet.",
                    style = MaterialTheme.typography.bodyLarge
                )

            }

        }

        else -> {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(applications) { application ->

                    EmployerApplicantCard(
                        application = application,
                        onClick = {
                            onApplicationClick(application.applicationId)
                        }
                    )

                }

            }

        }

    }

}