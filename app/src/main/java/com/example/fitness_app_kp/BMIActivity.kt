package com.example.fitness_app_kp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.fitness_app_kp.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBmiBinding
    val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW"
    val US_UNITS_VIEW = "US_UNIT_VIEW"

    var currentVisibleView : String = METRIC_UNITS_VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarBmiActivity)

        setSupportActionBar(binding.toolbarBmiActivity)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true) //set back button
        }
        // set back button action
        binding.toolbarBmiActivity.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.calculateUnitsBtn.setOnClickListener {
            if(currentVisibleView.equals(METRIC_UNITS_VIEW)) {
                if (validateMetricUnits()) {
                    val heightValue: Float = binding.metricUnitHeightEt.text.toString().toFloat() / 100 // convert to meters
                    val weightValue: Float = binding.metricUnitWeightEt.text.toString().toFloat()

                    val bmi = weightValue / (heightValue * heightValue)
                    displayBMIResult(bmi)
                } else {
                    Toast.makeText(this, "Please enter valid values", Toast.LENGTH_SHORT).show()
                }
            }else{
                if (validateUSUnits()) {
                    val weightValue: Float = binding.usUnitWeightEt.text.toString().toFloat()
                    val feetValue: String = binding.usUnitHeightFeetEt.text.toString()
                    val inchValue: String = binding.usUnitHeightInchEt.text.toString()

                    val heightValue = (feetValue.toFloat() * 12) + inchValue.toFloat()
                    val bmi = (weightValue / (heightValue * heightValue)) * 703
                    displayBMIResult(bmi)
                } else {
                    Toast.makeText(this, "Please enter valid values", Toast.LENGTH_SHORT).show()
                }
            }
        }

        makeVisibleMetricUnitsView()

        binding.unitsRg.setOnCheckedChangeListener { _, checkedId ->
            if(checkedId == R.id.metric_unit_rb){
                makeVisibleMetricUnitsView()
            }else{
                makeVisibleUSUnitsView()
            }
        }
    }

    private fun makeVisibleMetricUnitsView(){
        currentVisibleView = METRIC_UNITS_VIEW
        binding.metricUnitsViewLl.visibility = View.VISIBLE
        binding.usUnitsViewLl.visibility = View.GONE

        binding.metricUnitWeightEt.text!!.clear()
        binding.metricUnitHeightEt.text!!.clear()

        binding.displayBmiResultLl.visibility = View.GONE
    }

    private fun makeVisibleUSUnitsView(){
        currentVisibleView = US_UNITS_VIEW
        binding.metricUnitsViewLl.visibility = View.GONE
        binding.usUnitsViewLl.visibility = View.VISIBLE

        binding.usUnitWeightEt.text!!.clear()
        binding.usUnitHeightFeetEt.text!!.clear()
        binding.usUnitHeightInchEt.text!!.clear()

        binding.displayBmiResultLl.visibility = View.GONE

    }

    private fun displayBMIResult(bmi: Float){
        val bmiLabel: String
        val bmiDescription : String

        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops!You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to workout!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(35f) > 0) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel=""
            bmiDescription=""
        }

        binding.displayBmiResultLl.visibility = View.VISIBLE
        /*
        your_bmi_tv.visibility = View.VISIBLE
        bmi_result_tv.visibility = View.VISIBLE
        bmi_type_tv.visibility = View.VISIBLE
        bmi_description_tv.visibility = View.VISIBLE
        */

        // This is used to round the result value to 2 decimal values after "."
        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        //set updated text
       binding.bmiResultTv.text = bmiValue
       binding.bmiTypeTv.text = bmiLabel
       binding.bmiDescriptionTv.text = bmiDescription
    }

    private fun validateMetricUnits() : Boolean {
        var isValid = true

        if(binding.metricUnitWeightEt.text.toString().isEmpty() || binding.metricUnitHeightEt.text.toString().isEmpty()){
            isValid = false
        }


        return isValid
    }

    private fun validateUSUnits() : Boolean {
        var isValid = true

        if(binding.usUnitWeightEt.text.toString().isEmpty() ||
           binding.usUnitHeightFeetEt.text.toString().isEmpty() ||
            binding.usUnitHeightInchEt.text.toString().isEmpty()){
            isValid = false
        }

        return isValid
    }
}