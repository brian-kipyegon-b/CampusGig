package com.brian.campusgig.data.repository

import com.brian.campusgig.data.models.Gig
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EmployerRepository {

    private val database = FirebaseDatabase.getInstance().reference

    fun getEmployerGigs(

        employerId: String,

        onResult: (List<Gig>) -> Unit

    ) {

        database.child("gigs").orderByChild("employerId").equalTo(employerId)

            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    val gigs = mutableListOf<Gig>()
                    snapshot.children.forEach {

                        val gig = it.getValue(Gig::class.java)

                        if (gig != null) {

                            gigs.add(gig)

                        }

                    }

                    onResult(gigs)

                }

                override fun onCancelled(error: DatabaseError) {

                    onResult(emptyList())

                }

            })

    }

}