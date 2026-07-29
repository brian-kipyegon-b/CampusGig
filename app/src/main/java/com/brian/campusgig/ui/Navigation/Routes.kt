package com.brian.campusgig.ui.Navigation

import kotlinx.serialization.Serializable

@Serializable
object Landing


@Serializable
object Contact

@Serializable
object Profile

@Serializable
object Login

@Serializable
object Register

@Serializable
object Splash

@Serializable
object StudentDashboard

@Serializable
object EmployerDashboard

@Serializable
object StudentMessages

@Serializable
object StudentProfile

@Serializable
object MyApplications

@Serializable
object CreateGig

@Serializable
object EmployerGigs

@Serializable
object EmployerProfile

@Serializable
data class GigDetails(
    val gigId: String
)
@Serializable
data class EditGig(
    val gigId: String
)

@Serializable
data class StudentGigDetails(val gigId: String)

@Serializable
object BrowseGigs

@Serializable
data class ViewGig(
    val gigId: String
)
@Serializable
data class ApplyGig(
    val gigId: String
)

@Serializable
object StudentApplications

@Serializable
data class ApplicationDetails(
    val applicationId: String
)

@Serializable
data class EmployerApplicants(
    val gigId: String
)

@Serializable
data class EmployerApplicationDetails(
    val applicationId: String
)

@Serializable
object EmployerApplications

@Serializable
object Notifications
