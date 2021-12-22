package com.oyasis.diabetescare.ui.recyclerview.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oyasis.diabetescare.data.models.User
import com.oyasis.diabetescare.databinding.PatientItemBinding

class UserAdapter: RecyclerView.Adapter<UserViewHolder>() {

    private val dataList = arrayListOf<User>()
    private lateinit var  callback:(String, String)->Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = PatientItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  UserViewHolder(binding, callback)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.inflate(dataList[position])
    }

    override fun getItemCount(): Int {
        return  dataList.size
    }

    fun updateDataset(list: List<User>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    fun updateCallBack(callback: (String, String)->Unit) {
        this.callback = callback
    }
}