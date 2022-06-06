package com.example.dont_forget_to_drink

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.dont_forget_to_drink.databinding.ActivityFirstTimeStartedBinding


class FirstTimeStartedActivity : AppCompatActivity() {




    private lateinit var name : EditText
    private lateinit var surname : EditText
    private lateinit var weight: EditText
    private lateinit var age: EditText

    private lateinit var wakeUpTime: EditText
    private lateinit var sleepTime: EditText

    private lateinit var sp : SharedPreferences
    private lateinit var nameStr : String
    private lateinit var surnameStr : String
    private lateinit var weightStr: String
    private lateinit var ageStr: String
    private var genderStr: String = "man"

    private lateinit var wakeUpTimeStr: String
    private lateinit var sleepTimeStr: String

    private lateinit var dailyWaterIntakeStr: String

    private var mMediaPlayer: MediaPlayer? = null

    lateinit var binding: ActivityFirstTimeStartedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)  // vypne v appke nocny rezim...
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_time_started)

        binding = ActivityFirstTimeStartedBinding.inflate(layoutInflater)
        setContentView(binding.root)



        name = binding.userNameEditText
        surname = binding.userSurnameEditText
        weight = binding.weightEditText
        age = binding.ageEditText
        wakeUpTime = binding.wakeUpTimeEditText
        sleepTime = binding.sleepTimeEditText

        val btnConfirm = binding.confirmButton

        sp = getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE)

        playSound()


        // tento button listener je na prvotné spustenie aplikacie, pomocou neho sa potvrdzuju udaje o pouzivatelovi, ktory bude appku pouzivat
        btnConfirm.setOnClickListener {

            onConfirmSound()

            nameStr = name.text.toString()
            surnameStr = surname.text.toString()
            weightStr = weight.text.toString()
            ageStr = age.text.toString()
            wakeUpTimeStr = wakeUpTime.text.toString()
            sleepTimeStr = sleepTime.text.toString()

            // kontrola či su vsetky polia vyplnene
            if(nameStr.isEmpty() || surnameStr.isEmpty() || weightStr.isEmpty() || ageStr.isEmpty() || genderStr.isEmpty() || sleepTimeStr.isEmpty() || wakeUpTimeStr.isEmpty()) {
                Toast.makeText(this, "Please, fill out all of the input boxes", Toast.LENGTH_SHORT).show()
            } else if (sleepTimeStr.toInt() > 24 || sleepTimeStr.toInt() < 0 || wakeUpTimeStr.toInt() > 24 || wakeUpTimeStr.toInt() < 0){
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Warning !!!")
                builder.setMessage("Wake up time and Sleep Time must be numbers in range <0,24>!")
                //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

                builder.setPositiveButton("Understand") { dialog, which ->
                    Toast.makeText(applicationContext,
                        "Thank you!", Toast.LENGTH_SHORT).show()
                }
                builder.show()
            }
            else {


                // tu prebehne vypocet denneho prijmu tekutin
                // robil som to podla stranky kde sa to pocitalo v ounces nie v ml, tak tu robim aj prevody
                // link na stranku s pocitanim:  https://www.weightwatchers.com/ca/en/article/how-much-water-should-you-drink-every-day

                var resultWaterInTake = 0.0



                if(ageStr.toInt() < 30) {
                    resultWaterInTake = weightStr.toInt() * 40.0

                } else if(ageStr.toInt() < 56) {
                    resultWaterInTake = weightStr.toInt() * 35.0
                } else {
                    resultWaterInTake = weightStr.toInt() * 30.0
                }

                resultWaterInTake /= 28.3 // teraz mam vysledok v ounces a ešte to prevediem na ml

                resultWaterInTake *= 29.57

                // vysledok si este zaokruhlim na 100ml

                val tempNum = resultWaterInTake % 100

                resultWaterInTake -= tempNum

                dailyWaterIntakeStr = resultWaterInTake.toString()
                // este mi treba odstranit desatinnu ciarku a cislo za nou tak subStr musim dat
                dailyWaterIntakeStr = dailyWaterIntakeStr.dropLast(2)




                var editor: SharedPreferences.Editor = sp.edit()

                editor.putString("userName", nameStr)
                editor.putString("userSurname", surnameStr)
                editor.putString("userWeight", weightStr)
                editor.putString("userAge", ageStr)
                editor.putString("userGender", genderStr)
                editor.putString("dailyWaterIntake", dailyWaterIntakeStr)
                editor.putString("wakeUpTime", wakeUpTimeStr)
                editor.putString("sleepTime", sleepTimeStr)

                editor.commit()
                Toast.makeText(this, "Information Saved", Toast.LENGTH_SHORT).show()




                // pomocou tohoto zaevidujem ze pouzivatel uz raz vyplnil vsetky data o sebe a nezobrazi sa mu už nikdy tato prva obrazovka
                getSharedPreferences("MyUserPrefs", MODE_PRIVATE)
                    .edit()
                    .putBoolean("firstRun", false)
                    .commit()


                backToActivityMain()


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


    override fun onBackPressed() {
        // super.onBackPressed();
        // Not calling **super**, disables back button in current screen.
    }



    private fun backToActivityMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
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