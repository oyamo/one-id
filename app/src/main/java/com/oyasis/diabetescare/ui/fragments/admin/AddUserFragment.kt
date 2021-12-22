package com.oyasis.diabetescare.ui.fragments.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.afollestad.vvalidator.form
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.oyasis.diabetescare.common.Const
import com.oyasis.diabetescare.data.models.ServerResponse
import com.oyasis.diabetescare.data.models.User
import com.oyasis.diabetescare.data.services.PatientService
import com.oyasis.diabetescare.databinding.FragmentAddUserBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddUserFragment : Fragment() {

    private lateinit var binding: FragmentAddUserBinding
    private val auth = FirebaseAuth.getInstance()
    private val currentUser = auth.currentUser
    private val firebaseDatabase = FirebaseDatabase.getInstance().reference
    private val TAG = "AddUserFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddUserBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setFormValidation()
    }

    private fun addNewUser() {
        val emailAddress = binding.emailAddress.text.toString()
        val firstName = binding.firstName.text.toString()
        val lastName = binding.lastName.text.toString()
        val role = "User"
        val telephone = binding.telephoneNumber.text.toString()
        val diabetesStage = binding.diabetesStage.text.toString()
        val patientNo = binding.patientId.text.toString()
        val doctorId = currentUser!!.uid

        val user = User(
            "",
            emailAddress,
            firstName,
            lastName,
            role,
            telephone,
            diabetesStage,
            patientNo,
            doctorId
        )

        val retrofit = Retrofit.Builder()
            .baseUrl(Const.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(PatientService::class.java)
        val call = service.addUser(user)
        call.enqueue(object: Callback<ServerResponse> {
            override fun onResponse(
                call: Call<ServerResponse>,
                response: Response<ServerResponse>
            ) {
               toggleProgress()
                val statusCode = response.code()
                val serverResponse = response.body()
                Log.d(TAG, "onResponse: ${serverResponse.toString()}")
                if (statusCode == 200 && serverResponse != null) {
                    findNavController().navigateUp()
                } else if (statusCode in listOf<Int>(400, 500) && serverResponse != null) {
                    alertError(serverResponse.error)
                } else {
                    alertError("A fatal error occurred.")
                    Log.d(TAG, "onResponse: ${serverResponse?.status_code}")
                }
            }
            override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                toggleProgress()
                alertError(t.localizedMessage?: "Could not add.")
            }

        })

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

    private fun setFormValidation() {
        form {
            input(binding.emailAddress) {
                isNotEmpty()
                isNotEmpty()
            }

            input(binding.firstName) {
                isNotEmpty()
            }

            input(binding.lastName) {
                isNotEmpty()
            }

            input(binding.telephoneNumber) {
                isNotEmpty()
            }

            input(binding.diabetesStage) {
                isNumber()
                isNotEmpty()
            }

            submitWith(binding.login) {
                if (it.success()) {
                    toggleProgress()
                    addNewUser()
                }
            }
        }
    }
}