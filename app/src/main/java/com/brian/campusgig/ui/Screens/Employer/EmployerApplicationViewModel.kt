package com.brian.campusgig.ui.Screens.Employer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brian.campusgig.data.models.Application
import com.brian.campusgig.data.repository.StudentApplicationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EmployerApplicationViewModel : ViewModel() {

    private val repository = StudentApplicationRepository()

    private val _applications =
        MutableStateFlow<List<Application>>(emptyList())

    val applications: StateFlow<List<Application>> =
        _applications

    private val _loading =
        MutableStateFlow(false)

    val loading: StateFlow<Boolean> =
        _loading

    fun loadApplicants(gigId: String) {

        viewModelScope.launch {

            _loading.value = true

            _applications.value =
                repository.loadApplicantsForGig(gigId)

            _loading.value = false

        }

    }
    fun loadEmployerApplications(employerId: String) {
        viewModelScope.launch {
            _loading.value = true
            _applications.value =
                repository.loadEmployerApplications(employerId)
            _loading.value = false
        }
    }


}