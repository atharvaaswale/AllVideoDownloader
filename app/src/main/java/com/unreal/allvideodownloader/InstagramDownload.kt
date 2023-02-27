package com.unreal.allvideodownloader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.unreal.allvideodownloader.databinding.ActivityInstagramDownloadBinding
import com.unreal.allvideodownloader.utils.Downloaders

class InstagramDownload : AppCompatActivity() {
    lateinit var binding: ActivityInstagramDownloadBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_instagram_download)
        binding.back.setOnClickListener { finish() }

        binding.btnDownload.setOnClickListener {
            Downloaders.downloadIGVideo(applicationContext, binding.etLink.text.toString())
        }

        binding.etLink.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                binding.webview.loadUrl(binding.etLink.text.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
        })
    }
}