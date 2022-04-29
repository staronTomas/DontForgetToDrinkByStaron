package com.example.dont_forget_to_drink

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class UserProfileActivity  : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)  // vypne v appke nocny rezim...
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_profile_activity)


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

