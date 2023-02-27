package com.unreal.allvideodownloader.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class Downloaders {
    companion object{
        fun downloadIGVideo(context: Context?, postUrl: String) {
            val replacedUrl: String
            val finalVideoUrl = arrayOfNulls<String>(1)
            val requestQueue: RequestQueue
            requestQueue = Volley.newRequestQueue(context)
            if (TextUtils.isEmpty(postUrl)) {
                Log.e("VideoURLErrors", "Provided String is empty.")
            } else {
                replacedUrl = if (postUrl.contains("?utm_source=ig_web_copy_link")) {
                    val partToRemove = "?utm_source=ig_web_copy_link"
                    postUrl.replace(partToRemove, "")
                } else if (postUrl.contains("?utm_source=ig_web_button_share_sheet")) {
                    val partToRemove = "?utm_source=ig_web_button_share_sheet"
                    postUrl.replace(partToRemove, "")
                } else if (postUrl.contains("?utm_medium=share_sheet")) {
                    val partToRemove = "?utm_medium=share_sheet"
                    postUrl.replace(partToRemove, "")
                } else if (postUrl.contains("?utm_medium=copy_link")) {
                    val partToRemove = "?utm_medium=copy_link"
                    postUrl.replace(partToRemove, "")
                } else {
                    postUrl
                }
                val jsonObjectRequest = JsonObjectRequest(
                    Request.Method.GET,
                    "$replacedUrl?__a=1&__d=dis", null,
                    { response ->
                        var Obj1: JSONObject? = null
                        try {
                            Obj1 = response.getJSONObject("graphql")
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        var Obj2: JSONObject? = null
                        try {
                            Obj2 = Obj1!!.getJSONObject("shortcode_media")
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        try {
                            finalVideoUrl[0] = Obj2!!.getString("video_url")
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        Log.d("finalURL", finalVideoUrl[0]!!)
                        download(
                            finalVideoUrl[0],
                            context!!,
                            System.currentTimeMillis().toString() + ".mp4"
                        )
                    }) { error ->
                    Log.e("VideoURLErrors", "Something went wrong$error")
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
                requestQueue.add(jsonObjectRequest)
            }
        }


        fun download(
            downloadPath: String?,
            context: Context,
            fileName: String
        ) {
            Toast.makeText(context, "Downloading started...", Toast.LENGTH_SHORT).show()
            val uri = Uri.parse(downloadPath)
            val request = DownloadManager.Request(uri)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setTitle(fileName)
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS, "/ig_download/$fileName"
            )
            (context.getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager).enqueue(request)
        }
    }
}