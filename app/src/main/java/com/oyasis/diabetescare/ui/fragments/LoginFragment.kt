package com.oyasis.diabetescare.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.afollestad.vvalidator.form
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.oyasis.diabetescare.R
import com.oyasis.diabetescare.data.models.User
import com.oyasis.diabetescare.data.models.saveToPersistance
import com.oyasis.diabetescare.databinding.FragmentLoginBinding


class Login : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val auth = FirebaseAuth.getInstance()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val dbReference = firebaseDatabase.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        formValidation()
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

    private fun attemptLogin() {
        val password = binding.password.text.toString()
        val email = binding.username.text.toString()

        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            if (it.user != null) {
                getUserDetails(it.user!!.uid)
            } else{
                toggleProgress()
                alertError("Unknown Error")

            }
        }.addOnFailureListener {
            toggleProgress()
            alertError(it.localizedMessage?: "Unknown error")
        }

    }

    private fun alertError(error: String = "Login failed") {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    private fun getUserDetails(userId: String) {
        dbReference.child("Users").child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if(user != null) {
                    user.saveToPersistance(requireContext())
                    if (user.Role == "Admin") {
                        findNavController().navigate(R.id.action_login_to_adminFragment)
                    } else {
                        findNavController().navigate(R.id.action_login_to_normalUserFragment)
                    }
                } else {
                    alertError("Could not get user Records")
                    auth.signOut()
                    toggleProgress()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                alertError("Could not get user Records")
                auth.signOut()
                toggleProgress()
            }

        })
    }

    private fun formValidation() {
        form {
            input(binding.password) {
                isNotEmpty()
            }

            input(binding.username) {
                isEmail()
                isNotEmpty()
            }

            submitWith(binding.login) {
                if (it.success()) {
                    toggleProgress()
                    attemptLogin()
                }
            }
        }
    }

}