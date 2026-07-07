package com.example.fitness_app_kp

import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fitness_app_kp.databinding.ActivityFinishBinding
import java.text.SimpleDateFormat
import java.util.*



class FinishActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarFinishActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)//set back button
        }

        //set the back btn on toolbar
       binding.toolbarFinishActivity.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed() //go back to main activity, because I finished the exercise activity
        }


        binding.finishBtn.setOnClickListener {
            finish() // finish this activity and go back to main activity
        }

    }
}