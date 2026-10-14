package com.example.lifehackurbanmyth

import android.app.Activity
import android.os.Bundle
import android.graphics.Typeface
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {

    private var questionIndex = 0
    private var score = 0
    private var answered = false

    private val questions = listOf(
        "Drinking water helps your body function properly." to true,
        "You can safely replace sleep with energy drinks." to false,
        "Washing your hands helps prevent the spread of germs." to true,
        "Cracking your knuckles always causes arthritis." to false,
        "Regular physical activity supports good health." to true,
        "You should look directly at the sun to improve eyesight." to false,
        "Reading in dim light permanently damages your eyes." to false,
        "A short walk can help break up long periods of sitting." to true,
        "Antibiotics cure every type of cold and flu." to false,
        "Checking information from reliable sources helps spot myths." to true
    )

    private lateinit var root: LinearLayout
    private lateinit var questionText: TextView
    private lateinit var feedbackText: TextView
    private lateinit var scoreText: TextView
    private lateinit var hackButton: Button
    private lateinit var mythButton: Button
    private lateinit var nextButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showWelcome()
    }

    private fun setupScreen(title: String) {
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(28, 32, 28, 32)
            setBackgroundColor(0xFFF4F7FB.toInt())
        }

        val heading = TextView(this).apply {
            text = title
            textSize = 27f
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.CENTER
            setTextColor(0xFF183153.toInt())
        }

        root.addView(
            heading,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply { bottomMargin = 24 }
        )

        setContentView(root)
    }

    private fun addText(text: String, size: Float): TextView {
        return TextView(this).apply {
            this.text = text
            textSize = size
            gravity = Gravity.CENTER
            setTextColor(0xFF263238.toInt())
            setPadding(8, 12, 8, 12)
            root.addView(
                this,
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            )
        }
    }

    private fun addButton(label: String, action: () -> Unit): Button {
        return Button(this).apply {
            text = label
            textSize = 16f
            isAllCaps = false
            setOnClickListener { action() }

            root.addView(
                this,
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    topMargin = 8
                    bottomMargin = 8
                }
            )
        }
    }

    private fun showWelcome() {
        setupScreen("Life Hack or Urban Myth?")
        addText(
            "Test your knowledge with 10 everyday life and health facts. " +
                "Decide whether each statement is a useful fact or a myth.",
            18f
        )
        addButton("Start Quiz") {
            questionIndex = 0
            score = 0
            showQuestion()
        }
    }

    private fun showQuestion() {
        answered = false
        setupScreen("Question ${questionIndex + 1} of ${questions.size}")

        scoreText = addText("Score: $score", 16f)
        questionText = addText(questions[questionIndex].first, 23f)

        hackButton = addButton("Life Hack — TRUE") {
            checkAnswer(true)
        }

        mythButton = addButton("Urban Myth — FALSE") {
            checkAnswer(false)
        }

        feedbackText = addText("", 17f)
        nextButton = addButton("Next Question") {
            if (questionIndex < questions.lastIndex) {
                questionIndex++
                showQuestion()
            } else {
                showResults()
            }
        }

        nextButton.isEnabled = false
    }

    private fun checkAnswer(answer: Boolean) {
        if (answered) return
        answered = true

        val correct = questions[questionIndex].second
        if (answer == correct) {
            score++
            feedbackText.text = "Correct! Well done."
        } else {
            feedbackText.text = "Not quite! " +
                if (correct) "This is TRUE." else "This is FALSE."
        }

        scoreText.text = "Score: $score"
        hackButton.isEnabled = false
        mythButton.isEnabled = false
        nextButton.isEnabled = true
        nextButton.text =
            if (questionIndex == questions.lastIndex) "See Results"
            else "Next Question"
    }

    private fun showResults() {
        setupScreen("Quiz Complete!")
        addText("Your score: $score out of ${questions.size}", 25f)

        val message = when {
            score >= 8 -> "Excellent! You know your facts."
            score >= 5 -> "Good effort! Keep learning."
            else -> "Keep practising and checking reliable sources."
        }

        addText(message, 19f)

        addButton("Review Answers") {
            showReview()
        }

        addButton("Play Again") {
            questionIndex = 0
            score = 0
            showQuestion()
        }
    }

    private fun showReview() {
        setupScreen("Answer Guide")

        questions.forEachIndexed { index, question ->
            addText(
                "${index + 1}. ${question.first}\n" +
                    "Answer: ${if (question.second) "TRUE — Life Hack" else "FALSE — Urban Myth"}",
                15f
            )
        }

        addButton("Play Again") {
            questionIndex = 0
            score = 0
            showQuestion()
        }
    }
}
