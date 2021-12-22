package com.oyasis.diabetescare.ui.recyclerview.medical_advice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oyasis.diabetescare.data.models.MedicalAdvice
import com.oyasis.diabetescare.data.models.Status
import com.oyasis.diabetescare.databinding.AdviceItemBinding

class MAAdapter: RecyclerView.Adapter<MAViewHolder>() {
    private val adviceArrayList = arrayListOf<MedicalAdvice>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MAViewHolder {
        val binding: AdviceItemBinding = AdviceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MAViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MAViewHolder, position: Int) {
        holder.bind(adviceArrayList[position])
    }

    override fun getItemCount(): Int {
        return adviceArrayList.size
    }

    fun updateDataset(list: List<MedicalAdvice>) {
        adviceArrayList.clear()
        adviceArrayList.addAll(list)
        notifyDataSetChanged()
    }
}