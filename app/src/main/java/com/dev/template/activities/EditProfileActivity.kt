package com.dev.template.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.dev.template.R
import com.dev.template.databinding.ActivityProfileEditBinding
import com.dev.template.utils.ApplicationUtil
import com.dev.template.utils.ApplicationUtil.Companion.isEmailValid
import com.dev.template.utils.CONSTANTS
import java.text.SimpleDateFormat
import java.util.Calendar

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProfileEditBinding
    private var mYear : Int = 0
    private var mMonth : Int = 0
    private var mDay : Int = 0
    private var birthYear = 0
    private var ageYear : Int = 0
    private var ageMonth : Int = 0
    private var ageDate : Int = 0
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_edit)
        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.etDob.setOnClickListener {
            if (ApplicationUtil.isInternetAvailable(this@EditProfileActivity)) {
                setDate()
            } else {
                Toast.makeText(this@EditProfileActivity, getString(R.string.no_internet), Toast.LENGTH_SHORT)
                        .show()
            }
        }
        binding.btnUpdate.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()
            val dob = binding.etDob.text.toString().trim()
            if (ApplicationUtil.isInternetAvailable(this@EditProfileActivity)) {
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
                    phone == "" -> {
                        binding.tlEmail.isErrorEnabled = false
                        binding.tlPhone.isErrorEnabled = true
                        binding.tlPhone.error = getString(R.string.please_enter_phone_number)
                    }
                    dob == "" -> {
                        binding.tlPhone.isErrorEnabled = false
                        binding.tlDob.isErrorEnabled = true
                        binding.tlDob.error = getString(R.string.please_enter_date_of_birth)
                    }
                    else -> {
                        binding.tlDob.isErrorEnabled = false
                        Toast.makeText(this@EditProfileActivity, getString(R.string.profile_detail_update_successfully), Toast.LENGTH_SHORT)
                                .show()
                        finish()
                    }
                }
            } else {
                Toast.makeText(this@EditProfileActivity, getString(R.string.no_internet), Toast.LENGTH_SHORT)
                        .show()
            }
        }
    }

    fun setDate() {
        val c = Calendar.getInstance()
        mYear = c[Calendar.YEAR]
        mMonth = c[Calendar.MONTH]
        mDay = c[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(
            this, R.style.DialogTheme,
            { view : DatePicker, year : Int, monthOfYear : Int, dayOfMonth : Int ->
                view.minDate = System.currentTimeMillis() - 1000
                val cal = Calendar.getInstance()
                cal.timeInMillis
                cal[year, monthOfYear] = dayOfMonth
                val date = cal.time
                val sdf = SimpleDateFormat(CONSTANTS.DATE_MONTH_YEAR_FORMAT)
                val strDate = sdf.format(date)
                binding.etDob.setText(strDate)
                ageYear = year
                ageMonth = monthOfYear
                ageDate = dayOfMonth
                birthYear = getAge(ageYear, ageMonth, ageDate)
                if (birthYear < 18) {
                    binding.etDob.isFocusable = true
                    binding.etDob.requestFocus()
                    binding.tlDob.isErrorEnabled = true
                    binding.tlDob.error = getString(R.string.check_dob)
                } else {
                    binding.tlDob.isErrorEnabled = false
                }
            }, mYear, mMonth, mDay
        )
        datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis
        datePickerDialog.show()
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.primaryColor))
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.primaryColor))
    }

    private fun getAge(year : Int, month : Int, day : Int) : Int {
        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()
        dob[year, month] = day
        var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
        if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
            age--
        }
        return age
    }
}