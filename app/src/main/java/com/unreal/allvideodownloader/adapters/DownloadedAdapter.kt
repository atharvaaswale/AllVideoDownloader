package com.unreal.allvideodownloader.adapters

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.MediaController
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.unreal.allvideodownloader.R
import java.io.*

class DownloadedAdapter(
    private val filesList: ArrayList<String>,
    val context: Context,
) : RecyclerView.Adapter<DownloadedAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.whatsapp_status_column, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(filesList[position]).into(holder.image)
        if(filesList[position].endsWith(".mp4")){
            holder.play.visibility = View.VISIBLE
            holder.download.visibility = View.GONE
        }else{
            holder.image.setSystemUiVisibility( View.SYSTEM_UI_FLAG_HIDE_NAVIGATION )
            holder.download.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return filesList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val image = itemView.findViewById<ImageView>(R.id.ivStatus)
        val download = itemView.findViewById<ImageView>(R.id.download)
        val play = itemView.findViewById<ImageView>(R.id.play)
    }


}