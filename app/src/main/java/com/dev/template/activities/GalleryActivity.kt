package com.dev.template.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dev.template.R
import com.dev.template.databinding.ActivityGalleryBinding

class GalleryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityGalleryBinding
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gallery)

        binding.ivBack.setOnClickListener {
            finish()
        }
    }
}