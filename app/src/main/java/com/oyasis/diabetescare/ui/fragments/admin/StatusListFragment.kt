package com.oyasis.diabetescare.ui.fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.oyasis.diabetescare.R
import com.oyasis.diabetescare.data.models.Status
import com.oyasis.diabetescare.databinding.FragmentStatusListBinding
import com.oyasis.diabetescare.ui.recyclerview.status.StatusAdapter


class StatusListFragment : Fragment() {

    private lateinit var binding: FragmentStatusListBinding
    val args: StatusListFragmentArgs by navArgs()

    private val userArrayList = arrayListOf<Status>()
    private val database = FirebaseDatabase.getInstance()
    private val usersRef = database.getReference("Status")

    private val adapter = StatusAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStatusListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.patientName.text = String.format(getString(R.string.status_title), args.firstName)
        loadStatuses()
        binding.swipeRefresh.setOnRefreshListener {
            loadStatuses()
        }
        binding.statusList.adapter = adapter

        binding.updateMedication.setOnClickListener {
            val action = StatusListFragmentDirections.actionStatusListFragmentToUpdateMedicationFragment(args.userId)
            findNavController().navigate(action)
        }
    }

    private fun loadStatuses() {
        toggleRefresh()
        usersRef.child(args.userId).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                toggleRefresh()
                val status = snapshot.children
                if (status.iterator().hasNext()) {
                    userArrayList.clear()
                    binding.statusAvailability.visibility = View.GONE
                } else {
                    showLoadingFailed("No data available")
                    binding.statusAvailability.visibility = View.VISIBLE
                }

                for (dataSnap in status) {
                    val state = dataSnap.getValue(Status::class.java)
                    if (state != null) {
                        userArrayList.add(state)
                    }
                }
                adapter.updateDataset(userArrayList)
            }

            override fun onCancelled(error: DatabaseError) {
                toggleRefresh()
                showLoadingFailed("No data available")
            }
        })
    }
    private fun showLoadingFailed(errorString: String) {
        Toast.makeText(requireContext(), errorString, Toast.LENGTH_SHORT).show()
    }
    private fun toggleRefresh() {
        binding.swipeRefresh.isRefreshing =
            !binding.swipeRefresh.isRefreshing
    }

}