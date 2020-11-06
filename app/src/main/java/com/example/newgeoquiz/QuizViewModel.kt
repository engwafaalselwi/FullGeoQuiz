package com.example.newgeoquiz

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel

class QuizViewModel : ViewModel(){
    var currentIndex = 0
    var score = 0

    var isCheater = false

     val questionBank = listOf(
        Question(R.string.question_oceans,true),
        Question(R.string.question_sanaa,false),
        Question(R.string.question_sea,true),
        Question(R.string.question_taiz,false))

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer
    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId
    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

   fun preQuestion (){
       currentIndex = if(currentIndex == 0) {
           questionBank.size - 1
       }
       else {
           currentIndex - 1
       }
   }

    fun updateIsAnswered(){
        questionBank[currentIndex].answer=true;
    }



    }

