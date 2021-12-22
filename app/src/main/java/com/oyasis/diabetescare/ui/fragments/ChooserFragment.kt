package com.oyasis.diabetescare.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.oyasis.diabetescare.R
import com.oyasis.diabetescare.data.models.User
import com.oyasis.diabetescare.databinding.FragmentChooserBinding

class ChooserFragment : Fragment() {

    private lateinit var binding: FragmentChooserBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChooserBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val user = auth.currentUser
        if (user == null) {
          findNavController().navigate(R.id.action_chooserFragment_to_login)
        } else {
            val localUser = User.fromPersistance(requireContext())
            if (localUser.Role == "Admin") {
                findNavController().navigate(R.id.action_chooserFragment_to_adminFragment)
            } else {
                findNavController().navigate(R.id.action_chooserFragment_to_normalUserFragment)
            }
        }
    }

}