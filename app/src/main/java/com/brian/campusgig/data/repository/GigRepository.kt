package com.brian.campusgig.data.repository

import android.util.Log
import com.brian.campusgig.data.models.Gig
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class GigRepository {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun createGig(gig: Gig): Result<String> {

        return try {

            firestore
                .collection("gigs")
                .document(gig.gigId)
                .set(gig)
                .await()

            Result.success("Gig posted successfully")

        } catch (e: Exception) {

            Result.failure(e)

        }

    }

    suspend fun getEmployerGigs(): Result<List<Gig>> {

        return try {

            val employerId = FirebaseAuth.getInstance().currentUser?.uid
                ?: return Result.failure(Exception("User not logged in"))

            Log.d("GigRepository", "Current Employer ID: $employerId")

            val snapshot = firestore
                .collection("gigs")
                .whereEqualTo("employerId", employerId)
                .get()
                .await()

            Log.d("GigRepository", "Documents found: ${snapshot.size()}")

            val gigs = snapshot
                .toObjects(Gig::class.java)
                .sortedByDescending { it.postedAt }

            gigs.forEach {
                Log.d(
                    "GigRepository",
                    "Gig Title: ${it.title}, Employer: ${it.employerId}"
                )
            }

            Result.success(gigs)

        } catch (e: Exception) {

            Log.e("GigRepository", "Firestore Error", e)

            Result.failure(e)

        }
    }

    suspend fun getGigById(gigId: String): Result<Gig> {

        return try {

            val document = firestore
                .collection("gigs")
                .document(gigId)
                .get()
                .await()

            val gig = document.toObject(Gig::class.java)
                ?: throw Exception("Gig not found")

            Result.success(gig)

        } catch (e: Exception) {

            Result.failure(e)

        }

    }

    suspend fun updateGig(gig: Gig): Result<String> {
        return try {

            firestore
                .collection("gigs")
                .document(gig.gigId)
                .set(gig)
                .await()

            Result.success("Gig updated successfully")
        } catch (e: Exception) {

            Result.failure(e)
        }

    }

    suspend fun deleteGig(gigId: String): Result<String> {

        return try {

            firestore
                .collection("gigs")
                .document(gigId)
                .delete()
                .await()

            Result.success("Gig deleted")

        } catch (e: Exception) {

            Result.failure(e)

        }

    }

}