package com.brian.campusgig.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.brian.campusgig.R
import com.brian.campusgig.ui.theme.PrimaryPurple

@Composable
fun ProfileImage(
    imageUrl: String,
    onChangePhoto: () -> Unit
) {

    Box(
        modifier = Modifier.size(140.dp),
        contentAlignment = Alignment.BottomEnd
    ) {

        if (imageUrl.isNotEmpty()) {

            AsyncImage(
                model = imageUrl,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
            )

        } else {

            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(PrimaryPurple.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    painter = painterResource(R.drawable.person),
                    contentDescription = null,
                    tint = PrimaryPurple,
                    modifier = Modifier.size(72.dp)
                )

            }

        }

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(PrimaryPurple)
                .clickable {
                    onChangePhoto()
                },
            contentAlignment = Alignment.Center
        ) {

            Icon(
                painter = painterResource(R.drawable.camera),
                contentDescription = "Change Photo",
                tint = Color.White
            )

        }

    }

}