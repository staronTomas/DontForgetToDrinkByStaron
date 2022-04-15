package com.example.dont_forget_to_drink

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var sp : SharedPreferences
    private lateinit var userName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sp = getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE)



        val firstRun = sp.getBoolean("firstRun", true)
        if (firstRun) {
            //... Display the dialog message here ...
            // Save the state
            getSharedPreferences("MyUserPrefs", MODE_PRIVATE)
                .edit()
                .putBoolean("firstRun", false)
                .commit()
            showFirstStartWindow()
        }



        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        userName = findViewById(R.id.titleBarName)


        val userNameSP = sp.getString("userName", "");


        userName.text = userNameSP

        navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_profile -> Toast.makeText(applicationContext, "Clicked Profile", Toast.LENGTH_SHORT).show()
                R.id.nav_statistics -> Toast.makeText(applicationContext, "Clicked Statistics", Toast.LENGTH_SHORT).show()
                R.id.nav_disable_alarm -> Toast.makeText(applicationContext, "Clicked Disable Alarm", Toast.LENGTH_SHORT).show()
                R.id.nav_settings -> Toast.makeText(applicationContext, "Clicked Settings", Toast.LENGTH_SHORT).show()
                R.id.nav_share -> Toast.makeText(applicationContext, "Clicked Share", Toast.LENGTH_SHORT).show()
                R.id.nav_rate_us -> Toast.makeText(applicationContext, "Clicked Rate us", Toast.LENGTH_SHORT).show()

            }

            true
        }




    }

    private fun showFirstStartWindow() {
        val intent = Intent(this, FirstTimeStartedActivity::class.java)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}