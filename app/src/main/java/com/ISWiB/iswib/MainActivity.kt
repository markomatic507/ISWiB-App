package com.ISWiB.iswib

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/*
        This is our main activity
        Here is where everything, that is important to user, happens
 */

class MainActivity : AppCompatActivity() {

    //Getting instance of firestore database
    private var db = FirebaseFirestore.getInstance()

    //Here we get our day of festival by getting todays date
    private val date: String
        get() {
            //Here we get year, month, and a date from calendar
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            /*
            For checking
            Log.e("A", "$year-$month-$day")
            */
            //Returns what day of festival it is
            if (year == 2019 && month == 7) {
                return when (day) {
                    14 -> "1"
                    15 -> "2"
                    16 -> "3"
                    17 -> "4"
                    18 -> "5"
                    19 -> "6"
                    20 -> "7"
                    21 -> "8"
                    22 -> "9"
                    else -> "0"
                }

            }
            return "0"
        }

    //Here we add menu(3 dots with about, and change workshop)
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    //Here we do something when user select one of the options in menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            //Here we change workshop
            R.id.change -> {
                //First we get shared preferences, and call editor
                val preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)
                val editor = preferences.edit()
                //Then we reset workshop value and apply it
                editor.putString("workshop", null)
                editor.apply()
                //Then we finish activity and restart activity
                finish()
                startActivity(intent)
                return true
            }
            //Here we send user to about activity
            R.id.about -> {
                val intent = Intent(baseContext, About::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val day = findViewById<TextView>(R.id.day)

        val preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val workshop = preferences.getString("workshop", null)

        val parent = findViewById<LinearLayout>(R.id.content)

        if (workshop == null) {
            //If there is no workshop selected we make user choose one
            val child = layoutInflater.inflate(R.layout.choose, null) as LinearLayout
            parent.addView(child)
            val spinner = findViewById<View>(R.id.spinner) as Spinner
            val adapter = ArrayAdapter.createFromResource(
                this, R.array.workshops,
                android.R.layout.simple_spinner_item
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

        } else {
            //Here we get data from database, we use listener so it constantly checks for changes of our documents
            day.text = "Day $date - $workshop"
            db.collection(workshop)
                //Get only documents from current day
                .whereEqualTo("day", date)
                .addSnapshotListener(EventListener { queryDocumentSnapshots, e ->
                    if (e != null) {
                        //Show user that some kind of error happened
                        val child = layoutInflater.inflate(R.layout.item, null) as LinearLayout
                        (child.findViewById<View>(R.id.error) as TextView).text = "Error please try later"
                        (child.findViewById<View>(R.id.error) as TextView).visibility = View.VISIBLE
                        parent.addView(child)
                        return@EventListener
                    }
                    //Here we remove previous elements of activity so that we don't get
                    //duplicate elements when changes happen
                    val content = findViewById<View>(R.id.content) as LinearLayout
                    content.removeAllViews()
                    if (queryDocumentSnapshots!!.isEmpty) {
                        //Show user that scheadule is currently empty
                        val child = layoutInflater.inflate(R.layout.item, null) as LinearLayout
                        (child.findViewById<View>(R.id.error) as TextView).text = "Schedule is currently empty"
                        (child.findViewById<View>(R.id.error) as TextView).visibility = View.VISIBLE
                        parent.addView(child)
                        return@EventListener
                    }
                    //Here we go through every document in collection
                    for (document in queryDocumentSnapshots) {
                        //We get data
                        val name = document.getString("name")
                        val location = document.getString("location")
                        val from = document.getString("from")
                        val to = document.getString("to")
                        val lat = document.getString("lat")
                        val lon = document.getString("lon")
                        val id = document.getString("id")
                        val day = document.getString("day")
                        //We use this so we can get proper date from string
                        val time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
                        //Getting users timezone
                        time.timeZone = TimeZone.getDefault()

                        //Here we just check if somehow any null value has gotten in
                        if (name == null || location == null || from == null || to == null ||
                            lat == null || lon == null || id == null
                        ) {
                            return@EventListener
                        }

                        //We use this so we can later get time in milis so we can set alarm
                        val timeInput = "$from:00.000"

                        var date: Date? = null

                        //Here we check which date of festival is given day, and set milis for it
                        when (day) {
                            "1" -> try {
                                date = time.parse("2019-07-14 $timeInput")
                            } catch (e1: ParseException) {
                                e1.printStackTrace()
                            }

                            "2" -> try {
                                date = time.parse("2019-07-15 $timeInput")
                            } catch (e1: ParseException) {
                                e1.printStackTrace()
                            }

                            "3" -> try {
                                date = time.parse("2019-07-16 $timeInput")
                            } catch (e1: ParseException) {
                                e1.printStackTrace()
                            }

                            "4" -> try {
                                date = time.parse("2019-07-17 $timeInput")
                            } catch (e1: ParseException) {
                                e1.printStackTrace()
                            }

                            "5" -> try {
                                date = time.parse("2019-07-18 $timeInput")
                            } catch (e1: ParseException) {
                                e1.printStackTrace()
                            }

                            "6" -> try {
                                date = time.parse("2019-07-19 $timeInput")
                            } catch (e1: ParseException) {
                                e1.printStackTrace()
                            }

                            "7" -> try {
                                date = time.parse("2019-07-20 $timeInput")
                            } catch (e1: ParseException) {
                                e1.printStackTrace()
                            }

                            "8" -> try {
                                date = time.parse("2019-07-21 $timeInput")
                            } catch (e1: ParseException) {
                                e1.printStackTrace()
                            }

                            "9" -> try {
                                date = time.parse("2019-07-22 $timeInput")
                            } catch (e1: ParseException) {
                                e1.printStackTrace()
                            }

                            else -> try {
                                date = time.parse("2019-07-04 $timeInput")
                            } catch (e1: ParseException) {
                                e1.printStackTrace()
                            }

                        }

                        //Here we display given event and add onclick event to it
                        val child = layoutInflater.inflate(R.layout.item, null) as LinearLayout
                        (child.findViewById<View>(R.id.title) as TextView).text = name
                        (child.findViewById<View>(R.id.message) as TextView).text = location
                        (child.findViewById<View>(R.id.time) as TextView).text = "$from-$to"
                        val click = child.findViewById<LinearLayout>(R.id.click)

                        //Here we connect it with map function
                        click.setOnClickListener {
                            startActivity(
                                map(
                                    java.lang.Double.parseDouble(lat),
                                    java.lang.Double.parseDouble(lon)
                                )
                            )
                        }
                        parent.addView(child)

                        //And here we call start function so we get alarm
                        start(
                            Integer.parseInt(id), date!!.time, name, location, java.lang.Double.parseDouble(lat),
                            java.lang.Double.parseDouble(lon)
                        )
                    }
                })
        }
    }

    //Here we get map intent that sends user to map with given coordinates
    private fun map(lat: Double?, lon: Double?): Intent {
        val gmmIntentUri = Uri.parse("geo:$lat,$lon?q=$lat,$lon")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        return mapIntent
    }

    //Here we start alarms so we can get notifications in the future
    private fun start(id: Int?, time: Long?, title: String?, message: String?, lat: Double?, lon: Double?) {
        /*
        For cheacking
        Log.v("IN START", "IN START")
        */
        //First we get alarm manager and intent that is connected to alert receiver
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlertReceiver::class.java)
        //Then we send values to intent
        intent.putExtra("title", title)
        intent.putExtra("message", message)
        intent.putExtra("lat", lat)
        intent.putExtra("lon", lon)
        //We check if id is null, if it is we send 1, if not we send id
        if (id == null) {
            intent.putExtra("id", 1)
        } else {
            intent.putExtra("id", id)
        }
        val pendingIntent = PendingIntent.getBroadcast(this, id!!, intent, 0)

        //We check if time is in the past so that we don't get unnecessary notifications
        if (time ?: 0 >= System.currentTimeMillis()) {
            /*
            For cheacking
            Log.e("IN START", "IN$title-$message")
            */
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time!!, pendingIntent)
        }
    }

    //Here we get user to choose which workshop they belong to
    fun save(view: View) {
        val spinner = findViewById<Spinner>(R.id.spinner)
        val text = spinner.selectedItem.toString()
        val preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("workshop", text)
        editor.apply()
        finish()
        startActivity(intent)
    }
}
