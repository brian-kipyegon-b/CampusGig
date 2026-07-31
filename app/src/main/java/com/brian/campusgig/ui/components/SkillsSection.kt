package com.brian.campusgig.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.brian.campusgig.R
import com.brian.campusgig.data.models.UserProfile
import com.brian.campusgig.ui.theme.PrimaryPurple

@Composable
fun SkillsSection(
    profile: UserProfile?
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        androidx.compose.foundation.layout.Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "Skills",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            if (profile?.skills.isNullOrEmpty()) {

                Text(
                    text = "No skills added yet.",
                    modifier = Modifier.padding(top = 12.dp)
                )

            } else {

                FlowRow(
                    modifier = Modifier.padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    profile!!.skills.forEach { skill ->

                        AssistChip(
                            onClick = { },
                            label = {
                                Text(skill)
                            },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.skills),
                                    contentDescription = "person icon"
                                )
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = PrimaryPurple.copy(alpha = 0.1f)
                            )
                        )

                    }

                }

            }

        }

    }

}