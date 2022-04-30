package com.example.dont_forget_to_drink

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.dont_forget_to_drink.databinding.UserProfileActivityBinding


lateinit var binding: UserProfileActivityBinding
var myDialog: Dialog? = null

class UserProfileActivity  : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)  // vypne v appke nocny rezim...
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_profile_activity)

        binding = UserProfileActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        myDialog = Dialog(this);
        val closeUserProfileBtn = findViewById<ImageButton>(R.id.profileBackArrowBtn)
        closeUserProfileBtn.setOnClickListener {
            onBtnClickSound()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
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

