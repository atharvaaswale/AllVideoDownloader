package com.unreal.allvideodownloader.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.unreal.allvideodownloader.InstagramDownload
import com.unreal.allvideodownloader.MainBrowser
import com.unreal.allvideodownloader.R
import com.unreal.allvideodownloader.WhatsappStatusSaver
import com.unreal.allvideodownloader.model.DashBoardApps

class DashboardAdapter(private val appList: ArrayList<DashBoardApps>): RecyclerView.Adapter<DashboardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.apps_cardview_dashboard, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.icon.setImageResource(appList[position].icon)
        holder.appName.text = appList[position].appName

        if (appList[position].appName == "Whatsapp"){
            holder.icon.setOnClickListener {
                holder.icon.context.startActivity(
                    Intent(holder.icon.context, WhatsappStatusSaver::class.java)
                )
            }
        } else if(appList[position].appName == "Instagram"){
            holder.icon.setOnClickListener {
                holder.icon.context.startActivity(
                    Intent(holder.icon.context, InstagramDownload::class.java)
                        .putExtra("flag", "IG")
                )
            }
        } else if(appList[position].appName == "Facebook"){
            holder.icon.setOnClickListener {
                holder.icon.context.startActivity(
                    Intent(holder.icon.context, MainBrowser::class.java)
                        .putExtra("flag", "FB")
                )
            }
        } else if(appList[position].appName == "Youtube"){
            holder.icon.setOnClickListener {
                holder.icon.context.startActivity(
                    Intent(holder.icon.context, MainBrowser::class.java)
                        .putExtra("flag", "YT")
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return appList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var icon = itemView.findViewById<ImageView>(R.id.imgApp)
        var appName = itemView.findViewById<TextView>(R.id.tvAppname)
    }

}