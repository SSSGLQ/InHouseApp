package com.example.inhouseapp

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class StaffAdapter(val context: Activity, val dataList: List<Data>):
    RecyclerView.Adapter<StaffAdapter.StaffViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaffViewHolder {
        // create the view in case the layout manager fails to create view for holder
        val itemView = LayoutInflater.from(context).inflate(R.layout.each_staff, parent, false)
        return StaffViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: StaffViewHolder, position: Int) {
        // populate the data into the view
        val currentData = dataList[position]

        Picasso.get().load(currentData.avatar).into(holder.avatar);
        holder.email.text = currentData.email
        holder.firstName.text = currentData.first_name
        holder.lastName.text = currentData.last_name
    }

    class StaffViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val avatar: ImageView
        val email: TextView
        val firstName: TextView
        val lastName: TextView

        init {
            avatar = itemView.findViewById(R.id.staffAvatar)
            email = itemView.findViewById(R.id.emailTextView)
            firstName = itemView.findViewById(R.id.firstNameTextView)
            lastName = itemView.findViewById(R.id.lastNameTextView)
        }
    }
}