package com.brian.campusgig.ui.Screens.Employer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.brian.campusgig.data.models.Gig
import com.brian.campusgig.data.repository.EmployerRepository
import com.google.firebase.auth.FirebaseAuth

class EmployerDashboardViewModel : ViewModel() {

    private val repository = EmployerRepository()

    var gigs by mutableStateOf<List<Gig>>(emptyList())
        private set

    var activeGigs by mutableStateOf(0)
        private set

    var closedGigs by mutableStateOf(0)
        private set

    var applications by mutableStateOf(0)
        private set

    var loading by mutableStateOf(true)
        private set

    init {

        loadDashboard()

    }

    private fun loadDashboard() {

        val employerId = FirebaseAuth
            .getInstance()
            .currentUser
            ?.uid ?: return

        repository.getEmployerGigs(employerId) { list ->

            gigs = list

            activeGigs = list.count {

                it.status == "Open"

            }

            closedGigs = list.count {

                it.status == "Closed"

            }

            applications = list.sumOf {

                it.applicants

            }

            loading = false

        }

    }

}