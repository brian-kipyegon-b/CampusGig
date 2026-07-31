package com.brian.campusgig.data.repository

import com.brian.campusgig.data.models.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProfileRepository {

    private val firestore = FirebaseFirestore.getInstance()

    private val profileCollection = firestore.collection("users")

    suspend fun loadProfile(uid: String): UserProfile? {
        return try {
            profileCollection
                .document(uid)
                .get()
                .await()
                .toObject(UserProfile::class.java)
        } catch (e: Exception) {
            null
        }
    }
}
