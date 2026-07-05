package com.example.fitness_app_kp.exercise

import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import java.util.*
import android.media.MediaPlayer
import android.util.Log
import android.app.Dialog

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener
{
    private var restTimer: CountDownTimer? = null
    private var restProgress = 0
    private var restTime: Long = 10

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0
    private var exerciseTime: Long = 40

    private var exerciseList: ArrayList<ExerciseModel>
    private var currentExercisePosition = -1
    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null

    private var exerciseAdapter: ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        setSupportActionBar(toolbar_exercise_activity)
        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        toolbar_exercise_activity.setNavigationOnClickListener{
            customDialogForBackButton()
        }
        tts = TextToSpeech(this,this)
        exerciseList = Exercises.defaultExerciseList()
        setupRestView()

        setupExerciseStatusRecyclerView()

        private fun setRestProgressBar(){
            progressBar.progress = restProgress
            restTimer = object : CountDownTimer(restTime*1000,1000){

                override fun onTick(millisUntilFinished: Long){
                    restProgress++
                    progressBar.progress = restTime.toInt() - restProgress
                    timer_tv.text = (restTime-restProgress).toString()
                }
                override fun onFinish(){
                    currentExercisePosition++
                    exerciseList!![currentExercisePosition]
                    exerciseAdapter!!.notifyDataSetChanged() // update adapter abt data change
                    setupExerciseView()
                }
            }.start()
        }
        private fun setupRestView(){

            try{
                player = MediaPlayer.create(applicationContext, R.raw.press_start)
                player!!.isLooping = false
                player!!.start()
            } catch(e: Exception){
                e.printStackTrace()
            }

            rest_view_ll.visibility = View.VISIBLE
            exercise_view_ll-visibility = View.GONE

            if(exerciseTimer != null){
                exerciseTimer !!.cancel()
                exerciseProgress = 0
            }
            setupProgressBar()
            speakOut(exerciseList!![currentExercisePosition].getName())

            // setting exercise properties
            image_iv.setImageResource(exerciseList!![currentExercisePosition].getImage())
            exercise_name_tv.text = exerciseList!![currentExercisePosition].getName()
        }
        override fun onInit(status:Int){
            if(status == TextToSpeech.SUCCESS){ //check if tts is available
                val result = tts!!.setLanguage(Locale.GERMANY)
                // check if language is installed
                if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                    Log.e("TTS","The language is unsupported")
                } else{
                    Log.e("TTS","Initialization Failed!")
                }
            }

            private fun speakOut(text: String){
                tts!!.speak(text, TextToSpeech.QUEUE_FLUSH,null,"")
            }

            private fun setupExerciseStatusRecyclerView(){
                // set the recycler view in horizontal way
                exercise_status_rv.layoutManager = LinearLayoutManager(this,
                    LinearLayoutManager.HORIZONTAL, false)
                exerciseAdapter = ExerciseStatusAdapter(exerciseList!!,this)
                exercise_status_rv.adapter = exerciseAdapter
            }

            override fun onDestroy() {
                // rest view
                if(restTimer != null){
                    restTimer!!.cancel()
                    restProgress = 0
                }
                // exercise View
                if(exerciseTimer != null){
                    exerciseTimer!!.cancel()
                    exerciseProgress = 0
                }
                // text to speech
                if(tts!=null){
                    tts!!.stop()
                    tts!!.shutdown()
                }
                // media player
                if(player!= null){
                    player!!.stop()
                }
                super.onDestroy()
            }

            override fun onBackPressed(){
                customDialogForBackButton()
            }
            private fun customDialogForBackButton(){
               val customDialog = Dialog(this)
               customDialog.setContentView(R.layout.dialog_custom_back_confirmation)
                // Click YES
                customDialog.yes_btn.setOnClickListener {
                    finish()
                    customDialog.dismiss()
                }
                // Click NO
                customDialog.no_btn.setOnClickListener {
                    customDialog.dismiss()
                }
                customDialog.show()
            }
        }
