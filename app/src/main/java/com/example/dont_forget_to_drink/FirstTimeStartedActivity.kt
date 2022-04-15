package com.example.dont_forget_to_drink

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.compose.ui.text.font.FontWeight
import org.w3c.dom.Text

class FirstTimeStartedActivity : AppCompatActivity() {


    private lateinit var name : EditText
    private lateinit var surname : EditText
    private lateinit var weight: EditText
    private lateinit var gender: EditText

    private lateinit var sp : SharedPreferences
    private lateinit var nameStr : String
    private lateinit var surnameStr : String
    private lateinit var weightStr: String
    private lateinit var genderStr: String


    private lateinit var radioGroup: RadioGroup
    private lateinit var radioButton: RadioButton
    private lateinit var textView: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_time_started)

        name = findViewById(R.id.userName)
        surname = findViewById(R.id.userSurname)
        weight = findViewById(R.id.weightEditText)

        val btnConfirm = findViewById(R.id.confirm_button) as Button


        sp = getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE)

        btnConfirm.setOnClickListener {
            nameStr = name.text.toString()
            surnameStr = surname.text.toString()
            weightStr = weight.text.toString()

            if(nameStr.isEmpty() || surnameStr.isEmpty() || weightStr.isEmpty()) {
                Toast.makeText(this, "Please, fill out all of the input boxes", Toast.LENGTH_SHORT).show()
            } else {

                var editor: SharedPreferences.Editor = sp.edit()

                editor.putString("userName", nameStr)
                editor.putString("userSurname", surnameStr)
                editor.putString("userWeight", weightStr)

                editor.commit()
                Toast.makeText(this, "Information Saved", Toast.LENGTH_SHORT).show()
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