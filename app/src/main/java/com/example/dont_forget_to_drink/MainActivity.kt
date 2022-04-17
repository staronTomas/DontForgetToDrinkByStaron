package com.example.dont_forget_to_drink

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import android.media.MediaPlayer
import android.view.View

class MainActivity : AppCompatActivity() {

    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var sp : SharedPreferences
    private lateinit var userName: TextView
    private var mMediaPlayer: MediaPlayer? = null

    private var asdas: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        sp = getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE)
        playSound()


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






    // MUSIC PLAYER


    fun playSound() {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(this, R.raw.background_music)
            mMediaPlayer!!.isLooping = true
            mMediaPlayer!!.start()
        } else mMediaPlayer!!.start()
    }

    // 2. Pause playback
    fun pauseSound() {
        if (mMediaPlayer?.isPlaying == true) mMediaPlayer?.pause()
    }

    // 3. Stops playback
    fun stopSound() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.stop()
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }

    // 4. Destroys the MediaPlayer instance when the app is closed
    override fun onStop() {
        super.onStop()
        if (mMediaPlayer != null) {
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }
}