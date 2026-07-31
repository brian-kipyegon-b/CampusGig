package com.brian.campusgig.data.models

data class UserProfile(
    val uid: String = "",
    val fullName: String = "",
    val bio: String = "",
    val profileImage: String = "",
    val skills: List<String> = emptyList(),
    val profileCompleted: Boolean = false
)
