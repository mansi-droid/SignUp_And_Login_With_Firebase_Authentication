package com.dev.template.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dev.template.R
import com.dev.template.databinding.ActivityContactUsBinding
import com.dev.template.utils.ApplicationUtil
import com.dev.template.utils.ApplicationUtil.Companion.isEmailValid

class ContactUsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityContactUsBinding
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us)

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.btnSendMsg.setOnClickListener {
            submitContactUs()
        }
    }

    private fun submitContactUs() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val subject = binding.etSubject.text.toString().trim()
        val message = binding.etMessage.text.toString().trim()
        if (ApplicationUtil.isInternetAvailable(this@ContactUsActivity)) {
            when {
                name == "" -> {
                    binding.tlName.isErrorEnabled = true
                    binding.tlName.error = getString(R.string.please_enter_name)
                }
                email == "" -> {
                    binding.tlName.isErrorEnabled = false
                    binding.tlEmail.isErrorEnabled = true
                    binding.tlEmail.error = getString(R.string.please_enter_email_address)
                }
                !email.isEmailValid() -> {
                    binding.tlName.isErrorEnabled = false
                    binding.tlEmail.isErrorEnabled = true
                    binding.tlEmail.error = getString(R.string.please_enter_valid_email_address)
                }
                subject == "" -> {
                    binding.tlEmail.isErrorEnabled = false
                    binding.tlSubject.isErrorEnabled = true
                    binding.tlSubject.error = getString(R.string.please_enter_subject)
                }
                message == "" -> {
                    binding.tlSubject.isErrorEnabled = false
                    binding.tlMessage.isErrorEnabled = true
                    binding.tlMessage.error = getString(R.string.please_enter_message)
                }
                else -> {
                    binding.tlMessage.isErrorEnabled = false
                    Toast.makeText(this@ContactUsActivity, getString(R.string.contact_us_successfully), Toast.LENGTH_SHORT)
                            .show()
                    finish()
                }
            }
        } else {
            Toast.makeText(this@ContactUsActivity, getString(R.string.no_internet), Toast.LENGTH_SHORT)
                    .show()
        }
    }
}