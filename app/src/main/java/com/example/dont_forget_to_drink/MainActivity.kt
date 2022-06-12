package com.example.dont_forget_to_drink

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

import androidx.drawerlayout.widget.DrawerLayout
import androidx.work.*
import com.example.dont_forget_to_drink.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var sp : SharedPreferences
    private lateinit var myTextView: TextView
    private var mMediaPlayer: MediaPlayer? = null

    lateinit var binding: ActivityMainBinding

    var myDialog: Dialog? = null


    // zakladna onCreate metoda
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) // vypne v appke nocny rezim...
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        sp = getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE) // pomocou tohoto si ukladam do SharedPreferences udaje o pouzivatelovi

        playSound() // zapne mi hudbu

        isStartedFirstTime() // tato metoda sa pozrie nato či pouzivatel uz mal zapnutu appku a ak nie tak vyplni zakladne udaje o sebe pre chod aplikacie

        showSideMenu() // metoda ktora sa stara o chod bocneho menu

        loadDataToMainActivity()



    // KOD pre zmenu cup size
        myDialog = Dialog(this);
        val btn_click_me = binding.changeCupSizeButton as Button
        btn_click_me.setOnClickListener {
            onBtnClickSound()
            showChangeSizePopUp(this)
        }

        myDialog = Dialog(this);
        val btnAddDrank = binding.confirmDrinkButton as ImageButton

        btnAddDrank.setOnClickListener {
            onBtnClickSound()
            increaseDrankWater(this)
        }


        myWorkManagerFun()

    }


    // metoda ktorou spustam WorkManagera a ako casto ma vykonat danu cinnost
    private fun myWorkManagerFun() {
        val constraints = Constraints.Builder()
            .setRequiresCharging(false)
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresCharging(false)
            .setRequiresBatteryNotLow(false)
            .build()

        val myRequest = PeriodicWorkRequest.Builder(
            MyWorkManager::class.java,
            60,
            TimeUnit.MINUTES
        ).setConstraints(constraints)
            .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "my_id",
                ExistingPeriodicWorkPolicy.KEEP,
                myRequest
            )
    }




    // metoda ktorou navysim pocet vypitej vody v dany den
    @RequiresApi(Build.VERSION_CODES.N)
    private fun increaseDrankWater(mainActivity: MainActivity) {

        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Confirm increase of water drank")
        builder.setMessage("Are you sure?")

        builder.setPositiveButton("Yes") { dialog, which ->
            onConfirmSound()

            var editor: SharedPreferences.Editor = sp.edit()

            var cupSizeStr = sp.getString("cupSize", "")?.dropLast(2).toString()
            var alreadyDrankStr = sp.getString("todayDrank", "")

            var newDrankWaterAmount = (cupSizeStr.toInt() + (alreadyDrankStr?.toInt()!!)).toString()

            editor.putString("todayDrank", newDrankWaterAmount)
            editor.commit()

            loadDataToMainActivity()

            val waterDrank = sp.getString("todayDrank", "")?.toInt()
            val dailyWaterIntake = sp.getString("dailyWaterIntake", "")?.toInt()

            if(alreadyDrankStr.toInt() < dailyWaterIntake!!) {
                if (waterDrank != null) {
                    if (waterDrank >= dailyWaterIntake!!) {
                        Toast.makeText(
                            applicationContext,
                            sp.getString("dailyGoalCompleted", ""),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        showFragmentAfterCompletion(this)
                    }
                }
            }
        }
        builder.setNegativeButton("No") { dialog, which ->
            onBtnClickSound()
        }
        builder.show()
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun showFragmentAfterCompletion(v: MainActivity) {
        onWinSound()
        myDialog?.setContentView(R.layout.water_amount_drank)
        val txtClose: TextView = myDialog?.findViewById(R.id.txtcloseCompleted)!!
        txtClose.text = "X"
        txtClose.setOnClickListener { myDialog!!.dismiss() }
        myDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog?.show()


        val changeCupSizeBtn : Button? = myDialog?.findViewById(R.id.continueAfterCompletedWater)
        // set on-click listener for ImageView
        changeCupSizeBtn?.setOnClickListener {
            onBtnClickSound()
            myDialog!!.dismiss()
        }
    }





    // metoda ktorou zvysim hodnotu progressBaru
    @RequiresApi(Build.VERSION_CODES.N)
    private fun increaseProgressBar() {


        var progressBar = binding.waterProgressBar

        if (progressBar.progress >= 100) {
            return
        }

        var alreadyDrank = sp.getString("todayDrank", "")?.toInt()
        var dailyWaterIntake = sp.getString("dailyWaterIntake", "")?.toInt()

        var newPercentageForProgressBar = alreadyDrank!! * 100 / dailyWaterIntake!!

        progressBar.setProgress(newPercentageForProgressBar, true)
        progressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));


    }


    // nacitanie starych dat do aktivity
    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadDataToMainActivity() {

        val waterNeedToDrink= sp.getString("dailyWaterIntake", "")
        val waterDrank = sp.getString("todayDrank", "")

        var cupSize  = sp.getString("cupSize", "")

        myTextView = binding.waterDrankTextView
        myTextView.text = waterDrank + " / " + waterNeedToDrink + " ml"

        myTextView = binding.cupSizeTextView
        myTextView.text = cupSize

        if (cupSize != null) {
            cupSize = cupSize.dropLast(3) + "0"
        }

        var cupSizeInt = 0
        if (cupSize != null) {
            cupSizeInt = cupSize.toInt()
        }

        when {
            cupSizeInt >= 500 -> {
                myTextView.setBackgroundColor(Color.parseColor("#051d40"))
            }
            cupSizeInt >= 200 -> {
                myTextView.setBackgroundColor(Color.parseColor("#1f628d"))
            }
            else -> {
                myTextView.setBackgroundColor(Color.parseColor("#56aeff"))
            }
        }

        increaseProgressBar()


    }



    // metoda ktorou pri zapnuti overujem najskor ci je aplikacia zapnuta po prvy krat a treba nacitat data o pouzivatelovi
    private fun isStartedFirstTime() {

        welcomeSound()

        val firstRun = sp.getBoolean("firstRun", true)
        if (firstRun) {

            val editor: SharedPreferences.Editor = sp.edit()
            editor.putString("cupSize", "100ml")
            editor.putString("todayDrank", "0")
            editor.putString("dailyWaterIntake", "1")

            editor.apply()

            //... Display the dialog message here ...
            // Save the state
            val intent = Intent(this, FirstTimeStartedActivity::class.java)
            startActivity(intent)

        }
    }


    // metoda ktora sfunkcnuje bocne menu
    @RequiresApi(Build.VERSION_CODES.N)
    private fun showSideMenu() {

        onSwipeSound()

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_profile ->{
                    onBtnClickSound()
                    val intent = Intent(this, UserProfileActivity::class.java)
                    startActivity(intent)
                    }
                R.id.nav_statistics -> {
                    onBtnClickSound()
                    Toast.makeText(applicationContext, "Sorry, not working yet", Toast.LENGTH_SHORT)
                        .show()
                }
                R.id.nav_disable_alarm -> {
                    onBtnClickSound()
                    Toast.makeText(applicationContext, "Sorry, not working yet", Toast.LENGTH_SHORT)
                        .show()
                }
                R.id.nav_reset_water -> {
                    onBtnClickSound()

                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Warning !")
                    builder.setMessage("Do you really want to restart the counting?")
                    //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

                    builder.setPositiveButton("Okay, reset water counter") { dialog, which ->
                        var editor: SharedPreferences.Editor = sp.edit()
                        editor.putString("todayDrank", "0")
                        editor.commit()
                        loadDataToMainActivity()
                        Toast.makeText(applicationContext, "Water amount was Reseted", Toast.LENGTH_SHORT)
                            .show()
                        loadDataToMainActivity()
                    }
                    builder.setNegativeButton("Cancel restart.") { dialog, which ->
                        Toast.makeText(applicationContext, "Water restart was CANCELED.", Toast.LENGTH_SHORT)
                            .show()
                    }
                    builder.show()


                    var progressBar = binding.waterProgressBar
                    progressBar.setProgress(0, true)
                    progressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
                }
                R.id.nav_settings -> {
                    onBtnClickSound()
                    Toast.makeText(applicationContext, "Sorry, not working yet", Toast.LENGTH_SHORT)
                        .show()
                }
                R.id.nav_share -> {
                    onBtnClickSound()
                    Toast.makeText(applicationContext, "Sorry, not working yet", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_rate_us -> {
                    onBtnClickSound()
                    Toast.makeText(applicationContext, "Sorry, not working yet", Toast.LENGTH_SHORT).show()
                }

            }
            true
        }
    }


    // metoda ktora mi umozni zobrazovať zmenenie velkosti pohara
    @RequiresApi(Build.VERSION_CODES.N)
    private fun showChangeSizePopUp(v: MainActivity) {
        myDialog?.setContentView(R.layout.change_cup_size_pop_up)
        val txtClose: TextView = myDialog?.findViewById(R.id.txtclose)!!
        txtClose.text = "X"
        txtClose.setOnClickListener { myDialog!!.dismiss() }
        myDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog?.show()



        val littleCupImgBtn : ImageButton? = myDialog?.findViewById(R.id.littleCupSizeImgBtn)
        littleCupImgBtn?.setOnClickListener {

            onConfirmSound()
            var cupSize = "100ml";
            var editor: SharedPreferences.Editor = sp.edit()
            editor.putString("cupSize", cupSize)
            editor.commit()

            loadDataToMainActivity()

            Toast.makeText(applicationContext, "Cup size was set to 100ml. :)", Toast.LENGTH_SHORT).show()
            myDialog!!.dismiss()
        }

        val mediumCupImgBtn : ImageButton? = myDialog?.findViewById(R.id.mediumCupSizeImgBtn)
        mediumCupImgBtn?.setOnClickListener {

            onConfirmSound()
            var cupSize = "200ml";
            var editor: SharedPreferences.Editor = sp.edit()
            editor.putString("cupSize", cupSize)
            editor.commit()

            loadDataToMainActivity()

            Toast.makeText(applicationContext, "Cup size was set to 200ml. :)", Toast.LENGTH_SHORT).show()
            myDialog!!.dismiss()
        }

        val largeCupImgBtn : ImageButton? = myDialog?.findViewById(R.id.largeCupSizeImgBtn)
        // set on-click listener for ImageView
        largeCupImgBtn?.setOnClickListener {

            onConfirmSound()
            var cupSize = "500ml";
            var editor: SharedPreferences.Editor = sp.edit()
            editor.putString("cupSize", cupSize)
            editor.commit()

            loadDataToMainActivity()

            Toast.makeText(applicationContext, "Cup size was set to 500ml. :)", Toast.LENGTH_SHORT).show()
            myDialog!!.dismiss()
        }



        val changeCupSizeBtn : Button? = myDialog?.findViewById(R.id.ownCupSizeConfirmBtn)
        // set on-click listener for ImageView
        changeCupSizeBtn?.setOnClickListener {

            onConfirmSound()
            val ownSize : EditText? = myDialog?.findViewById(R.id.usersOwnCupSizeEditText)
            val ownSizeStr = ownSize?.text.toString()
            if (ownSizeStr.length > 0) {
                if(ownSizeStr.toInt() > 0) {
                    var cupSize = ownSizeStr + "ml";

                    var editor: SharedPreferences.Editor = sp.edit()
                    editor.putString("cupSize", cupSize)
                    editor.commit()

                    loadDataToMainActivity()


                    Toast.makeText(applicationContext, "Cup size was set to your own size. :)", Toast.LENGTH_SHORT).show()
                    myDialog!!.dismiss()
                } else {
                    Toast.makeText(applicationContext, "The amount has to be greater than 0.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext, "The amount value can't be empty.", Toast.LENGTH_SHORT).show()
            }



        }
    }


    // onOptions metoda
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    // Ked sa mi znova zapne tato aktivita

    // Restart metoda
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
            onCloseSound()
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


    private fun playSound() {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(this, R.raw.background_music)
            mMediaPlayer!!.isLooping = true
            mMediaPlayer!!.start()
        } else mMediaPlayer!!.start()
    }

    // 2. Pause playback
    private fun pauseSound() {
        if (mMediaPlayer?.isPlaying == true) mMediaPlayer?.pause()
    }

    // 3. Stops playback
    private fun stopSound() {
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


    private fun welcomeSound() {
        val buttonClickSound: MediaPlayer = MediaPlayer.create(this, R.raw.welcome_sound)
        buttonClickSound.start()
    }

    private fun onConfirmSound() {
        val buttonClickSound: MediaPlayer = MediaPlayer.create(this, R.raw.confirm_sound)
        buttonClickSound.start()
    }

    private fun onBtnClickSound()  {
        val buttonClickSound: MediaPlayer = MediaPlayer.create(this, R.raw.btn_click_sound)
        buttonClickSound.start()
    }

    private fun onSwipeSound() {
        val buttonClickSound: MediaPlayer = MediaPlayer.create(this, R.raw.swipe_sound)
        buttonClickSound.start()
    }

    private fun onCloseSound() {
        val buttonClickSound: MediaPlayer = MediaPlayer.create(this, R.raw.close_sound_effect)
        buttonClickSound.start()
    }

    private fun onWinSound() {
        val buttonClickSound: MediaPlayer = MediaPlayer.create(this, R.raw.win_sound_effect)
        buttonClickSound.start()
    }


}
