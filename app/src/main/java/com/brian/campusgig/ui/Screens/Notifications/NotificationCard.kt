package com.brian.campusgig.ui.Screens.Notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.brian.campusgig.data.models.Notification
import com.brian.campusgig.ui.theme.PrimaryPurple

@Composable
fun NotificationCard(
    notification: Notification,
    onClick: () -> Unit
) {
    val isUnread = !notification.read

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isUnread) 6.dp else 2.dp
        ),
        colors = CardDefaults.cardColors(
            // Subtle purple background for unread notifications
            containerColor = if (isUnread) PrimaryPurple.copy(alpha = 0.04f) else Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top, // Keeps the dot at the top
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Left: Icon Container
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(PrimaryPurple.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.notifications),
                    contentDescription = "Notification",
                    modifier = Modifier.size(22.dp),
                    tint = PrimaryPurple
                )
            }

            // Middle: Content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = notification.title,
                    fontWeight = if (isUnread) FontWeight.Bold else FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = Color.Black,
                    maxLines = 1
                )

                Text(
                    text = notification.message,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    lineHeight = 18.sp,
                    maxLines = 2 // Prevents extremely long messages from breaking the layout
                )
            }

            // Right: Unread Indicator Dot
            if (isUnread) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(PrimaryPurple)
                        .align(Alignment.Top)
                )
            }
        }
    }
}