package com.brian.campusgig.data.repository

import com.brian.campusgig.data.models.Gig
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class StudentGigRepository {
    private val firestore = FirebaseFirestore.getInstance()

    fun loadLatestGigs(
        onSuccess: (List<Gig>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        firestore.collection("gigs")
            .whereEqualTo("status", "Open")
            .get()
            .addOnSuccessListener { snapshot ->
                val gigs = snapshot.documents
                    .mapNotNull {
                        it.toObject(Gig::class.java)
                    }
                    .sortedByDescending { it.postedAt }
                onSuccess(gigs)
            }
            .addOnFailureListener {
                onFailure(it.message ?: "Failed to load gigs.")
            }
    }
    suspend fun getGigById(gigId: String): Gig? {
        return firestore.collection("gigs")
            .document(gigId)
            .get()
            .await()
            .toObject(Gig::class.java)
    }
}

