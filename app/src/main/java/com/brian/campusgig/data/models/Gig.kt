package com.brian.campusgig.data.models

data class Gig(

    val gigId: String = "",

    val employerId: String = "",

    val employerName: String = "",

    val title: String = "",

    val description: String = "",

    val category: String = "",

    val location: String = "",

    val pay: Double = 0.0,

    val deadline: String = "",

    val duration: String = "",

    val skills: List<String> = emptyList(),

    val applicants: Int = 0,

    val status: String = "Open",

    val postedAt: Long = System.currentTimeMillis()
)
