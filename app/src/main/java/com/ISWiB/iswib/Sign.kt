package com.ISWiB.iswib

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import com.firebase.ui.auth.AuthUI
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

/*
        Sign Activity or admin activity
        Here we can add events
 */

class Sign : AppCompatActivity() {

    //Getting instance of firestore database
    private var db = FirebaseFirestore.getInstance()

    //This allow us to use authuis email auth
    private var providers: List<AuthUI.IdpConfig> = listOf(AuthUI.IdpConfig.EmailBuilder().build())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Adds back button to action bar that sends user to its parent activity that is defined in manifest
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_sign)

        //Adding values to the spinner
        val spinner = findViewById<View>(R.id.spinner) as Spinner
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.workshops, android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        //Starts auth process with help of authui
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            1
        )
    }

    //Called when authui gets some result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //Checks is user succeeded in authing, if it did it just continues, if not it throws it back to main activity
        if (resultCode == Activity.RESULT_OK) {
            return
        } else {
            val intent = Intent(baseContext, MainActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // Respond to the action bar's Up/Home button
            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //Function for writing to database
    fun write(view: View) {

        //Here we get values from all the fields
        val spinner = findViewById<View>(R.id.spinner) as Spinner
        val workshop = spinner.selectedItem.toString()
        val documentname = findViewById<EditText>(R.id.documentName)
        val documentName = documentname.text.toString()
        val Name = findViewById<EditText>(R.id.name)
        val name = Name.text.toString()
        val Location = findViewById<EditText>(R.id.location)
        val location = Location.text.toString()
        val From = findViewById<EditText>(R.id.from)
        val from = From.text.toString()
        val To = findViewById<EditText>(R.id.to)
        val to = To.text.toString()
        val Day = findViewById<EditText>(R.id.day)
        val day = Day.text.toString()
        val Lat = findViewById<EditText>(R.id.lat)
        val lat = Lat.text.toString()
        val Lon = findViewById<EditText>(R.id.lon)
        val lon = Lon.text.toString()
        val Id = findViewById<EditText>(R.id.id)
        val id = Id.text.toString()

        //Here we put all those values to hash map
        val sch = HashMap<String, Any>()
        sch["name"] = name
        sch["location"] = location
        sch["from"] = from
        sch["to"] = to
        sch["day"] = day
        sch["lat"] = lat
        sch["lon"] = lon
        sch["id"] = id

        //And here we write to database
        db.collection(workshop).document(documentName).set(sch)
    }
}
