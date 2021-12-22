package com.oyasis.diabetescare.ui.recyclerview.user

import androidx.recyclerview.widget.RecyclerView
import com.oyasis.diabetescare.data.models.User
import com.oyasis.diabetescare.databinding.PatientItemBinding

class UserViewHolder(val binding: PatientItemBinding, val callback: (String, String) -> Unit) : RecyclerView.ViewHolder(binding.root) {

    fun inflate(user: User) {
        binding.diabetesStage.text = "Stage ${user.DiabetesStage}"
        binding.firstName.text = user.FirstName
        binding.patientId.text = user.PatientNo
        binding.root.setOnClickListener {
            callback(user.UserId, user.FirstName)
        }
    }

}