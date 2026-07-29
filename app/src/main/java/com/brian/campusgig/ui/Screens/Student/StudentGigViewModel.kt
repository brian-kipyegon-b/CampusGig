package com.brian.campusgig.ui.Screens.Student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brian.campusgig.data.models.Gig
import com.brian.campusgig.data.repository.StudentGigRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StudentGigViewModel: ViewModel() {

    private val repository = StudentGigRepository()

    private val _gigs = MutableStateFlow<List<Gig>>(emptyList())
    val gigs: StateFlow<List<Gig>> = _gigs

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _selectedGig = MutableStateFlow<Gig?>(null)
    val selectedGig: StateFlow<Gig?> = _selectedGig

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    fun loadLatestGigs() {

        _loading.value = true
        repository.loadLatestGigs(
            onSuccess = { latestGigs ->
                _gigs.value = latestGigs
                _loading.value = false
            },
            onFailure = { _loading.value = false }
        )
    }

    fun loadGig(gigId: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                _selectedGig.value = repository.getGigById(gigId)
            } catch (e: Exception) {
                _error.value = e.message ?: "An unexpected error occurred."
            } finally {
                _loading.value = false
            }
        }
    }
}