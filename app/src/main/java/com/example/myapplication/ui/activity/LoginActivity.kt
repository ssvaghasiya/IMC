package com.example.myapplication.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.MyApp
import com.example.myapplication.R
import com.example.myapplication.database.DBHelper
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.viewmodels.MainViewModel
import com.example.myapplication.viewmodels.MainViewModelFactory
import com.google.gson.Gson
import javax.inject.Inject

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var mainViewModel: MainViewModel
    lateinit var binding: ActivityLoginBinding
    var db: DBHelper? = null

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        (application as MyApp).applicationComponent.inject(this)
        mainViewModel = ViewModelProvider(this, mainViewModelFactory)[MainViewModel::class.java]

        initView()
        setOnClickListener()
    }

    private fun initView() {
        db = DBHelper(this, null)
        getCommit()
    }

    private fun getCommit() {
        val sharedPreference =
            getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)
        val email = sharedPreference.getString("email", "")
        if (email.isNullOrEmpty().not()) {
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    private fun setOnClickListener() = with(binding) {
        textViewRegister.setOnClickListener(this@LoginActivity)
        buttonLogin.setOnClickListener(this@LoginActivity)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.textViewRegister -> {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            R.id.buttonLogin -> {
                if (isValidate())
                    getUserData(binding.editTextEmail.text.toString().trim())
            }
        }
    }

    @SuppressLint("Range")
    fun getUserData(email: String) {
        val data = db?.getUserData(email)
        if (data != null && data.email == email && data.password == binding.editTextPassword.text.toString()
                .trim()
        ) {
            commit()
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        } else {
            showToast("invalid")
        }
    }

    fun commit() {
        val sharedPreference =
            getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putString("email", binding.editTextEmail.text.toString().trim())
        editor.commit()
    }

    @SuppressLint("Range")
    fun getAllData() {
        val cursor = db?.getData()
        cursor!!.moveToFirst()
        do {
            Log.e("id", cursor.getString(cursor.getColumnIndex(DBHelper.ID_COL)) + "\n")
            Log.e("name", cursor.getString(cursor.getColumnIndex(DBHelper.EMAIL)) + "\n")
        } while (cursor.moveToNext())

        cursor.close()
    }

    fun isValidate(): Boolean {
        if (binding.editTextEmail.text.toString().trim().isNullOrEmpty()) {
            showToast("Please enter email")
            return false
        } else if (binding.editTextPassword.text.toString().trim().isNullOrEmpty()) {
            showToast("Please enter password")
            return false
        }
        return true
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

}