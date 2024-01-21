package com.practicum.playlistmakerapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputBinding
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import com.practicum.playlistmakerapp.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    private lateinit var searchValue: String
    private lateinit var inputEditText: EditText
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //val linearLayout = findViewById<LinearLayout>(R.id.container)
        inputEditText = binding.inputEditText
        val backButton = binding.backButton
        val clearButton = binding.clearIcon

        backButton.setOnClickListener {
            val displayIntent = Intent(this, MainActivity::class.java)
            startActivity(displayIntent)
            finish()
        }

        fun onBackPressed() {
            val displayIntent = Intent(this, MainActivity::class.java)
            startActivity(displayIntent)
            finish()
        }

        clearButton.setOnClickListener {
            searchValue = inputEditText.setText("").toString()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchValue = p0.toString()
                clearButton.visibility = clearButtonVisibility(p0)
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchValue = p0.toString()
                clearButton.visibility = clearButtonVisibility(p0)
            }

            override fun afterTextChanged(p0: Editable?) {
                searchValue = p0.toString()
                clearButton.visibility = clearButtonVisibility(p0)
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
        searchValue = inputEditText.text.toString()
        outState.putString(KEY, searchValue)
    }

    companion object {
        private const val KEY = "KEY"
        //const val DEFAULT_VALUE = ""
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchValue = savedInstanceState.getString(KEY).toString()
        inputEditText.setText(searchValue)
    }
}