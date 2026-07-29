package com.brian.campusgig.ui.Screens.Employer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brian.campusgig.data.models.Gig
import com.brian.campusgig.data.repository.GigRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GigViewModel : ViewModel() {

    private val repository = GigRepository()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    private val _gigs = MutableStateFlow<List<Gig>>(emptyList())
    val gigs: StateFlow<List<Gig>> = _gigs

    private val _selectedGig = MutableStateFlow<Gig?>(null)
    val selectedGig: StateFlow<Gig?> = _selectedGig
    fun createGig(gig: Gig) {

        viewModelScope.launch {

            _loading.value = true
            _success.value = false
            _message.value = ""

            val result = repository.createGig(gig)

            _loading.value = false

            result.onSuccess {

                _success.value = true
                _message.value = it

            }.onFailure {

                _success.value = false
                _message.value = it.message ?: "Failed to post gig"

            }

        }

    }

    fun loadEmployerGigs() {

        viewModelScope.launch {

            _loading.value = true

            val result = repository.getEmployerGigs()

            _loading.value = false

            result.onSuccess { gigs ->

                Log.d("GigViewModel", "Loaded ${gigs.size} gigs")

                gigs.forEach {
                    Log.d(
                        "GigViewModel",
                        "Gig: ${it.title} employer=${it.employerId}"
                    )
                }

                _gigs.value = gigs

            }.onFailure {

                Log.e(
                    "GigViewModel",
                    "Error: ${it.message}"
                )

                _message.value = it.message ?: "Error"

            }

        }

    }

    fun loadGig(gigId: String) {

        viewModelScope.launch {

            _loading.value = true

            val result = repository.getGigById(gigId)

            _loading.value = false

            result.onSuccess {

                _selectedGig.value = it

            }.onFailure {

                _message.value = it.message ?: "Unable to load gig"

            }

        }

    }
    fun getGigById(gigId: String) {

        viewModelScope.launch {

            _loading.value = true

            val result = repository.getGigById(gigId)

            _loading.value = false

            result.onSuccess {
                _selectedGig.value = it
            }.onFailure {
                _message.value = it.message ?: "Failed to load gig"
            }

        }

    }

    fun updateGig(gig: Gig) {

        viewModelScope.launch {

            _loading.value = true

            val result = repository.updateGig(gig)

            _loading.value = false

            result.onSuccess {

                _success.value = true

                _message.value = it

            }.onFailure {

                _message.value = it.message ?: "Failed to update gig"

            }

        }

    }

    fun deleteGig(gigId: String) {

        viewModelScope.launch {

            repository.deleteGig(gigId)

            loadEmployerGigs()

        }

    }

    fun resetState() {
        _loading.value = false
        _success.value = false
        _message.value = ""

    }

}