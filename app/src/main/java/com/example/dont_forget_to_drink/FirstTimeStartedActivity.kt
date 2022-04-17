package com.example.dont_forget_to_drink

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.View.inflate
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.dont_forget_to_drink.databinding.ActivityFirstTimeStartedBinding.inflate


class FirstTimeStartedActivity : AppCompatActivity() {


    private lateinit var name : EditText
    private lateinit var surname : EditText
    private lateinit var weight: EditText
    private lateinit var age: EditText

    private lateinit var sp : SharedPreferences
    private lateinit var nameStr : String
    private lateinit var surnameStr : String
    private lateinit var weightStr: String
    private lateinit var ageStr: String
    private lateinit var genderStr: String

    private var mMediaPlayer: MediaPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_time_started)

        name = findViewById(R.id.userNameEditText)
        surname = findViewById(R.id.userSurnameEditText)
        weight = findViewById(R.id.weightEditText)
        age = findViewById(R.id.ageEditText)

        val btnConfirm = findViewById(R.id.confirm_button) as Button


        sp = getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE)

        playSound()


        // tento button listener je na prvotné spustenie aplikacie, pomocou neho sa potvrdzuju udaje o pouzivatelovi, ktory bude appku pouzivat
        btnConfirm.setOnClickListener {
            nameStr = name.text.toString()
            surnameStr = surname.text.toString()
            weightStr = weight.text.toString()
            ageStr = age.text.toString()

            if(nameStr.isEmpty() || surnameStr.isEmpty() || weightStr.isEmpty() || ageStr.isEmpty() || genderStr.isEmpty()) {
                Toast.makeText(this, "Please, fill out all of the input boxes", Toast.LENGTH_SHORT).show()
            } else {

                var editor: SharedPreferences.Editor = sp.edit()

                editor.putString("userName", nameStr)
                editor.putString("userSurname", surnameStr)
                editor.putString("userWeight", weightStr)
                editor.putString("userAge", ageStr)
                editor.putString("userGender", genderStr)

                editor.commit()
                Toast.makeText(this, "Information Saved", Toast.LENGTH_SHORT).show()



                // tu prebehne vypocet denneho prijmu tekutin





                // pomocou tohoto zaevidujem ze pouzivatel uz raz vyplnil vsetky data o sebe a nezobrazi sa mu už nikdy tato prva obrazovka
                getSharedPreferences("MyUserPrefs", MODE_PRIVATE)
                    .edit()
                    .putBoolean("firstRun", false)
                    .commit()

                super.finish()  // toto mi zavrie moju aktivitu a zapne sa activity_main.xml
            }
        }
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_woman ->
                    if (checked) {
                        genderStr = "woman"
                        Toast.makeText(this, "Zenaaa", Toast.LENGTH_SHORT).show()
                    }
                R.id.radio_man ->
                    if (checked) {
                        genderStr = "man"
                    }
            }
        }
    }


    override fun onBackPressed() {
        // super.onBackPressed();
        // Not calling **super**, disables back button in current screen.
    }









    // MUSIC PLAYER


    fun playSound() {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(this, R.raw.first_time_activity_music)
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
            mMediaPlayer = MediaPlayer.create(this, R.raw.first_time_activity_music)
            mMediaPlayer!!.isLooping = true
            mMediaPlayer!!.start()
        } else mMediaPlayer!!.start()
    }
}