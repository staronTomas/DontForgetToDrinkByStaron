package com.example.dont_forget_to_drink

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate


private lateinit var name : EditText
private lateinit var surname : EditText
private lateinit var weight: EditText
private lateinit var age: EditText

private lateinit var genderStr: String

private lateinit var wakeUpTime: EditText
private lateinit var sleepTime: EditText

private lateinit var sp : SharedPreferences




class EditProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        sp = getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE)


        loadOldData()

    }

    private fun loadOldData() {
        Toast.makeText(applicationContext, "Cusdfgdsfg )", Toast.LENGTH_SHORT).show()
        val userName = sp.getString("userName", "")
        val userSurname = sp.getString("userSurname", "")
        val userWeight = sp.getString("userWeight", "")
        val userAge = sp.getString("userAge", "")
        val userGender = sp.getString("userGender", "")
        val userWakeUpTime = sp.getString("wakeUpTime", "")
        val userSleepTime = sp.getString("sleepTime", "")
        val dailyWaterAmount = sp.getString("dailyWaterIntake", "")

        var myEditText = findViewById<EditText>(R.id.editName)
        var myText = "$userName"
        myEditText.hint = myText

        myEditText = findViewById<EditText>(R.id.editSurname)
        myText = "$userSurname"
        myEditText.hint = myText


        myEditText = findViewById<EditText>(R.id.editAge)
        myText = "$userAge"
        myEditText.hint = myText

        myEditText = findViewById<EditText>(R.id.editWeight)
        myText = "$userWeight kg"
        myEditText.hint = myText

        if (userGender.equals("man")) {
            var radioButton: RadioButton = findViewById<RadioButton>(R.id.radio_man)
            radioButton.isChecked = true;
        } else {
            var radioButton: RadioButton = findViewById<RadioButton>(R.id.radio_woman)
            radioButton.isChecked = true;
        }

        myEditText = findViewById<EditText>(R.id.editWakeUpTime)
        myText = "$userWakeUpTime:00"
        myEditText.hint = myText

        myEditText = findViewById<EditText>(R.id.editSleepTime)
        myText = "$userSleepTime :00"
        myEditText.hint = myText

        var myTextView : TextView = findViewById<TextView>(R.id.dailyAmountOfWaterTextView)
        var myText2 = "$dailyWaterAmount ml"
        myTextView.text = myText2


    }


    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_woman ->
                    if (checked) {
                        onBtnClickSound()
                        genderStr = "woman"
                    }
                R.id.radio_man ->
                    if (checked) {
                        onBtnClickSound()
                        genderStr = "man"
                    }
            }
        }
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