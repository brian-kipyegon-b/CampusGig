package com.brian.campusgig.data.repository

import com.brian.campusgig.data.models.Application
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class EmployerApplicationRepository {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getApplicantsForGig(
        gigId: String
    ): List<Application> {
        return firestore
            .collection("applications")
            .whereEqualTo("gigId", gigId)
            .get()
            .await()
            .toObjects(Application::class.java)
            .sortedByDescending { it.appliedAt }

    }
    suspend fun updateApplicationStatus(
        applicationId: String,
        status: String
    ) {
        firestore
            .collection("applications")
            .document(applicationId)
            .update(
                "status",
                status
            )
            .await()
    }

}