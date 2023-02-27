package com.unreal.allvideodownloader

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.unreal.allvideodownloader.utils.Util.Companion.WHATSAPP_STATUS_FOLDER_PATH
import com.unreal.allvideodownloader.adapters.StatusAdapter
import com.unreal.allvideodownloader.databinding.ActivityWhatsappStatusSaverBinding
import com.unreal.allvideodownloader.model.StatusModel
import java.io.File
import java.util.*

class WhatsappStatusSaver : AppCompatActivity() {
    lateinit var binding: ActivityWhatsappStatusSaverBinding
    private var statusList =  ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_whatsapp_status_saver)
        getImagePath()
        binding.rv.adapter = StatusAdapter(statusList, this)
        binding.rv.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rv.setHasFixedSize(true)
    }

    fun getData(){
        val model: StatusModel
        val targetPath: String = Environment.getExternalStorageDirectory().absolutePath +"/WhatsApp/Media/.statuses"
        val targetDirector = File(targetPath)
        val allFiles = targetDirector.listFiles()

        val targetPathBusiness = Environment.getExternalStorageDirectory().absolutePath +"/WhatsApp Business/Media/.statuses"
        val targetDirectorBusiness = File(targetPathBusiness)
        val allFilesBusiness = targetDirectorBusiness.listFiles()

    }

    /*fun getImagePath(): ArrayList<String> {
        // image path list
        val list: ArrayList<String> = ArrayList()

        // fetching file path from storage
        val file = File(Environment.getExternalStorageDirectory().toString() + WHATSAPP_STATUS_FOLDER_PATH)
        val listFile = file.listFiles()

        if (listFile != null && listFile.isNullOrEmpty()) {
            Arrays.sort(listFile, LastModifiedFileComparator.LASTMODIFIED_REVERSE)
        }

        if (listFile != null) {
            for (imgFile in listFile) {
                if (
                    imgFile.name.endsWith(".jpg")
                    || imgFile.name.endsWith(".jpeg")
                    || imgFile.name.endsWith(".png")
                    || imgFile.name.endsWith(".mp4")
                ) {
                    val model = imgFile.absolutePath
                    list.add(model)
                }
            }
        }
        // return imgPath List
        return list
    }*/

    fun getImagePath(){
        val statusDirectory = File(Environment.getExternalStorageDirectory().toString() + WHATSAPP_STATUS_FOLDER_PATH)
        if (statusDirectory.exists() && statusDirectory.isDirectory) {
            val statuses = statusDirectory.listFiles { file ->
                file.isFile && file.name.endsWith(".jpg")
                        || file.name.endsWith(".jpeg")
                        || file.name.endsWith(".png")
                        || file.name.endsWith(".mp4")
            }
            statuses?.forEach {
                Log.d("File name: ", it.absolutePath+"\n")
                statusList.add(it.absolutePath)

            }
        } else {
            Toast.makeText(applicationContext,"The directory does not exist or is not a directory.", Toast.LENGTH_SHORT).show()
        }
    }
}