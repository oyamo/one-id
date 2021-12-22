package com.oyasis.diabetescare.ui.recyclerview.status

import androidx.recyclerview.widget.RecyclerView
import com.oyasis.diabetescare.data.models.Status
import com.oyasis.diabetescare.databinding.StatusItemBinding

class StatusViewHolder(var binding: StatusItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun inflate(status: Status) {
        binding.statusText.text = status.issueText
        binding.timeText.text = status.timePosted
    }
}