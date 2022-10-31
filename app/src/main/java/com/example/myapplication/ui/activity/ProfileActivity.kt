package com.example.myapplication.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.MyApp
import com.example.myapplication.R
import com.example.myapplication.database.DBHelper
import com.example.myapplication.databinding.ActivityProfileBinding
import com.example.myapplication.viewmodels.MainViewModel
import com.example.myapplication.viewmodels.MainViewModelFactory
import com.google.gson.Gson
import javax.inject.Inject


class ProfileActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var mainViewModel: MainViewModel
    lateinit var binding: ActivityProfileBinding
    var db: DBHelper? = null
    var email: String? = null

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        (application as MyApp).applicationComponent.inject(this)
        mainViewModel = ViewModelProvider(this, mainViewModelFactory)[MainViewModel::class.java]
        email = intent.getStringExtra("email")

        initView()
        setOnClickListener()
    }

    private fun initView() {
        db = DBHelper(this, null)
        if (email.isNullOrEmpty().not())
            setData()
    }

    private fun setData() = with(binding) {
        val localData = email?.let { db?.getUserData(it) }
        if (localData != null)
            binding.textViewDetails.text = Gson().toJson(localData)
    }

    private fun getCommit() {
        val sharedPreference =
            getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)
        email = sharedPreference.getString("email", "")
    }

    private fun setOnClickListener() = with(binding) {
        buttonLogout.setOnClickListener(this@ProfileActivity)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.buttonLogout -> {
                val preferences = getSharedPreferences("USER_DATA", MODE_PRIVATE)
                val editor = preferences.edit()
                editor.clear()
                editor.apply()
                val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
    }
}