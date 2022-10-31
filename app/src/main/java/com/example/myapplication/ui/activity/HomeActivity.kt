package com.example.myapplication.ui.activity

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.MyApp
import com.example.myapplication.R
import com.example.myapplication.database.DBHelper
import com.example.myapplication.databinding.ActivityHomeBinding
import com.example.myapplication.viewmodels.MainViewModel
import com.example.myapplication.viewmodels.MainViewModelFactory
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class HomeActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var mainViewModel: MainViewModel
    lateinit var binding: ActivityHomeBinding
    var db: DBHelper? = null

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory
    private val c: Calendar = Calendar.getInstance()
    private var dateTime = ""
    var mYear = 0
    var mMonth = 0
    var mDay = 0
    var mHour = 0
    var mMinute = 0
    var email: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        (application as MyApp).applicationComponent.inject(this)
        mainViewModel = ViewModelProvider(this, mainViewModelFactory)[MainViewModel::class.java]

        initView()
        setOnClickListener()
    }


    private fun initView() {
        db = DBHelper(this, null)
        getCommit()
    }

    private fun setOnClickListener() = with(binding) {
        buttonTime.setOnClickListener(this@HomeActivity)
        buttonProfile.setOnClickListener(this@HomeActivity)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.buttonTime -> {
                datePicker()
            }
            R.id.buttonProfile -> {
                val intent = Intent(this@HomeActivity, ProfileActivity::class.java)
                intent.putExtra("email", email)
                startActivity(intent)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun datePicker() {

        // Get Current Date
        mYear = c[Calendar.YEAR]
        mMonth = c[Calendar.MONTH]
        mDay = c[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(
            this,
            { view, year, monthOfYear, dayOfMonth ->
                dateTime = dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                //*************Call Time Picker Here ********************
                c[Calendar.YEAR] = year
                c[Calendar.MONTH] = monthOfYear
                c[Calendar.DAY_OF_MONTH] = dayOfMonth

                tiemPicker()
            }, mYear, mMonth, mDay
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun tiemPicker() {
        // Get Current Time
        mHour = c[Calendar.HOUR_OF_DAY]
        mMinute = c[Calendar.MINUTE]

        // Launch Time Picker Dialog
        val timePickerDialog = TimePickerDialog(
            this,
            { view, hourOfDay, minute ->
                mHour = hourOfDay
                mMinute = minute
                val date = "$dateTime $hourOfDay:$minute"
                binding.textViewTime.text = date

                c[Calendar.HOUR_OF_DAY] = hourOfDay
                c[Calendar.MINUTE] = minute
                sendPush(date)
                getDateTime()
            }, mHour, mMinute, false
        )
        timePickerDialog.show()
    }

    private fun getDateTime() {
        if (email.isNullOrEmpty().not())
            email?.let { db?.updateTime(it, stringToDate()) }
//        getAllData()
    }

    @SuppressLint("Range")
    fun getAllData() {
        val cursor = db?.getData()
        cursor!!.moveToFirst()
        do {
            Log.e("id", cursor.getString(cursor.getColumnIndex(DBHelper.ID_COL)) + "\n")
            Log.e("name", cursor.getString(cursor.getColumnIndex(DBHelper.TIME)) + "\n")
        } while (cursor.moveToNext())

        cursor.close()
    }

    private fun stringToDate(): String {
        val dateFormat2 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val finalDate = dateFormat2.format(c.time)
        Log.e("Date Format:", "Final Date:$finalDate")
        return finalDate
    }

    private fun getCommit() {
        val sharedPreference =
            getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)
        email = sharedPreference.getString("email", "")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendPush(date: String) {
        val intent = Intent(applicationContext, ProfileActivity::class.java)
        intent.putExtra("email", email)
        val CHANNEL_ID = "MYCHANNEL"
        val notificationChannel =
            NotificationChannel(CHANNEL_ID, "name", NotificationManager.IMPORTANCE_HIGH)
        val pendingIntent = PendingIntent.getActivity(applicationContext, 1, intent, 0)
        val notification = Notification.Builder(
            applicationContext, CHANNEL_ID
        )
            .setContentTitle("Selected date")
            .setContentIntent(pendingIntent)
            .addAction(android.R.drawable.sym_action_chat, date, pendingIntent)
            .setChannelId(CHANNEL_ID)
            .setSmallIcon(android.R.drawable.sym_action_chat)
            .build()

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
        notificationManager.notify(1, notification)
    }
}