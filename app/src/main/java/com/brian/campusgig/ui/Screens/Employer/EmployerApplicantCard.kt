package com.brian.campusgig.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brian.campusgig.R
import com.brian.campusgig.data.models.Application
import com.brian.campusgig.ui.theme.PrimaryPurple

@Composable
fun EmployerApplicantCard(
    application: Application,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left Side: Avatar + Student Info
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Avatar Icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(PrimaryPurple.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.user),
                        contentDescription = "Student Avatar",
                        modifier = Modifier.size(24.dp),
                        tint = PrimaryPurple
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                // Student Details
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = application.studentName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        maxLines = 1
                    )

                    Text(
                        text = "${application.course} • Year ${application.yearOfStudy}",
                        fontSize = 13.sp,
                        color = Color.Gray,
                        maxLines = 1
                    )
                }
            }

            // Right Side: Status Badge
            StatusBadge(status = application.status)
        }
    }
}

@Composable
private fun StatusBadge(status: String) {
    // Determine colors based on status
    val (backgroundColor, textColor) = when (status.lowercase()) {
        "accepted" -> Color(0xFFE8F5E9) to Color(0xFF2E7D32) // Light Green bg, Dark Green text
        "rejected" -> Color(0xFFFFEBEE) to Color(0xFFC62828) // Light Red bg, Dark Red text
        else -> PrimaryPurple.copy(alpha = 0.1f) to PrimaryPurple // Light Purple bg, Purple text
    }

    Box(
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(20.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = status.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
            color = textColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}