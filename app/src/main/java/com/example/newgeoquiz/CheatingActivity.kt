package com.example.newgeoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity.apply
import android.widget.Button
import android.widget.TextView

private const val EXTRA_ANSWER_IS_TRUE =
    "com.example.newgeoquiz.answer_is_true"

const val EXTRA_ANSWER_SHOWN = "com.example.newgeoquiz.answer_shown"

class CheatingActivity : AppCompatActivity() {
    private lateinit var answerTextView : TextView
    private  lateinit var showAnswerView : Button
    private lateinit var version : TextView

    private var answerIsTrue = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheating)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE,false)
        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerView = findViewById(R.id.show_answer_button)
        version = findViewById(R.id.TextVersion)

        version.setText("API LEVEL: ${Build.VERSION.SDK_INT}")

        showAnswerView.setOnClickListener {
            var answerText = when {
                answerIsTrue -> R.string.true_button
                else ->R.id.flase_button
            }
            answerTextView.setText(answerText)
            setAnswerShownResult(true)
        }


    }

    private fun setAnswerShownResult(isAnswerShown :Boolean){

        var data = Intent().apply{
            putExtra(EXTRA_ANSWER_SHOWN , isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(packageContext : Context, answerIsTrue : Boolean): Intent {
            return Intent(packageContext , CheatingActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE , answerIsTrue)
            }

        }
    }
}