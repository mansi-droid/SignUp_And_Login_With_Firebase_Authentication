package com.dev.template.activities

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dev.template.BuildConfig
import com.dev.template.R
import com.dev.template.databinding.ActivityDashboardBinding
import com.dev.template.databinding.LogoutLayoutBinding
import com.dev.template.utils.ApplicationUtil
import com.dev.template.utils.CONSTANTS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DashboardActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration : AppBarConfiguration
    private lateinit var binding : ActivityDashboardBinding
    private lateinit var database : DatabaseReference
    private var logoutDialog : Dialog? = null
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        database = Firebase.database.reference
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        setSupportActionBar(binding.appBarDashboard.toolbar)
        val navController = findNavController(R.id.nav_host_fragment_content_dashboard)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_profile, R.id.nav_gallery, R.id.nav_about_us, R.id.nav_plans, R.id.nav_contact_us, R.id.nav_logout
            ), binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        val headerView = binding.navView.getHeaderView(0)
        val name = headerView.findViewById<TextView>(R.id.tvName)
        val email = headerView.findViewById<TextView>(R.id.tvEmail)

        database.child(CONSTANTS.fireBaseDataBase).child(userId).get().addOnSuccessListener {
            name.text = it.child("name").value.toString()
            email.text = it.child("email").value.toString()
        }.addOnFailureListener {
        }

        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_profile -> startActivity(Intent(this, EditProfileActivity::class.java))
                R.id.nav_gallery -> startActivity(Intent(this, GalleryActivity::class.java))
                R.id.nav_about_us -> startActivity(Intent(this, AboutUsActivity::class.java))
                R.id.nav_plans -> startActivity(Intent(this, PlansActivity::class.java))
                R.id.nav_contact_us -> startActivity(Intent(this, ContactUsActivity::class.java))
                R.id.nav_rate_and_review -> rateAndReview()
                R.id.nav_logout -> logoutPopup()
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }
    }

    private fun logoutPopup() {
        if (ApplicationUtil.isInternetAvailable(this@DashboardActivity)) {
            logoutDialog = Dialog(this@DashboardActivity)
            logoutDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val bindingPopUp = DataBindingUtil.inflate<LogoutLayoutBinding>(
                layoutInflater, R.layout.logout_layout, null, false
            )
            logoutDialog!!.setContentView(bindingPopUp.root)
            logoutDialog!!.window!!.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )
            logoutDialog!!.window!!.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            logoutDialog!!.setOnKeyListener { _ : DialogInterface?, keyCode : Int, _ : KeyEvent? ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    logoutDialog!!.dismiss()
                    return@setOnKeyListener true
                }
                false
            }
            bindingPopUp.btnYes.setOnClickListener {
                Firebase.auth.signOut()
                Toast.makeText(this, getString(R.string.logout_successfully), Toast.LENGTH_SHORT)
                        .show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            bindingPopUp.btnNo.setOnClickListener {
                logoutDialog!!.dismiss()
            }
            logoutDialog!!.show()
            logoutDialog!!.setCancelable(false)
            logoutDialog!!.setCanceledOnTouchOutside(false)
        } else {
            Toast.makeText(this@DashboardActivity, getString(R.string.no_internet), Toast.LENGTH_SHORT)
                    .show()
        }
    }

    private fun rateAndReview() {
        val appPackageName = BuildConfig.APPLICATION_ID
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$appPackageName")
                )
            )
        } catch (error : ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                )
            )
        }
    }

    override fun onSupportNavigateUp() : Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_dashboard)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}