package com.brian.campusgig.ui.Screens.Student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brian.campusgig.data.models.Application
import com.brian.campusgig.data.repository.StudentApplicationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StudentApplicationViewModel : ViewModel() {
    private val repository = StudentApplicationRepository()
    private val _applications =
        MutableStateFlow<List<Application>>(emptyList())

    val applications: StateFlow<List<Application>>
            = _applications.asStateFlow()

    private val _selectedApplication =
        MutableStateFlow<Application?>(null)

    val selectedApplication: StateFlow<Application?> =
        _selectedApplication.asStateFlow()
    private val _appliedGigIds =
        MutableStateFlow<Set<String>>(emptySet())

    val appliedGigIds: StateFlow<Set<String>> =
        _appliedGigIds.asStateFlow()

    private val _loading =
        MutableStateFlow(false)

    val loading: StateFlow<Boolean>
            = _loading.asStateFlow()

    private val _applicationSuccess =
        MutableStateFlow(false)

    val applicationSuccess: StateFlow<Boolean>
            = _applicationSuccess.asStateFlow()

    private val _error =
        MutableStateFlow<String?>(null)

    val error: StateFlow<String?>
            = _error.asStateFlow()

    fun applyForGig(
        application: Application
    ) {

        viewModelScope.launch {
            try {
                _loading.value = true
                val alreadyApplied =
                    repository.hasAlreadyApplied(
                        application.studentId,
                        application.gigId
                    )
                if (alreadyApplied) {
                    _error.value =
                        "You have already applied for this gig."
                    _loading.value = false
                    return@launch
                }
                repository.applyForGig(application)
                _applicationSuccess.value = true
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadMyApplications(
        studentId: String
    ) {
        viewModelScope.launch {
            _loading.value = true
            try {
                _applications.value =
                    repository.loadMyApplications(studentId)

            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }

    }

    fun loadAppliedGigIds(
        studentId: String
    ) {
        viewModelScope.launch {
            _appliedGigIds.value =
                repository.getAppliedGigIds(studentId)
        }
    }

    fun loadApplication(
        applicationId: String
    ) {

        viewModelScope.launch {
            _loading.value = true
            try { _selectedApplication.value =
                repository.getApplicationById(applicationId)
            } catch (e: Exception) {
                _error.value = e.message
            } finally { _loading.value = false }
        }
    }
    suspend fun acceptApplication(
        application: Application
    ) {
        repository.acceptApplication(
            application.applicationId
        )
    }

    suspend fun rejectApplication(
        application: Application
    ) {
        repository.rejectApplication(
            application.applicationId
        )
    }
    fun clearError() { _error.value = null }
    fun resetSuccess() { _applicationSuccess.value = false }
}
