package com.brian.campusgig.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brian.campusgig.R
import com.brian.campusgig.data.models.Gig
import com.brian.campusgig.ui.theme.PrimaryPurple

@Composable
fun StudentGigCard(
    gig: Gig,
    hasApplied: Boolean,
    onCardClick: () -> Unit,
    onApplyClick: () -> Unit,
    onSaveClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Section with Category & Status
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PrimaryPurple.copy(0.08f))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category Badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = PrimaryPurple.copy(0.15f),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.category),
                            contentDescription = null,
                            tint = PrimaryPurple,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = gig.category,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = PrimaryPurple,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(Modifier.width(8.dp))

                // Status Badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (gig.status == "Open") Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                ) {
                    Text(
                        text = gig.status,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (gig.status == "Open") Color(0xFF2E7D32) else Color.Red
                    )
                }
            }

            // Main Content
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Title
                Text(
                    text = gig.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // Employer Name
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.employee_insurance),
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = gig.employerName.ifBlank { "Unknown Employer" },
                        fontSize = 13.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Info Grid (Location, Pay, Duration)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Location
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.location),
                            contentDescription = null,
                            tint = PrimaryPurple,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = gig.location,
                            fontSize = 13.sp,
                            color = Color.DarkGray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Pay
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.pay),
                            contentDescription = null,
                            tint = PrimaryPurple,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "KES ${gig.pay.toInt()}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryPurple
                        )
                    }
                }

                // Duration & Deadline
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.duration),
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = gig.duration,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.calender),
                            contentDescription = null,
                            tint = if (isUrgent(gig.deadline)) Color.Red else Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "Deadline: ${gig.deadline}",
                            fontSize = 12.sp,
                            color = if (isUrgent(gig.deadline)) Color.Red else Color.Gray,
                            fontWeight = if (isUrgent(gig.deadline)) FontWeight.Medium else FontWeight.Normal
                        )
                    }
                }

                // Skills (Horizontal Scroll)
                if (gig.skills.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        gig.skills.take(5).forEach { skill ->
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = PrimaryPurple.copy(0.1f),
                                modifier = Modifier.height(24.dp)
                            ) {
                                Text(
                                    text = skill,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontSize = 11.sp,
                                    color = PrimaryPurple,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        if (gig.skills.size > 5) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color.LightGray.copy(0.3f),
                                modifier = Modifier.height(24.dp)
                            ) {
                                Text(
                                    text = "+${gig.skills.size - 5}",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontSize = 11.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // Applicants Count
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.applications_icon),
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "${gig.applicants} ${if (gig.applicants == 1) "applicant" else "applicants"}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Save Button (Icon Button)
                    IconButton(
                        onClick = onSaveClick,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.LightGray.copy(0.2f))
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.employee_insurance), // Replace with bookmark icon
                            contentDescription = "Save",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Apply Button
                    Button(
                        onClick = {
                            if (gig.status == "Open") onApplyClick()
                        },
                        modifier = Modifier
                            .height(44.dp)
                            .weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when {
                                hasApplied -> Color(0xFF4CAF50)
                                gig.status == "Open" -> PrimaryPurple
                                else -> Color.Gray
                            },
                            disabledContainerColor = Color.LightGray
                        ),
                        enabled = gig.status == "Open" && !hasApplied
                    ) {
                        Text(
                            text = when {
                                hasApplied -> "Applied"
                                gig.status == "Open" -> "Apply Now"
                                else -> "Closed"
                            },
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

// Helper function to check if deadline is urgent (within 3 days)
private fun isUrgent(deadline: String): Boolean {
    return try {
        val deadlineMillis = android.icu.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())
            .parse(deadline)?.time ?: return false
        val now = System.currentTimeMillis()
        val daysUntil = (deadlineMillis - now) / (1000 * 60 * 60 * 24)
        daysUntil <= 3 && daysUntil >= 0
    } catch (e: Exception) {
        false
    }
}