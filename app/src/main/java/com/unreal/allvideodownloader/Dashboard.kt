package com.unreal.allvideodownloader

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.ashudevs.facebookurlextractor.FacebookExtractor
import com.ashudevs.facebookurlextractor.FacebookFile
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.unreal.allvideodownloader.adapters.DashboardAdapter
import com.unreal.allvideodownloader.databinding.ActivityDashboardBinding
import com.unreal.allvideodownloader.model.DashBoardApps
import com.unreal.allvideodownloader.utils.Downloaders
import com.unreal.allvideodownloader.utils.Downloaders.Companion.download
import com.unreal.allvideodownloader.utils.Downloaders.Companion.downloadIGVideo
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.net.URL


class Dashboard : AppCompatActivity() {
    lateinit var binding: ActivityDashboardBinding
    var appList = ArrayList<DashBoardApps>()
    var iconList = arrayOf(
        R.drawable.ic_youtube,
        R.drawable.ic_whatsapp,
        R.drawable.ic_instagram,
        R.drawable.ic_facebook,
        R.drawable.ic_pinterest,
        R.drawable.ic_tiktok,
        R.drawable.ic_tumblr,
        R.drawable.ic_imdb,
        R.drawable.ic_reddit,
        R.drawable.ic_vimeo,
        R.drawable.ic_linkedin,
        R.drawable.ic_metacafe,
        R.drawable.ic_twitter,
        R.drawable.ic_dailymotion,
        R.drawable.ic_flicker
    )
    var appNameList = arrayOf(
        "Youtube", "Whatsapp", "Instagram", "Facebook",
        "Pinterest", "Tiktok", "Tumblr", "IMDb",
        "Reddit", "Vimeo", "LinkedIn", "Metacafe",
        "Twitter", "Dailymotion", "Flickr", "Pinterest"
    )

    val iglink = "https://www.instagram.com/p/CoiOScpsGg_/"
    val fblink = "https://www.facebook.com/reel/877041193406266?s=yWDuG2&fs=e&mibextid=Nif5oz"

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
        val background: Drawable = this.getResources().getDrawable(R.drawable.dashboard_gradient_bg)
        window.setBackgroundDrawable(background)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)


        isAllPermissionGranted()
        binding.downloads.setOnClickListener {
            startActivity(
                Intent(applicationContext, DownloadsFolder::class.java)
            )
        }

        binding.menus.setOnClickListener {
            val popupMenu: PopupMenu = PopupMenu(this, binding.menus)
            popupMenu.menuInflater.inflate(R.menu.dashboard_menus,popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when(item.itemId) {
                    R.id.exit ->
                        showExitDialog()
                }
                true
            })
            popupMenu.show()
        }


        when {
            intent?.action == Intent.ACTION_SEND -> {
                if ("text/plain" == intent.type) {
                    handleSendText(intent) // Handle text being sent
                }
            }
            else -> {
                // Handle other intents, such as being started from the home screen
            }
        }

        object : FacebookExtractor(this, iglink, false) {
            override fun onExtractionComplete(facebookFile: FacebookFile) {
                Log.e("TAG", "---------------------------------------")
                Log.e("TAG", "facebookFile AutherName :: " + facebookFile.author)
                Log.e("TAG", "facebookFile FileName :: " + facebookFile.filename)
                Log.e("TAG", "facebookFile Ext :: " + facebookFile.ext)
                Log.e("TAG", "facebookFile SD :: " + facebookFile.sdUrl)
                Log.e("TAG", "facebookFile HD :: " + facebookFile.hdUrl)
                Log.e("TAG", "---------------------------------------")
                download(facebookFile.hdUrl, applicationContext, System.currentTimeMillis().toString() + ".mp4")
            }

            override fun onExtractionFail(error: Exception) {
                Log.e("Error", "Error Facebook:: " + error.message)
                error.printStackTrace()
            }
        }


        BottomSheetBehavior.from(binding.sheet).apply {
//            peekHeight = 1600
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        for (i in iconList.indices) {
            appList.add(DashBoardApps(iconList[i], appNameList[i]))
        }
        binding.rv.adapter = DashboardAdapter(appList)
        binding.rv.layoutManager = GridLayoutManager(applicationContext, 4)


        binding.etSearchbar.setOnEditorActionListener{ v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                startActivity(
                    Intent(applicationContext, MainBrowser::class.java)
                        .putExtra("flag", "GL")
                        .putExtra("googleQuery", "${binding.etSearchbar.text}")
                )
                true
            } else {
                false
            }
        }


        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    // Respond to navigation item 1 click
                    true
                }
                R.id.downloads -> {
                    true
                }
                else -> false
            }
        }


    }


    private fun handleSendText(intent: Intent) {
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
            downloadIGVideo(
                applicationContext,
                it
            )
        }
    }

    fun showExitDialog(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.exit_dialog)
        val btnNo = dialog.findViewById<Button>(R.id.cancel)
        val btnYes = dialog.findViewById<Button>(R.id.exit)
        btnNo.setOnClickListener {dialog.dismiss()}
        btnYes.setOnClickListener {
            dialog.dismiss()
            finish()
        }
        dialog.show()
    }

    fun getFbData(){
        val url = URL("https://fb.watch/iEFv7I5YlX/")
        var host = url.host
        if (host.contains("facebook.com") || host.contains("fb.watch")){
            CallGetFBData().execute(fblink)
        }else{
            Toast.makeText(applicationContext, "Invalid Link!", Toast.LENGTH_SHORT).show()
        }
    }

    inner class CallGetFBData: AsyncTask<String, Void, Document>() {
        var fbDocument: Document? = null
        override fun doInBackground(vararg p0: String?): Document {
            try {
                fbDocument = Jsoup.connect(p0[0]).get()
            } catch (e: IOException){
                e.printStackTrace()
            }
            return  fbDocument!!
        }

        override fun onPostExecute(result: Document?) {
            val videoUrl = result!!.select("meta[property=\"og:video\"]")
                .last().attr("content")
            if (!videoUrl.equals("")){
                Downloaders.download(videoUrl, applicationContext, System.currentTimeMillis().toString())
            }
        }

    }

    override fun onBackPressed() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.exit_dialog)
        val btnNo = dialog.findViewById<Button>(R.id.cancel)
        val btnYes = dialog.findViewById<Button>(R.id.exit)
        btnNo.setOnClickListener {dialog.dismiss()}
        btnYes.setOnClickListener {
            dialog.dismiss()
            finish()
        }
        dialog.show()
    }
    @RequiresApi(Build.VERSION_CODES.R)
    fun isAllPermissionGranted(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED  || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                84
            )
            false
        } else { true }
    }

}