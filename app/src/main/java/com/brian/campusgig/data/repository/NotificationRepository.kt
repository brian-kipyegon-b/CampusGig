package com.brian.campusgig.data.repository

import com.brian.campusgig.data.models.Notification
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class NotificationRepository {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun createNotification(notification: Notification) {

        firestore.collection("notifications")
            .document(notification.notificationId)
            .set(notification)
            .await()
    }

    suspend fun loadNotifications(
        userId: String
    ): List<Notification> {

        val snapshot = firestore
            .collection("notifications")
            .whereEqualTo("receiverId", userId)
            .get()
            .await()

        android.util.Log.d(
            "CampusGig",
            "Firestore returned ${snapshot.size()} notifications"
        )

        return snapshot
            .toObjects(Notification::class.java)
            .sortedByDescending {
                it.createdAt
            }

    }

    suspend fun markAsRead(notificationId: String){

        firestore.collection("notifications")
            .document(notificationId)
            .update("read", true)
            .await()

    }
    suspend fun getUnreadCount(
        userId: String
    ): Int {

        return firestore.collection("notifications")
            .whereEqualTo("receiverId", userId)
            .whereEqualTo("read", false)
            .get()
            .await()
            .size()

    }

}