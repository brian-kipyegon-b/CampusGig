package com.brian.campusgig.ui.Screens.Authentication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brian.campusgig.data.models.User
import com.brian.campusgig.data.repository.AuthRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: User) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val TAG = "AuthViewModel"
    private val repo = AuthRepository()
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val firestore = FirebaseFirestore.getInstance()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user
    fun registerUser(username: String, email: String, password: String, phoneNumber: String, confirmPassword: String, role: String) {
        if (role.isEmpty()) {
            _authState.value = AuthState.Error("Please select a role (Student or Employer)")
            return
        }
        if (phoneNumber.isBlank()) {
            _authState.value = AuthState.Error("Please enter your phone number")
            return
        }
        if (password != confirmPassword) {
            _authState.value = AuthState.Error("Passwords do not match")
            return
        }
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            Log.d(TAG, "Calling repo.register")
            val result = repo.register(username, email, phoneNumber, password, role)
            Log.d(TAG, "repo.register returned: $result")
            _authState.value = result.fold(
                onSuccess = { user -> 
                    Log.d(TAG, "Registration Success")
                    AuthState.Success(user) 
                },
                onFailure = { 
                    Log.e(TAG, "Registration Failure: ${it.message}")
                    AuthState.Error(it.message ?: "Registration failed") 
                }
            )
        }
    }

    fun loginUser(input: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = repo.login(input, password)
            _authState.value = result.fold(
                onSuccess = { user -> AuthState.Success(user) }, // CHANGED: Passes user object
                onFailure = { AuthState.Error(it.message ?: "Login failed") }
            )
        }
    }

    fun checkAuthStatus() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            // We must fetch the custom User object from Firestore to get the role
            val result = repo.getCurrentUserData()

            _authState.value = result.fold(
                onSuccess = { user -> AuthState.Success(user) }, // Now correctly passes the User object
                onFailure = { AuthState.Idle } // If no user is logged in, stay Idle
            )
        }
    }
    fun loadUser(uid: String) {
        firestore.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { document ->

                _user.value = document.toObject(User::class.java)

            }

    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }

    fun logout() {
        repo.logout()
        _authState.value = AuthState.Idle
    }
}