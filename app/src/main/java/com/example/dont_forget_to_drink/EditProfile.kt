package com.example.dont_forget_to_drink

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
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


        // Button listeners pre horne a aj dolne tlacidlo confirm edit profile
        var editProfileButton1 = findViewById<ImageButton>(R.id.editProfileButtonTop)
        editProfileButton1.setOnClickListener {
            onBtnClickSound()
            alertChangeProfileDetails()
        }
        var editProfileButton2 = findViewById<Button>(R.id.editProfileButtonBottom)
        editProfileButton2.setOnClickListener {
            onBtnClickSound()
            alertChangeProfileDetails()
        }

        myDialog = Dialog(this);
        val closeUserProfileBtn = findViewById<ImageButton>(R.id.profileBackArrowBtn)
        closeUserProfileBtn.setOnClickListener {
            onBtnClickSound()
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    // tu prebehne alert pre potvrdenie zmien profilu
    private fun alertChangeProfileDetails() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Attention !")
        builder.setMessage("Do you really want to change your personal data?")

        builder.setPositiveButton("Yes, change") { dialog, which ->
            onConfirmSound()
            changeProfileDetails()
        }
        builder.setNegativeButton("No, cancel.") { dialog, which ->
            onBtnClickSound()
            Toast.makeText(applicationContext,
                "Data change canceled", Toast.LENGTH_SHORT).show()
        }
        builder.show()
    }

    // tu prebehne zmena dat o pouzivatelovi
    private fun changeProfileDetails() {

        var nameStr = findViewById<EditText>(R.id.editName).text.toString()
        var surnameStr = findViewById<EditText>(R.id.editSurname).text.toString()
        var weightStr = findViewById<EditText>(R.id.editWeight).text.toString()
        var ageStr = findViewById<EditText>(R.id.editAge).text.toString()
        var wakeUpTimeStr = findViewById<EditText>(R.id.editWakeUpTime).text.toString()
        var sleepTimeStr = findViewById<EditText>(R.id.editSleepTime).text.toString()




        if(nameStr.isEmpty() || surnameStr.isEmpty() || weightStr.isEmpty() || ageStr.isEmpty() || genderStr.isEmpty() || sleepTimeStr.isEmpty() || wakeUpTimeStr.isEmpty()) {
                Toast.makeText(this, "Please, fill out all of the input boxes", Toast.LENGTH_SHORT).show()
        }
       else if (sleepTimeStr.toInt() > 24 || sleepTimeStr.toInt() < 0 || wakeUpTimeStr.toInt() > 24 || wakeUpTimeStr.toInt() < 0) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Warning !!!")
            builder.setMessage("Wake up time and Sleep Time must be numbers in range <0,24>!")
            //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

            builder.setPositiveButton("Understand") { dialog, which ->
                Toast.makeText(
                    this,
                    "Thank you!", Toast.LENGTH_SHORT
                ).show()
            }
            builder.show()
        }
        else {

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

            var dailyWaterIntakeStr = resultWaterInTake.toString()
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


            super.closeContextMenu()
            finish()




        }

    }

    private fun loadOldData() {
        var userName = sp.getString("userName", "")
        var userSurname = sp.getString("userSurname", "")
        var userWeight = sp.getString("userWeight", "")
        var userAge = sp.getString("userAge", "")
        var userGender = sp.getString("userGender", "")
        genderStr = userGender.toString()
        var userWakeUpTime = sp.getString("wakeUpTime", "")
        var userSleepTime = sp.getString("sleepTime", "")
        var dailyWaterAmount = sp.getString("dailyWaterIntake", "")



        var text1 = findViewById<TextView>(R.id.titleNameSurname)
        var myText : String = "$userName $userSurname"
        text1.text = myText

        var myEditText = findViewById<EditText>(R.id.editName)
        myText = userName.toString()
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