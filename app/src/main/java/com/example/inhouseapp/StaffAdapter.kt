package com.example.inhouseapp

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


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

//        Picasso.get().load(currentData.avatar).into(holder.avatar)
        DownloadImageTask(holder.avatar).execute(currentData.avatar)
        holder.email.text = currentData.email
        holder.firstName.text = currentData.first_name
        holder.lastName.text = currentData.last_name
    }
     class DownloadImageTask(avatarDisplay: ImageView) : AsyncTask<String?, Void?, Bitmap?>() {
         val display = avatarDisplay
         override fun onPostExecute(result: Bitmap?) {
            if (result != null) {
                // display the image downloaded
                display.setImageBitmap(result)
            }
        }

         override fun doInBackground(vararg params: String?): Bitmap? {
             return try {
                 val url = URL(params[0])
                 val connection = url.openConnection() as HttpURLConnection
                 connection.doInput = true
                 connection.connect()
                 val input = connection.inputStream
                 BitmapFactory.decodeStream(input)
             } catch (e: IOException) {
                 e.printStackTrace()
                 null
             }
         }
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