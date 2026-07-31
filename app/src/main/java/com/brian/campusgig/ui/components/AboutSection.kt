package com.brian.campusgig.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.brian.campusgig.R
import com.brian.campusgig.data.models.UserProfile

@Composable
fun AboutSection(
    profile: UserProfile?
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    painter = painterResource(R.drawable.person),
                    contentDescription = "person icon",
                    modifier = Modifier.size(24.dp)
                )

                Text(
                    text = " About Me",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

            }

            Text(
                text = if (profile?.bio?.isNotBlank() == true)
                    profile.bio
                else
                    "No bio added yet.",
                modifier = Modifier.padding(top = 12.dp),
                style = MaterialTheme.typography.bodyMedium
            )

        }

    }

}