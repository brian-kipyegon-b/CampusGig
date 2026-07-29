package com.brian.campusgig.ui.Screens.Landing

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.brian.campusgig.R
import com.brian.campusgig.data.models.FeatureItem
import com.brian.campusgig.ui.Navigation.Login
import com.brian.campusgig.ui.Navigation.Register
import com.brian.campusgig.ui.theme.Background
import com.brian.campusgig.ui.theme.LightPurple
import com.brian.campusgig.ui.theme.MainText
import com.brian.campusgig.ui.theme.PrimaryPurple

@Composable
fun LandingPage(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    val scrollState = rememberScrollState()

    val features = listOf(
        FeatureItem(
            icon = R.drawable.search,
            title = "Discover Gigs",
            description = "Find part-time jobs and gigs on CampusGig"
        ),
        FeatureItem(
            icon = R.drawable.verified,
            title = "Verified & Trusted",
            description = "All gigs are posted by verified users"
        ),
        FeatureItem(
            icon = R.drawable.flexible,
            title = "Flexible & Convenient",
            description = "Work on your own time and grow your skills"
        )
    )

    Column(
        modifier = modifier
            .background(color = Background)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Top App Bar
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 24.dp, end = 24.dp, bottom = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "CampusGig Logo",
                    tint = PrimaryPurple,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.Bold)) {
                            append("Campus")
                        }
                        withStyle(style = SpanStyle(color = PrimaryPurple, fontSize = 24.sp, fontWeight = FontWeight.Bold)) {
                            append("Gig")
                        }
                    }
                )
            }

            Button(
                onClick = {navHostController.navigate(Register)},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = PrimaryPurple
                ),
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, PrimaryPurple),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Text(
                    text = "Register",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Hero Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = PrimaryPurple, fontSize = 36.sp, fontWeight = FontWeight.ExtraBold)) {
                        append("Find Gigs.\n")
                    }
                    withStyle(style = SpanStyle(color = MainText, fontSize = 36.sp, fontWeight = FontWeight.ExtraBold)) {
                        append("Build Experience.")
                    }
                },
                textAlign = TextAlign.Center,
                lineHeight = 42.sp
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "The trusted campus community to find part-time jobs and gigs that fit your schedule.",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = MainText.copy(alpha = 0.7f),
                modifier = Modifier.fillMaxWidth(0.9f)
            )

            Spacer(Modifier.height(24.dp))

            // Hero Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                AsyncImage(
                    model = R.drawable.landing,
                    contentDescription = "Students finding gigs",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.height(24.dp))

            // Role Selection Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                RoleSelectionCard(
                    modifier = Modifier.weight(1f),
                    iconRes = R.drawable.student_reading,
                    title = "I'm a Student",
                    subtitle = "Find gigs & earn",
                    onClick = { /* TODO: Navigate to Student Flow */ }
                )
                Spacer(modifier = Modifier.width(16.dp))
                RoleSelectionCard(
                    modifier = Modifier.weight(1f),
                    iconRes = R.drawable.employee_insurance,
                    title = "I'm an Employer",
                    subtitle = "Post jobs & hire",
                    onClick = { /* TODO: Navigate to Employer Flow */ }
                )
            }
        }

        Spacer(Modifier.height(32.dp))

        // Features Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(LightPurple.copy(alpha = 0.3f))
                .padding(vertical = 32.dp, horizontal = 24.dp)
        ) {
            Text(
                text = "Why Choose CampusGig?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryPurple,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                features.forEach { feature ->
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(PrimaryPurple.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(feature.icon),
                                contentDescription = null,
                                tint = PrimaryPurple,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Text(
                            text = feature.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MainText,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = feature.description,
                            fontSize = 12.sp,
                            color = MainText.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        // Bottom CTA Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {navHostController.navigate(Register)},
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryPurple,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "Get Started",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(id = R.drawable.forward_arrow), // Consider replacing with an arrow_forward drawable
                    contentDescription = "Get Started with CampusGig",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Already have an account? Sign In",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = PrimaryPurple,
                modifier = Modifier
                    .clickable {navHostController.navigate(Login)}
                    .padding(8.dp)
            )

            Spacer(Modifier.height(16.dp)) // Bottom padding for comfortable scrolling
        }
    }
}

@Composable
private fun RoleSelectionCard(
    modifier: Modifier = Modifier,
    iconRes: Int,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(120.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = PrimaryPurple.copy(alpha = 0.2f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = PrimaryPurple,
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MainText
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = MainText.copy(alpha = 0.7f)
            )
        }
    }
}


//import android.R.attr.fontWeight
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.defaultMinSize
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Card
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalConfiguration
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.SpanStyle
//import androidx.compose.ui.text.buildAnnotatedString
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.withStyle
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import coil.compose.AsyncImage
//import com.brian.campusgig.R
//import com.brian.campusgig.data.models.FeatureItem
//import com.brian.campusgig.ui.theme.Background
//import com.brian.campusgig.ui.theme.DarkPurple
//import com.brian.campusgig.ui.theme.LightPurple
//import com.brian.campusgig.ui.theme.MainText
//import com.brian.campusgig.ui.theme.PrimaryPurple
//import com.brian.campusgig.ui.theme.RegisterButton
//
//@Composable
//fun LandingPage(
//    modifier: Modifier
//){
//    val configuration = LocalConfiguration.current
//    val screenWidthDp = configuration.screenWidthDp.dp
//    val screenHeightDp = configuration.screenHeightDp.dp
//    val scrollState = rememberScrollState()
//
//    val Features = listOf<FeatureItem>(
//        FeatureItem(
//            icon = R.drawable.search,
//            title = "Discover Gigs",
//            description = "Find part-time jobs and gigs on CampusGig"
//        ),
//        FeatureItem(
//            icon = R.drawable.verified,
//            title = "Verified & Trusted",
//            description = "All gigs are posted by verified users"
//        ),
//        FeatureItem(
//            icon = R.drawable.flexible,
//            title = "Flexible & Convenient",
//            description = "Work on your own time and grow your skills"
//        )
//    )
//
//
//    Column(
//        modifier = Modifier
//            .background(color = Background)
//            .fillMaxSize()
//            .padding(vertical = 32.dp, horizontal = 12.dp)
//            .verticalScroll(scrollState)
//    ) {
//        Row(
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(screenHeightDp * 0.12f)
//                .padding(horizontal = 16.dp)
//        ) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    painter = painterResource(R.drawable.logo),
//                    contentDescription = "CampusGig Logo",
//                    tint = DarkPurple,
//                    modifier = Modifier
//                        .size(28.dp)
//                )
//                Spacer(Modifier.width(5.dp))
//                Text(
//                    buildAnnotatedString {
//                        withStyle(style = SpanStyle(Color.Black, fontSize = 28.sp, fontWeight = FontWeight.Bold)){
//                            append("Campus")
//                        }
//                        withStyle(style = SpanStyle(DarkPurple, fontSize = 28.sp, fontWeight = FontWeight.Bold)){
//                            append("Gig")
//                        }
//                    }
//                )
//            }
//
//            Button(
//                onClick = {},
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color.Transparent,
//                    contentColor = DarkPurple
//                ),
//                shape = RoundedCornerShape(50),
//                border = BorderStroke(1.dp, Color(0xFF6A1B9A)),
//                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 0.dp),
//                modifier = Modifier.defaultMinSize(minHeight = 0.dp, minWidth = 0.dp)
//            ) {
//                Text(
//                    "Register",
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.Bold
//                )
//
//            }
//        }
//
//        Column(
//            modifier = Modifier
//                .height(screenHeightDp * 0.2f)
//                .padding(horizontal = 16.dp)
//        ) {
//            Text(
//                buildAnnotatedString {
//                    withStyle(style = SpanStyle(PrimaryPurple, fontSize = 32.sp, fontWeight = FontWeight.Bold)) {
//                        append("Find ")
//                    }
//                    withStyle(style = SpanStyle(MainText, fontSize = 32.sp, fontWeight = FontWeight.Bold)) {
//                        append("Gigs.")
//                    }
//                },
//                textAlign = TextAlign.Center
//            )
//
//            Text(
//                buildAnnotatedString {
//                    withStyle(style = SpanStyle(PrimaryPurple, fontSize = 32.sp, fontWeight = FontWeight.Bold)) {
//                        append("Build ")
//                    }
//                    withStyle(style = SpanStyle(MainText, fontSize = 32.sp, fontWeight = FontWeight.Bold)) {
//                        append("Experience.")
//                    }
//                },
//                textAlign = TextAlign.Center
//            )
//
//            //spacing
//            Spacer(Modifier.height(12.dp))
////tag line
//            Text(
//                text = "The trusted campus community to find part-time jobs and gigs that fit your schedule.",
//                fontSize = 16.sp,
//                textAlign = TextAlign.Left,
//                color = Color.Gray,
//                modifier = Modifier
//                    .width(screenWidthDp * 0.5f)
//            )
//        }
//
//        Column(
//            verticalArrangement = Arrangement.Top,
//            horizontalAlignment = Alignment.Start,
//            modifier = Modifier
//                .height(screenHeightDp * 0.5f)
//        ) {
//            Box(){
//                AsyncImage(
//                    model = R.drawable.landing,
//                    contentDescription = "Image of the landing page",
//                    modifier = Modifier.height(screenHeightDp * 0.5f)
//                )
//
//                Column() {
//                    Button(
//                        onClick = {},
//                    ) {
//                        Icon(
//                            painter = painterResource(R.drawable.student_reading),
//                            contentDescription = "Icon for an Student",
//                            modifier = Modifier
//                                .size(28.dp)
//                        )
//                        Text("I'm a Student")
//                    }
//                    Button(
//                        onClick = {},
//                    ) {
//                        Icon(
//                            painter = painterResource(R.drawable.employee_insurance),
//                            contentDescription = "Icon for an Employer",
//                            modifier = Modifier
//                                .size(28.dp)
//                        )
//                        Text("I'm a Employer")
//                    }
//                }
//            }
//        }
//
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(DarkPurple)
//        ) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.fillMaxWidth()
//            ) {Features.forEach { feature ->
//                Column(
//                    verticalArrangement = Arrangement.Center,
//                    modifier = Modifier.width(screenWidthDp * 0.3f)
//                ) {
//                    Icon(
//                        painter = painterResource(feature.icon),
//                        contentDescription = "Icon for ${feature.title}",
//                        modifier = Modifier
//                            .size(28.dp)
//                    )
//                    Text(
//                        text = feature.title,
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                    Text(
//                        text = feature.description,
//                        fontSize = 12.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//            }
//
//        }
//
//            Button(
//                onClick = {},
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = RegisterButton, // purple
//                    contentColor = Color.White
//                ),
//                shape = RoundedCornerShape(50),
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("Get Started")
//                Spacer(modifier = Modifier.width(6.dp))
//                Icon(
//                    painter = painterResource(id = R.drawable.student_reading),
//                    contentDescription = "Arrow",
//                    tint = Color.White
//                )
//            }
//
//
//            // Sign In text
//            Text(
//                text = "Already have an account? Sign In",
//                fontSize = 14.sp,
//                color = Color.White,
//                modifier = Modifier.clickable {  }
//            )
//        }
//    }
//}