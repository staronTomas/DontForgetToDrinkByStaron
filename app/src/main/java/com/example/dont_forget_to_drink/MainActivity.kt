package com.example.dont_forget_to_drink

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var sp : SharedPreferences
    private lateinit var userName: TextView
    private var mMediaPlayer: MediaPlayer? = null

    var myDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) // vypne v appke nocny rezim...
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sp = getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE) // pomocou tohoto si ukladam do SharedPreferences udaje o pouzivatelovi

        playSound() // zapne mi hudbu

        isStartedFirstTime() // tato metoda sa pozrie nato či pouzivatel uz mal zapnutu appku a ak nie tak vyplni zakladne udaje o sebe pre chod aplikacie

        ShowSideMenu() // metoda ktora sa stara o chod bocneho menu




    // KOD pre zmenu cup size

        myDialog = Dialog(this);

        val btn_click_me = findViewById(R.id.changeCupSizeButton) as Button
        btn_click_me.setOnClickListener {
            showChangeSizePopUp(this)
        }





    }


    fun isStartedFirstTime() {
        val firstRun = sp.getBoolean("firstRun", true)
        if (firstRun) {
            //... Display the dialog message here ...
            // Save the state
            val intent = Intent(this, FirstTimeStartedActivity::class.java)
            startActivity(intent)
        }
    }


    fun ShowSideMenu() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        userName = findViewById(R.id.titleBarName)


        val userNameSP = sp.getString("dailyWaterIntake", "");


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




    fun showChangeSizePopUp(v: MainActivity) {
        val txtclose: TextView
        myDialog?.setContentView(R.layout.change_cup_size_pop_up)
        txtclose = myDialog?.findViewById(R.id.txtclose)!!
        txtclose.text = "X"
        txtclose.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                myDialog!!.dismiss()
            }
        })
        myDialog?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog?.show()



        // get reference to ImageView
        val littleCupImgBtn : ImageButton? = myDialog?.findViewById(R.id.littleCupSizeImgBtn)
        // set on-click listener for ImageView

        littleCupImgBtn?.setOnClickListener {
            Toast.makeText(this@MainActivity, "You clicked on ImageView.", Toast.LENGTH_SHORT).show()
        }

        val mediumCupImgBtn : ImageButton? = myDialog?.findViewById(R.id.mediumCupSizeImgBtn)
        // set on-click listener for ImageView

        mediumCupImgBtn?.setOnClickListener {
            Toast.makeText(this@MainActivity, "You clicked on ImageView.", Toast.LENGTH_SHORT).show()
        }

        val largeCupImgBtn : ImageButton? = myDialog?.findViewById(R.id.largeCupSizeImgBtn)
        // set on-click listener for ImageView

        largeCupImgBtn?.setOnClickListener {
            Toast.makeText(this@MainActivity, "You clicked on ImageView.", Toast.LENGTH_SHORT).show()
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }





    // Ked sa mi znova zapne tato aktivita

    override fun onRestart() {
        super.onRestart()
        finish()
        startActivity(intent)
    }

    // ked stlaci clovek back Button na activity main, tak sa ho to opyta ci chce naozaj skoncit aplikaciu.
    override fun onBackPressed() {


        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Exit")
        builder.setMessage("Do you really want to exit the app?")
        //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton("Yes") { dialog, which ->
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        builder.setNegativeButton("No") { dialog, which ->
        }
        builder.show()

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

    // ked znova spustim appku tak sa mi nanovo spusti hudba
    override fun onResume() {
        super.onResume()
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(this, R.raw.background_music)
            mMediaPlayer!!.isLooping = true
            mMediaPlayer!!.start()
        } else mMediaPlayer!!.start()
    }
}