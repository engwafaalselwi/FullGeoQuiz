package com.example.newgeoquiz

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders


    private const val KEY_INDEX = "index"
    private const val REQUEST_CODE_CHAT = 0

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton :ImageButton
    private lateinit var  prevButton :ImageButton
    private lateinit var questionTextView : TextView
    private lateinit var cheatButton : Button

   private var score = 0




    private val quizViewModel : QuizViewModel by lazy{
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            savedInstanceState.putInt(KEY_INDEX , quizViewModel.currentIndex)
        }


        val provider :ViewModelProvider = ViewModelProviders.of(this)
        val quizViewModel = provider.get(QuizViewModel::class.java)

        trueButton = findViewById(R.id.true_button)
        falseButton=findViewById(R.id.flase_button)
        nextButton=findViewById(R.id.next_button)
        questionTextView=findViewById(R.id.question_text_view)
        prevButton = findViewById(R.id.prev_button)
        cheatButton = findViewById(R.id.cheat_button)
        trueButton.setOnClickListener  {

            checkAnswer(true)
        }

        falseButton.setOnClickListener {
            checkAnswer(false)

        }

        nextButton.setOnClickListener {
           quizViewModel.moveToNext()

            updateQusetion()

        }
        prevButton.setOnClickListener {
            quizViewModel.preQuestion()

            updateQusetion()
        }

        cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatingActivity.newIntent(this@MainActivity,answerIsTrue)
//            val options = ActivityOptions.makeClipRevealAnimation(view, 0, 0, view.width, view.height)
          startActivityForResult(intent, REQUEST_CODE_CHAT )
        }


        questionTextView.setOnClickListener {
           quizViewModel.moveToNext()
            updateQusetion()
        }

        updateQusetion()

    }
    private fun updateQusetion (){
       // val questionTextResId = questionBank[currentIndex].textResId
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)

        if(quizViewModel.questionBank[quizViewModel.currentIndex].textResId <= 1){
            trueButton.isEnabled = false
            falseButton.isEnabled = false

        }
        else{
            trueButton.isEnabled = true
            falseButton.isEnabled = true
        }

    }

    private fun checkAnswer(userAnswer :Boolean){
        trueButton.isEnabled = false
        falseButton.isEnabled =false
       // val correctAnswer = questionBank[currentIndex].answer

        quizViewModel.updateIsAnswered()
        val correctAnswer = quizViewModel.currentQuestionAnswer



        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }

        if(userAnswer == correctAnswer){
            score++

        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
        showQuizResult()


    }

    private fun showQuizResult(){
        quizViewModel.questionBank.forEach {question:Question->
            if(!question.answer){
                return
            }
        }
        Toast.makeText(this,"YourQuizResult: ${score}/${quizViewModel.questionBank.size}",Toast.LENGTH_SHORT).show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode != Activity.RESULT_OK){
            return
        }
        if(requestCode == REQUEST_CODE_CHAT ) {
            quizViewModel.isCheater = data ?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }
    }
