package com.brian.campusgig.data.models

data class Application(
    val applicationId: String = "",
    val gigId: String = "",
    val gigTitle: String = "",
    val employerId: String = "",
    val studentId: String = "",
    val studentName: String = "",
    val studentEmail: String = "",
    val phoneNumber: String = "",
    val course: String = "",
    val yearOfStudy: String = "",
    val skillsDescription: String = "",
    val coverLetter: String = "",
    val status: String = "Pending",
    val appliedAt: Long = System.currentTimeMillis()
)

