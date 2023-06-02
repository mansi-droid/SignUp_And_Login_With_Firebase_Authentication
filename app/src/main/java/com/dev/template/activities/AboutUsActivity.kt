package com.dev.template.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dev.template.R
import com.dev.template.databinding.ActivityAboutUsBinding
class AboutUsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAboutUsBinding
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about_us)

        binding.ivBack.setOnClickListener {
            finish()
        }
    }
}