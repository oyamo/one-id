package com.oyasis.diabetescare.ui.fragments.normal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.afollestad.vvalidator.form
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.oyasis.diabetescare.R
import com.oyasis.diabetescare.common.Util.toDateTime
import com.oyasis.diabetescare.data.models.Status
import com.oyasis.diabetescare.data.models.User
import com.oyasis.diabetescare.databinding.FragmentAddStatusBinding


class AddStatusFragment : Fragment() {

    private lateinit var binding: FragmentAddStatusBinding
    private var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val firebaseRef = firebaseDatabase.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddStatusBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        form {
            input(binding.statusText) {
                isNotEmpty()
            }

            submitWith(binding.login) {
                if (it.success()) {
                    updatePatientStatus()
                }
            }
        }
    }

    private fun updatePatientStatus() {
        // Update the status
        toggleProgress()

        val patientStatus = binding.statusText.text.toString()
        val userId  =  FirebaseAuth.getInstance()
            .currentUser?.uid?:User.fromPersistance(requireContext()).UserId

        val status = Status(
            System.currentTimeMillis().toDateTime(),
            patientStatus
        )

        firebaseRef.child("Status").child(userId).push()
            .setValue(status).addOnSuccessListener {
                findNavController().navigateUp()
            }.addOnFailureListener {
                toggleProgress()
                alertError(it.localizedMessage?:"Unknown error")
            }
    }


    private fun toggleProgress() {
        binding.loading.let {
            val visibility = it.visibility
            it.visibility = when(visibility) {
                View.GONE -> View.VISIBLE
                else -> View.GONE
            }
        }
    }

    private fun alertError(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }



}