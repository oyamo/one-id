package com.oyasis.diabetescare.ui.fragments.normal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.oyasis.diabetescare.R
import com.oyasis.diabetescare.data.models.MedicalAdvice
import com.oyasis.diabetescare.data.models.User
import com.oyasis.diabetescare.databinding.FragmentNormalUserBinding
import com.oyasis.diabetescare.ui.recyclerview.medical_advice.MAAdapter

class NormalUserFragment : Fragment() {
    private val adviceArrayList = arrayListOf<MedicalAdvice>()
    private val database = FirebaseDatabase.getInstance()
    private val adviceRef = database.getReference("MedicalAdvice")
    private val firebaseUser = FirebaseAuth.getInstance().currentUser
    private val adapter =MAAdapter()

    private lateinit var binding: FragmentNormalUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNormalUserBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.patientsList.adapter = adapter
        loadUsers()
        binding.swipeRefresh.setOnRefreshListener {
            toggleRefresh()
            loadUsers()
        }
        binding.newStatus.setOnClickListener {
            findNavController().navigate(R.id.action_normalUserFragment_to_addStatusFragment)
        }

        binding.logoutBtn.setOnClickListener {
            logOut()
        }
    }

    private fun logOut() {
        User.logOut(requireContext())
        FirebaseAuth.getInstance().signOut()
        requireActivity().finish()
    }


    private fun loadUsers() {
        toggleRefresh()
        val userId = firebaseUser?.uid?:User.fromPersistance(requireContext()).UserId

        adviceRef.child(userId).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                toggleRefresh()
                val adviceChildren = snapshot.children

                if (adviceChildren.iterator().hasNext()) {
                    adviceArrayList.clear()
                    toggleVG()
                }

                for (dataSnap in adviceChildren) {
                    val advice = dataSnap.getValue(MedicalAdvice::class.java)
                    if (advice != null) {
                        adviceArrayList.add(advice)
                    }
                }

                adapter.updateDataset(adviceArrayList)
            }

            override fun onCancelled(error: DatabaseError) {
                toggleRefresh()
                showLoadingFailed(error.message)
            }
        })
    }

    private fun toggleRefresh() {
        binding.swipeRefresh.isRefreshing =
            !binding.swipeRefresh.isRefreshing
    }

    private fun toggleVG() {
        binding.emptyGrp.visibility = View.GONE
    }

    private fun showLoadingFailed(errorString: String) {
        Toast.makeText(requireContext(), errorString, Toast.LENGTH_SHORT).show()
    }
}