package com.oyasis.diabetescare.ui.fragments.admin

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.database.FirebaseDatabase
import com.oyasis.diabetescare.R
import com.oyasis.diabetescare.common.Util
import com.oyasis.diabetescare.common.Util.toTime
import com.oyasis.diabetescare.data.models.MedicalAdvice
import com.oyasis.diabetescare.data.models.User
import com.oyasis.diabetescare.databinding.FragmentUpdateMedicationBinding


class UpdateMedicationFragment : Fragment(),
    DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    private val TAG = "Update"

    private lateinit var binding: FragmentUpdateMedicationBinding

    private lateinit var appointmentDate: String
    private lateinit var appointmentTime: String

    val calendar: Calendar = Calendar.getInstance()
    val validator: Util.TimeValidator = Util.TimeValidator()

    val args: UpdateMedicationFragmentArgs by navArgs()
    val database = FirebaseDatabase.getInstance()
    val medicationRef = database.getReference("MedicalAdvice")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUpdateMedicationBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun toggleAppointment() {
        val visibility = if (binding.appointmentCheckBox.isChecked) View.VISIBLE else View.GONE
        binding.appointmentVG.visibility = visibility
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appointmentCheckBox.setOnClickListener {
            toggleAppointment()
        }

        binding.pickDate.setOnClickListener {
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)
            val datePickerDialog = DatePickerDialog(requireContext(), this, year, month, day)
            datePickerDialog.show()
        }

        binding.pickTime.setOnClickListener {
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            val datePickerDialog = TimePickerDialog(requireContext(),this, hour, minute, true )
            datePickerDialog.show()
        }

        binding.postUpdate.setOnClickListener {
            postUpdate()
        }

    }

    override fun onResume() {
        super.onResume()
        toggleAppointment()
    }



    private fun postUpdate() {
        val advice = binding.medicalAdvice.text.toString()
        val appointmentAddress = binding.locationAddress.text.toString()

        val adviceObj = MedicalAdvice(
            advice,
            System.currentTimeMillis(),
            null,
            null,
            User.fromPersistance(requireContext()).FirstName,
            appointmentAddress
        )

        if (advice.isEmpty()) {
            binding.medicalAdvice.selectAll()
            binding.medicalAdvice.error = ""
            return
        }

        if (binding.appointmentCheckBox.isChecked) {

            if (!validator.validateDateTime()) {
                alertError("Date can not be in the past")
                return
            }

            if (appointmentAddress.isEmpty()) {
                binding.locationAddress.selectAll()
                binding.locationAddress.error = ""
            }

            adviceObj.appointmentDate = binding.pickDate.text.toString()
            adviceObj.appointmentTime = binding.pickTime.text.toString()
        }

        toggleProgress()

        medicationRef.child(args.userId).push().setValue(adviceObj).addOnSuccessListener {
            findNavController().navigate(R.id.action_updateMedicationFragment_to_adminFragment)
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


    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        this.appointmentDate = "${day.toTime()}-${(month + 1).toTime()}-${year.toTime()}"
        this.binding.pickDate.text = this.appointmentDate
        validator.setDate(year, month, day)
    }

    override fun onTimeSet(p_: TimePicker?, hour: Int, minute: Int) {
        this.appointmentTime = "${hour.toTime()}:${minute.toTime()}"
        this.binding.pickTime.text = this.appointmentTime
        Log.d(TAG, "onDateSet: $hour")
        validator.setTime(hour, minute)
    }

}