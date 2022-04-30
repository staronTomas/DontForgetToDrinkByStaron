package com.example.dont_forget_to_drink

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.dont_forget_to_drink.databinding.UserProfileActivityBinding


lateinit var binding: UserProfileActivityBinding
var myDialog: Dialog? = null
private lateinit var toggle : ActionBarDrawerToggle
private lateinit var sp : SharedPreferences

private lateinit var myTextView: TextView

class UserProfileActivity  : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)  // vypne v appke nocny rezim...
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_profile_activity)

        binding = UserProfileActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sp = getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE) // pomocou tohoto si ukladam do SharedPreferences udaje o pouzivatelovi


        loadDataToUserProfileActivity()

        myDialog = Dialog(this);
        val closeUserProfileBtn = findViewById<ImageButton>(R.id.profileBackArrowBtn)
        closeUserProfileBtn.setOnClickListener {
            onBtnClickSound()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }


    private fun loadDataToUserProfileActivity() {

        /*

         editor.putString("userName", nameStr)
                editor.putString("userSurname", surnameStr)
                editor.putString("userWeight", weightStr)
                editor.putString("userAge", ageStr)
                editor.putString("userGender", genderStr)
                editor.putString("dailyWaterIntake", dailyWaterIntakeStr)

         */

        val userName = sp.getString("userName", "")
        val userSurname = sp.getString("userSurname", "")
        val userWeight = sp.getString("userWeight", "")
        val userAge = sp.getString("userAge", "")
        val userGender = sp.getString("userGender","")
        val userWakeUpTime = sp.getString("wakeUpTime", "")
        val userSleepTime = sp.getString("sleepTime", "")


        var myTextView = binding.nameSurname
        val userFullNameStr : String = "$userName $userSurname"
        myTextView.text = userFullNameStr

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
}

