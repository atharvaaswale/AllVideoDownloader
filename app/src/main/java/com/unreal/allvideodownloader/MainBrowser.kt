package com.unreal.allvideodownloader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.unreal.allvideodownloader.databinding.ActivityMainBrowserBinding

class MainBrowser : AppCompatActivity() {
    lateinit var binding: ActivityMainBrowserBinding
    var flag : String?= ""
    var googleSearchQuery: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_browser)
        binding.webview.settings.javaScriptEnabled = true
        this.binding.webview.settings.domStorageEnabled = true
        binding.webview.canGoBack()
        flag = intent.getStringExtra("flag")!!


        if (flag.contentEquals("IG")){
            binding.webview.loadUrl("https://www.instagram.com/")
        } else if(flag.contentEquals("FB")){
            binding.webview.loadUrl("https://www.facebook.com/")
        } else if(flag.contentEquals("YT")){
            binding.webview.loadUrl("https://www.youtube.com/")
        } else if(flag.contentEquals("GL")){
            googleSearchQuery = intent.getStringExtra("googleQuery")!!
            if (googleSearchQuery!!.contains(".com") ||
                googleSearchQuery!!.contains("https://www.") ||
                googleSearchQuery!!.contains("www.")){
                binding.webview.loadUrl(googleSearchQuery!!)
            }else{
                binding.webview.loadUrl("https://www.google.com/search?q=$googleSearchQuery/")
            }

        }

    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}