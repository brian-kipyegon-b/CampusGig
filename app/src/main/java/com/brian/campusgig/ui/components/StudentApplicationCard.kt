package com.brian.campusgig.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brian.campusgig.data.models.Application
import com.brian.campusgig.ui.theme.PrimaryPurple
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun StudentApplicationCard(
    application: Application,
    onClick: () -> Unit
) {

    val statusColor = when (application.status) {
        "Accepted" -> Color(0xFF2E7D32)
        "Rejected" -> Color.Red
        else -> Color(0xFFFF9800)
    }

    val formattedDate = SimpleDateFormat(
        "dd MMM yyyy",
        Locale.getDefault()
    ).format(Date(application.appliedAt))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = application.gigTitle,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = PrimaryPurple
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text("Course: ${application.course}")

            Text("Year: ${application.yearOfStudy}")

            Text("Applied: $formattedDate")

            Spacer(modifier = Modifier.height(12.dp))

            AssistChip(
                onClick = {},
                label = {
                    Text(application.status)
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = statusColor.copy(alpha = .15f),
                    labelColor = statusColor
                )
            )

        }

    }

}