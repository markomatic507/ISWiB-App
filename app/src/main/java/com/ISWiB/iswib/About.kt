package com.ISWiB.iswib

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

/*
         ABOUT ACTIVITY
         Contains app name, and by whom it was made
         Clicking 5 times on the name of creator sends you to admin activity where you can add events
*/

class About : AppCompatActivity() {

    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Adds back button to action bar that sends user to its parent activity that is defined in manifest
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_about)
    }

    //Function that counts number of clicks and sends user to admin page
    fun go(view: View) {
        count++
        if (count >= 5) {
            val intent = Intent(baseContext, Sign::class.java)
            startActivity(intent)
        }
    }
}
