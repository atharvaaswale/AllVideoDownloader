package com.unreal.allvideodownloader

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.unreal.allvideodownloader.adapters.DownloadedAdapter
import com.unreal.allvideodownloader.adapters.StatusAdapter
import com.unreal.allvideodownloader.databinding.ActivityDownloadsFolderBinding
import java.io.File

class DownloadsFolder : AppCompatActivity() {
    lateinit var binding: ActivityDownloadsFolderBinding
    var filesList = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_downloads_folder)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
        val background: Drawable = this.getResources().getDrawable(R.drawable.dashboard_gradient_bg)
        window.setBackgroundDrawable(background)

        binding.back.setOnClickListener { finish() }
        getImagePath()
        binding.rv.adapter = DownloadedAdapter(filesList, this)
        binding.rv.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rv.setHasFixedSize(true)
    }

    fun getImagePath() {
        val statusDirectory = File(Environment.getExternalStorageDirectory().absolutePath + "/Download/ig_download/")
        if (statusDirectory.exists() && statusDirectory.isDirectory) {
            val files = statusDirectory.listFiles { file ->
                file.isFile && file.name.endsWith(".jpg")
                        || file.name.endsWith(".jpeg")
                        || file.name.endsWith(".png")
                        || file.name.endsWith(".mp4")
            }
            files?.forEach {
                filesList.add(it.absolutePath)
            }
        } else {
            Toast.makeText(
                applicationContext,
                "The directory does not exist or is not a directory.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}