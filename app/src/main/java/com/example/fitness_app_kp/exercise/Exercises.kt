package com.example.fitness_app_kp.exercise

class Exercises {
    companion object{
        fun defaultExerciseList():ArrayList<ExerciseModel>{
            var exercisesList = ArrayList<ExerciseModel>()

            val jumpingJacks = ExerciseModel(1,
                "Jumping Jacks",
                R.drawable.jumping_jacks,
                false,
                false)
            exercisesList.add(jumpingJacks)

            val lunge = ExerciseModel(2,
                "Lunge",
                R.drawable.lunge
                false,
                false)
            exercisesList.add(lunge)

            val plank = ExerciseModel(3,
                name="Plank",
                image=R.drawable.plank,
                false,
                false)
            exercisesList.add(plank)

            val pushUps = ExerciseModel(4,
                "Push Ups",
                R.drawable.pushups,
                false,
                false)
            exercisesList.add(pushUps)

            val squat = ExerciseModel(5,
                "Squat",
                R.drawable.squat,
                false,
                false)
            exercisesList.add(squat)

            return exercisesList
        }
    }
}