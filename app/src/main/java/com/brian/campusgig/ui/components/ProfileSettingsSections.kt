package com.brian.campusgig.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brian.campusgig.R
import com.brian.campusgig.ui.theme.PrimaryPurple

@Composable
fun SettingsSection(
    onEditProfile: () -> Unit,
    onNotifications: () -> Unit,
    onSecurity: () -> Unit
) {
    ProfileListCard(title = "App Settings") {
        SettingsItem(icon = R.drawable.edit, label = "Edit Profile", onClick = onEditProfile)
        SettingsItem(icon = R.drawable.notifications, label = "Notifications", onClick = onNotifications)
        SettingsItem(icon = R.drawable.verified, label = "Security", onClick = onSecurity)
    }
}

@Composable
fun SupportSection(
    onHelpCenter: () -> Unit,
    onPrivacyPolicy: () -> Unit
) {
    ProfileListCard(title = "Support") {
        SettingsItem(icon = R.drawable.logo, label = "Help Center", onClick = onHelpCenter)
        SettingsItem(icon = R.drawable.verified, label = "Privacy Policy", onClick = onPrivacyPolicy)
    }
}

@Composable
private fun ProfileListCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            text = title,
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
            Column(modifier = Modifier.padding(8.dp)) {
                content()
            }
        }
    }
}

@Composable
private fun SettingsItem(
    icon: Int,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = PrimaryPurple,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            color = Color.DarkGray,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        
        Icon(
            painter = painterResource(R.drawable.forward_arrow),
            contentDescription = null,
            tint = Color.LightGray,
            modifier = Modifier.size(16.dp)
        )
    }
}
