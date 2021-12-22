package com.oyasis.diabetescare.ui.recyclerview.status

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oyasis.diabetescare.data.models.Status
import com.oyasis.diabetescare.databinding.StatusItemBinding

class StatusAdapter(): RecyclerView.Adapter<StatusViewHolder>() {
    private val dataList = arrayListOf<Status>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val binding = StatusItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  StatusViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        holder.inflate(dataList[position])
    }

    override fun getItemCount(): Int {
        return  dataList.size
    }

    fun updateDataset(list: List<Status>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }
}