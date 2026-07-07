package com.example.fitness_app_kp.exercise

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitness_app_kp.R
import java.util.Locale
import com.example.fitness_app_kp.FinishActivity
import com.example.fitness_app_kp.databinding.ActivityExerciseBinding

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var binding: ActivityExerciseBinding

    private var restTimer: CountDownTimer? = null
    private var restProgress = 0
    private val restTime: Long = 10

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0
    private val exerciseTime: Long = 40

    private lateinit var exerciseList: ArrayList<ExerciseModel>
    private var currentExercisePosition = -1

    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null

    private var exerciseAdapter: ExerciseAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarExerciseActivity)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbarExerciseActivity.setNavigationOnClickListener {
            customDialogForBackButton()
        }
        onBackPressedDispatcher.addCallback(this) {
            customDialogForBackButton()
        }

        tts = TextToSpeech(this, this)

        exerciseList = Exercises.defaultExerciseList()

        setupRestView()
        setupExerciseStatusRecyclerView()
    }

    private fun setupRestView() {

        try {
            player = MediaPlayer.create(applicationContext, R.raw.press_start)
            player?.isLooping = false
            player?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.restViewLl.visibility = View.VISIBLE
        binding.exerciseViewLl.visibility = View.GONE

        restTimer?.cancel()
        restProgress = 0

        binding.nextExerciseTv.text =
            exerciseList[currentExercisePosition + 1].getName()

        setRestProgressBar()
    }

    private fun setRestProgressBar() {

        binding.progressBar.progress = restProgress

        restTimer = object : CountDownTimer(restTime * 1000, 1000) {

            override fun onTick(millisUntilFinished: Long) {

                restProgress++

                binding.progressBar.progress = restTime.toInt() - restProgress

                binding.timerTv.text =
                    (restTime - restProgress).toString()
            }

            override fun onFinish() {

                currentExercisePosition++

                exerciseList[currentExercisePosition].setIsSelected(true)

                exerciseAdapter?.notifyDataSetChanged()

                setupExerciseView()
            }

        }.start()
    }

    private fun setupExerciseView() {

        binding.exerciseViewLl.visibility = View.VISIBLE
        binding.restViewLl.visibility = View.GONE

        exerciseTimer?.cancel()
        exerciseProgress = 0

        binding.imageIv.setImageResource(
            exerciseList[currentExercisePosition].getImage()
        )

        binding.exerciseNameTv.text =
            exerciseList[currentExercisePosition].getName()

        speakOut(exerciseList[currentExercisePosition].getName())

        setExerciseProgressBar()
    }

    private fun setExerciseProgressBar() {

        binding.exerciseProgressBar.progress = exerciseProgress

        exerciseTimer = object : CountDownTimer(exerciseTime * 1000, 1000) {

            override fun onTick(millisUntilFinished: Long) {

                exerciseProgress++

                binding.exerciseProgressBar.progress =
                    exerciseTime.toInt() - exerciseProgress

                binding.exerciseTimerTv.text =
                    (exerciseTime - exerciseProgress).toString()
            }

            override fun onFinish() {

                if (currentExercisePosition < exerciseList.size - 1) {

                    exerciseList[currentExercisePosition].setIsSelected(false)
                    exerciseList[currentExercisePosition].setIsCompleted(true)

                    exerciseAdapter?.notifyDataSetChanged()

                    setupRestView()

                } else {

                    startActivity(
                        Intent(
                            this@ExerciseActivity,
                            FinishActivity::class.java
                        )
                    )

                    finish()
                }
            }

        }.start()
    }

    override fun onInit(status: Int) {

        if (status == TextToSpeech.SUCCESS) {

            val result = tts?.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED) {

                Log.e("TTS", "Language not supported.")
            }

        } else {

            Log.e("TTS", "Initialization failed.")
        }
    }

    private fun speakOut(text: String) {

        tts?.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            ""
        )
    }

    private fun setupExerciseStatusRecyclerView() {

        binding.exerciseStatusRv.layoutManager =
            LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
            )

        exerciseAdapter =
            ExerciseAdapter(exerciseList, this)

        binding.exerciseStatusRv.adapter = exerciseAdapter
    }

    override fun onDestroy() {

        restTimer?.cancel()
        exerciseTimer?.cancel()

        tts?.stop()
        tts?.shutdown()

        player?.apply {
            stop()
            release()
        }

        super.onDestroy()
    }


    private fun customDialogForBackButton() {

        val dialog = Dialog(this)

        dialog.setContentView(R.layout.dialog_custom_back_confirmation)

        val yesButton = dialog.findViewById<Button>(R.id.yes_btn)
        val noButton = dialog.findViewById<Button>(R.id.no_btn)

        yesButton.setOnClickListener{
            dialog.dismiss()
            finish()
        }
        noButton.setOnClickListener(){
            dialog.dismiss()
        }

        dialog.show()
    }
}
