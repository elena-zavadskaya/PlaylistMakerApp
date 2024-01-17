package com.practicum.playlistmakerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout

class SearchActivity : AppCompatActivity() {

    private lateinit var searchValue: String
    private lateinit var inputEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //val linearLayout = findViewById<LinearLayout>(R.id.container)
        inputEditText = findViewById(R.id.inputEditText)
        val backButton = findViewById<ImageView>(R.id.back_button)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)

        backButton.setOnClickListener {
            val displayIntent = Intent(this, MainActivity::class.java)
            startActivity(displayIntent)
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                TODO()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchValue = p0.toString()
                clearButton.visibility = clearButtonVisibility(p0)
            }

            override fun afterTextChanged(p0: Editable?) {
                TODO()
            }

        }

        inputEditText.addTextChangedListener(simpleTextWatcher)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        inputEditText = findViewById(R.id.inputEditText)
        searchValue = inputEditText.text.toString()
        outState.putString(KEY, searchValue)
    }

    companion object {
        const val KEY = "KEY"
        //const val DEFAULT_VALUE = ""
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputEditText = findViewById(R.id.inputEditText)
        searchValue = savedInstanceState.getString(KEY).toString()
        inputEditText.setText(searchValue)
    }
}