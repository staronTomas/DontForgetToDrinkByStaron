package com.example.dont_forget_to_drink

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton

class FirstTimeStartedActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_time_started)
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