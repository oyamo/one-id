package com.oyasis.diabetescare.ui.fragments.admin

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
import com.oyasis.diabetescare.data.models.User
import com.oyasis.diabetescare.databinding.FragmentAdminBinding
import com.oyasis.diabetescare.ui.recyclerview.user.UserAdapter


class AdminFragment : Fragment() {

    private val userArrayList = arrayListOf<User>()
    private val database = FirebaseDatabase.getInstance()
    private val usersRef = database.getReference("Users")


    // Recyclerview Adapter
    val adapter = UserAdapter()

    private lateinit var binding: FragmentAdminBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentAdminBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.patientsList.adapter = adapter
        adapter.updateCallBack { uid, fname ->
            val nav = findNavController()

            val action = AdminFragmentDirections.actionAdminFragmentToStatusListFragment(
                uid, fname
            )

            nav.navigate(action)
        }

        binding.logoutBtn.setOnClickListener {
            logOut()
        }

        binding.swipeRefresh.setOnRefreshListener {
            loadUsers()
        }
    }

    override fun onResume() {
        super.onResume()
        addOnFabClickListener()
        loadUsers()
    }

    private fun logOut() {
        User.logOut(requireContext())
        FirebaseAuth.getInstance().signOut()
        requireActivity().finish()
    }

    private fun loadUsers() {
        toggleRefresh()
        usersRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                toggleRefresh()
                val users = snapshot.children
                if (users.iterator().hasNext()) userArrayList.clear()

                for (dataSnap in users) {
                    val user = dataSnap.getValue(User::class.java)
                    if (user != null && user.Role != "Admin") {
                        userArrayList.add(user)
                    }
                }

                adapter.updateDataset(userArrayList)
            }

            override fun onCancelled(error: DatabaseError) {
                toggleRefresh()
                showLoadingFailed(error.message)
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

    private fun addOnFabClickListener() {
        binding.newUser.setOnClickListener {
            findNavController().navigate(R.id.action_adminFragment_to_addUserFragment)
        }
    }
}