package com.brian.campusgig.data.repository

import android.util.Log
import com.brian.campusgig.data.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout

class AuthRepository {
    private val TAG = "AuthRepository"
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    suspend fun getCurrentUserData(): Result<User> {
        return try {
            val currentUser = FirebaseAuth.getInstance().currentUser
                ?: throw Exception("No user logged in")

            val userDoc = FirebaseFirestore.getInstance()
                .collection("users").document(currentUser.uid).get().await()

            val user = userDoc.toObject(User::class.java) ?: throw Exception("User data not found")
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(username: String, email: String, phoneNumber: String, password: String, role: String): Result<User> {
        return try {
            Log.d(TAG, "Starting registration for $email")
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user!!.uid
            Log.d(TAG, "User created in Auth with UID: $uid")
            
            // Use HashMap for Firestore to avoid serialization issues
            val userData = hashMapOf(
                "uid" to uid,
                "username" to username,
                "phoneNumber" to phoneNumber,
                "email" to email,
                "role" to role
            )
            
            Log.d(TAG, "Attempting to save user to Firestore...")
            
            // Add timeout to prevent indefinite hanging
            withTimeout(10000) {
                firestore.collection("users").document(uid).set(userData).await()
            }
            
            Log.d(TAG, "User successfully saved to Firestore")
            
            val user = User(uid, username, email, phoneNumber, role)
            Result.success(user)
        } catch (e: Exception) {
            Log.e(TAG, "Registration failed: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun login(input: String, password: String): Result<User> {
        return try {
            val email = if (input.contains("@")) {
                input
            } else {
                val snapshot = firestore.collection("users")
                    .whereEqualTo("username", input)
                    .get().await()
                if (snapshot.isEmpty) throw Exception("Username not found")
                snapshot.documents.first().getString("email") ?: throw Exception("Email not found for this username")
            }

            auth.signInWithEmailAndPassword(email, password).await()

            val uid = auth.currentUser!!.uid
            val userDoc = firestore.collection("users").document(uid).get().await()
            val user = userDoc.toObject(User::class.java) ?: throw Exception("User data not found")
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        auth.signOut()
    }
}