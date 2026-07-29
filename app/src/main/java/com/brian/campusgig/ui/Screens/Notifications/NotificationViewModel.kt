package com.brian.campusgig.ui.Screens.Notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brian.campusgig.data.models.Notification
import com.brian.campusgig.data.repository.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificationViewModel : ViewModel() {

    private val repository = NotificationRepository()

    private val _notifications =
        MutableStateFlow<List<Notification>>(emptyList())

    val notifications: StateFlow<List<Notification>>
            = _notifications

    private val _loading =
        MutableStateFlow(false)

    val loading: StateFlow<Boolean>
            = _loading
    private val _unreadCount =
        MutableStateFlow(0)

    val unreadCount: StateFlow<Int> =
        _unreadCount

    fun loadNotifications(receiverId: String) {

        viewModelScope.launch {

            _loading.value = true

            val result =
                repository.loadNotifications(receiverId)

            android.util.Log.d(
                "CampusGig",
                "Notifications loaded = ${result.size}"
            )

            _notifications.value = result

            _loading.value = false
        }

    }
    fun loadUnreadCount(
        receiverId: String
    ) {

        viewModelScope.launch {

            _unreadCount.value =
                repository.getUnreadCount(receiverId)

        }

    }

    fun markAsRead(
        notificationId: String,
        receiverId: String
    ) {
        viewModelScope.launch {

            repository.markAsRead(notificationId)

            _notifications.value =
                repository.loadNotifications(receiverId)

            _unreadCount.value =
                repository.getUnreadCount(receiverId)
        }
    }
}