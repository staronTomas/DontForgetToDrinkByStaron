package com.example.dont_forget_to_drink

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.dont_forget_to_drink.databinding.UserProfileActivityBinding


lateinit var binding: UserProfileActivityBinding
var myDialog: Dialog? = null
private lateinit var sp : SharedPreferences

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

        val editBtn = binding.editProfileButton as Button
        editBtn.setOnClickListener {
            onBtnClickSound()
            editProfileOnCLick()
        }

        val delBtn = binding.deleteProfileButton as Button
        delBtn.setOnClickListener {
            onBtnClickSound()
            deleteProfileOnClick()
        }




    }

    private fun editProfileOnCLick() {
        onBtnClickSound()
        val intent = Intent(this, EditProfile::class.java)
        startActivity(intent)
    }


    private fun deleteProfileOnClick() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Warning !!!")
        builder.setMessage("All of your data will be permanently lost!")

        builder.setPositiveButton("Yes, Delete all data") { dialog, which ->
            sp.edit().clear().commit()
            super.onRestart()
            finish()
        }
        builder.setNegativeButton("No, cancel") { dialog, which ->
            Toast.makeText(applicationContext,
                "Thank you for keeping your data safe. :)", Toast.LENGTH_SHORT).show()
        }
        builder.show()
    }


    private fun loadDataToUserProfileActivity() {


        val userName = sp.getString("userName", "")
        val userSurname = sp.getString("userSurname", "")
        val userWeight = sp.getString("userWeight", "")
        val userAge = sp.getString("userAge", "")
        val userGender = sp.getString("userGender","")
        val userWakeUpTime = sp.getString("wakeUpTime", "")
        val userSleepTime = sp.getString("sleepTime", "")
        val dailyWaterAmount = sp.getString("dailyWaterIntake", "")


        var myTextView = binding.nameSurname
        var myText : String = "$userName $userSurname"
        myTextView.text = myText

        myTextView = binding.nameTextView
        myText = "$userName"
        myTextView.text = myText

        myTextView = binding.surnameTxtView
        myText = "$userSurname"
        myTextView.text = myText


        myTextView = binding.ageTextView
        myText = "$userAge"
        myTextView.text = myText

        myTextView = binding.weightTextView
        myText = "$userWeight kg"
        myTextView.text = myText

        myTextView = binding.genderTextView
        myText = "$userGender"
        myTextView.text = myText

        myTextView = binding.wakeUpTimeTextView
        myText = "$userWakeUpTime" + ":00"
        myTextView.text = myText

        myTextView = binding.sleepTimeTextView
        myText = "$userSleepTime" + ":00"
        myTextView.text = myText

        myTextView = binding.dailyAmountOfWaterTextView
        myText = "$dailyWaterAmount" + " ml"
        myTextView.text = myText


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


