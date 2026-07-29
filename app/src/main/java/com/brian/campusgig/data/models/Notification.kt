package com.brian.campusgig.data.models

data class Notification(

    val notificationId: String = "",

    val receiverId: String = "",

    val title: String = "",

    val message: String = "",

    val type: String = "",

    val relatedId: String = "",

    val read: Boolean = false,

    val createdAt: Long = System.currentTimeMillis()

)