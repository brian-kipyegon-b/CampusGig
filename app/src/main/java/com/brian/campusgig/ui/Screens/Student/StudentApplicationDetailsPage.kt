package com.brian.campusgig.ui.Screens.Student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.brian.campusgig.R
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

                title = {
                    Text("Application Details")
                },

                navigationIcon = {

                    IconButton(
                        onClick = {
                            navHostController.popBackStack()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.back_arrow),
                            contentDescription = "icon for back arrow"
                        )

                    }

                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryPurple,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )

            )

        }

    ) { padding ->

        application?.let { app ->

            Column(

                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),

                verticalArrangement = Arrangement.spacedBy(18.dp)

            ) {

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {

                        Text(
                            app.gigTitle,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Status")

                        AssistChip(

                            onClick = {},

                            label = {
                                Text(app.status)
                            }

                        )

                    }

                }

                DetailItem(
                    "Full Name",
                    app.studentName
                )

                DetailItem(
                    "Email",
                    app.studentEmail
                )

                DetailItem(
                    "Phone Number",
                    app.phoneNumber.ifBlank { "Not Provided" }
                )

                DetailItem(
                    "Course",
                    app.course
                )

                DetailItem(
                    "Year of Study",
                    app.yearOfStudy
                )

                DetailItem(
                    "Skills",
                    app.skillsDescription
                )

                DetailItem(
                    "Cover Letter",
                    app.coverLetter
                )

            }

        }

    }

}

@Composable
private fun DetailItem(
    title: String,
    value: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                title,
                color = Color.Gray,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(value)

        }

    }

}