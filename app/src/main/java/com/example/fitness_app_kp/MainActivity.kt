package com.example.fitness_app_kp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import android.content.Intent
import android.widget.LinearLayout
import androidx.compose.ui.tooling.preview.Preview

import com.example.fitness_app_kp.exercise.ExerciseActivity
import com.example.fitness_app_kp.BMIActivity
import com.example.fitness_app_kp.databinding.ActivityBmiBinding
import com.example.fitness_app_kp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        var binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startLl.setOnClickListener {
            val intent = Intent(this, ExerciseActivity::class.java)
            startActivity(intent)
        }

        binding.bmiLl.setOnClickListener {
            val intent = Intent(this, BMIActivity::class.java)
            startActivity(intent)
        }
    }

}