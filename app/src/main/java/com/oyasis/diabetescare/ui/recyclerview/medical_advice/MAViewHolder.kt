package com.oyasis.diabetescare.ui.recyclerview.medical_advice

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.oyasis.diabetescare.common.Util.toDate
import com.oyasis.diabetescare.data.models.MedicalAdvice
import com.oyasis.diabetescare.databinding.AdviceItemBinding
import com.oyasis.diabetescare.databinding.MedicationItemBinding

class MAViewHolder(val binding: AdviceItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(advice: MedicalAdvice) {
        binding.appointmentAddress.text = "Address: ${advice.appointmentAddress}"
        binding.timeText.text = advice.timeStampPosted.toDate()
        binding.medicText.text = advice.advice
        binding.doctorName.text = "Dr. "+advice.doctorName

        advice.appointmentDate?.let {
           if (it.isNotEmpty()) {
               binding.appointmentGroup.visibility = View.VISIBLE
               binding.appointmentAddress.text = advice.appointmentAddress
               binding.appointmentDateTime.text = "${advice.appointmentDate} at ${advice.appointmentTime}"
           }
        }
    }
}