package com.hadi.vp2coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hadi.vp2coroutines.databinding.ActivityMainBinding
import com.hadi.vp2coroutines.databinding.ActivityUrlImagesBinding

class UrlImagesActivity : AppCompatActivity(R.layout.activity_url_images) {

    private lateinit var binding: ActivityUrlImagesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_url_images)
        binding = ActivityUrlImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}