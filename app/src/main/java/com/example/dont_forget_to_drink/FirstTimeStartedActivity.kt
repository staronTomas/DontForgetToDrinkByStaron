package com.example.dont_forget_to_drink

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class FirstTimeStartedActivity : AppCompatActivity() {


    lateinit var name : EditText
    lateinit var surname : EditText
    lateinit var button : Button

    lateinit var sp : SharedPreferences
    lateinit var nameStr : String
    lateinit var surnameStr : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_time_started)

        name = findViewById(R.id.userName)
        surname = findViewById(R.id.userSurname)


        sp = getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE)

        button.setOnClickListener(View.OnClickListener {
            @Override
            fun onClick(view: View) {
                nameStr = name.text.toString()
                surnameStr = surname.text.toString()

                var editor: SharedPreferences.Editor = sp.edit()

                editor.putString("userName", nameStr)
                editor.putString("userSurname", surnameStr)

                editor.commit()
                Toast.makeText(this@FirstTimeStartedActivity, "Information Saved", Toast.LENGTH_SHORT).show()
            }
        })

    }



    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_woman ->
                    if (checked) {
                        // Pirates are the best
                    }
                R.id.radio_man ->
                    if (checked) {
                        // Ninjas rule
                    }
            }
        }
    }
}