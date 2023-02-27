package com.unreal.allvideodownloader

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import com.unreal.allvideodownloader.utils.Downloaders

class InstagramDownloader : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instagram_downloader)

        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
            Downloaders.downloadIGVideo(
                applicationContext,
                it
            )
        }
    }
}