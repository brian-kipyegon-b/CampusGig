package com.brian.campusgig.data.repository

import android.util.Log
import com.brian.campusgig.data.models.Application
import com.brian.campusgig.data.models.Notification
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.UUID

class StudentApplicationRepository {

    private val firestore = FirebaseFirestore.getInstance()

    private val applicationCollection =
        firestore.collection("applications")

    private val gigCollection =
        firestore.collection("gigs")

    suspend fun applyForGig(application: Application) {

        try {

            Log.d("CampusGig", "1. Saving application")

            applicationCollection
                .document(application.applicationId)
                .set(application)
                .await()

            Log.d("CampusGig", "2. Application saved")

            Log.d("CampusGig", "3. Creating employer notification for: ${application.employerId}")

            NotificationRepository().createNotification(
                Notification(
                    notificationId = UUID.randomUUID().toString(),
                    receiverId = application.employerId,
                    title = "New Applicant",
                    message = "${application.studentName} applied for ${application.gigTitle}",
                    type = "NEW_APPLICATION",
                    relatedId = application.applicationId
                )
            )

            Log.d("CampusGig", "4. Notification created")

            Log.d("CampusGig", "5. Updating applicant count")

            gigCollection
                .document(application.gigId)
                .update(
                    "applicants",
                    FieldValue.increment(1)
                )
                .await()

            Log.d("CampusGig", "6. Applicant count updated")

        } catch (e: Exception) {

            Log.e("CampusGig", "applyForGig failed", e)

            throw e
        }
    }

    suspend fun hasAlreadyApplied(
        studentId: String,
        gigId: String
    ): Boolean {

        val result = applicationCollection
            .whereEqualTo("studentId", studentId)
            .whereEqualTo("gigId", gigId)
            .get()
            .await()

        return !result.isEmpty

    }

    suspend fun getAppliedGigIds(
        studentId: String
    ): Set<String> {

        return applicationCollection
            .whereEqualTo("studentId", studentId)
            .get()
            .await()
            .documents
            .mapNotNull {
                it.getString("gigId")
            }
            .toSet()

    }

    suspend fun loadMyApplications(
        studentId: String
    ): List<Application> {

        return applicationCollection
            .whereEqualTo("studentId", studentId)
            .get()
            .await()
            .toObjects(Application::class.java)
            .sortedByDescending {
                it.appliedAt
            }

    }

    suspend fun getApplicationById(
        applicationId: String
    ): Application? {
        return applicationCollection
            .document(applicationId)
            .get()
            .await()
            .toObject(Application::class.java)
    }

    suspend fun loadApplicantsForGig(
        gigId: String
    ): List<Application> {

        return applicationCollection
            .whereEqualTo("gigId", gigId)
            .get()
            .await()
            .toObjects(Application::class.java)
            .sortedByDescending {
                it.appliedAt
            }

    }

    suspend fun loadEmployerApplications(
        employerId: String
    ): List<Application> {

        val employerGigIds = gigCollection
            .whereEqualTo("employerId", employerId)
            .get()
            .await()
            .documents
            .map { it.id }

        if (employerGigIds.isEmpty()) return emptyList()

        return applicationCollection
            .get()
            .await()
            .toObjects(Application::class.java)
            .filter { it.gigId in employerGigIds }
            .sortedByDescending { it.appliedAt }
    }
    suspend fun acceptApplication(
        applicationId: String
    ) {

        val application = getApplicationById(applicationId)
            ?: return

        applicationCollection
            .document(applicationId)
            .update(
                "status",
                "Accepted"
            )
            .await()

        NotificationRepository().createNotification(
            Notification(
                notificationId = UUID.randomUUID().toString(),
                receiverId = application.studentId,
                title = "Application Accepted",
                message = "Congratulations! Your application for ${application.gigTitle} has been accepted.",
                type = "APPLICATION_ACCEPTED",
                relatedId = application.applicationId
            )

        )

    }

    suspend fun rejectApplication(
        applicationId: String
    ) {

        val application = getApplicationById(applicationId)
            ?: return

        applicationCollection
            .document(applicationId)
            .update(
                "status",
                "Rejected"
            )
            .await()

        NotificationRepository().createNotification(

            Notification(
                notificationId = UUID.randomUUID().toString(),
                receiverId = application.studentId,
                title = "Application Update",
                message = "Unfortunately, your application for ${application.gigTitle} was not successful.",
                type = "APPLICATION_REJECTED",
                relatedId = application.applicationId
            )

        )

    }

}
