package com.practicum.playlistmakerapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmakerapp.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var searchValue: String
    private lateinit var binding: ActivitySearchBinding

    private val trackBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(trackBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val searchService = retrofit.create(ITunesApi::class.java)
    private val tracks: MutableList<Track> = mutableListOf()
    private val trackAdapter = TrackAdapter(tracks)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        /*tracks.add(
            Track(
                "Smells Like Teen Spirit",
                "Nirvana",
                "5:01",
                "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
            )
        )
        tracks.add(
            Track(
                "Billie Jean",
                "Michael Jackson",
                "4:35",
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
            )
        )
        tracks.add(
            Track(
                "Stayin' Alive",
                "Bee Gees",
                "4:10",
                "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
            )
        )
        tracks.add(
            Track(
                "Whole Lotta Love",
                "Led Zeppelin",
                "5:33",
                "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
            )
        )
        tracks.add(
            Track(
                "Sweet Child O'Mine",
                "Guns N' Roses",
                "5:03",
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"
            )
        )*/

        binding.recyclerView.adapter = trackAdapter

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.clearIcon.setOnClickListener {
            tracks.clear()
            searchValue = binding.inputEditText.setText("").toString()
            showEmptyPage()
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchValue = p0.toString()
                binding.clearIcon.isVisible = clearButtonVisibility(p0)
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchValue = p0.toString()
                binding.clearIcon.isVisible = clearButtonVisibility(p0)
            }

            override fun afterTextChanged(p0: Editable?) {
                searchValue = p0.toString()
                binding.clearIcon.isVisible = clearButtonVisibility(p0)
            }

        }

        binding.inputEditText.addTextChangedListener(simpleTextWatcher)

        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
                true
            }
            false
        }

        binding.reloadButton.setOnClickListener {
            search()
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Boolean {
        return !s.isNullOrEmpty()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        searchValue = binding.inputEditText.text.toString()
        outState.putString(KEY, searchValue)
    }

    companion object {
        private const val KEY = "KEY"
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchValue = savedInstanceState.getString(KEY).toString()
        binding.inputEditText.setText(searchValue)
    }

    private fun search() {
        if (searchValue.isNotEmpty()) {
            searchService
                .trackSearch(searchValue)
                .enqueue(object : Callback<TrackResponse> {
                    override fun onResponse(
                        call: Call<TrackResponse>,
                        response: Response<TrackResponse>
                    ) {
                        checkResponse(response)
                    }

                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        showInternetErrorPage()
                    }
                })
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun checkResponse(response:  Response<TrackResponse>) {
        if (response.code() == 200) {
            tracks.clear()
            if (response.body()?.results?.isNotEmpty() == true) {
                tracks.addAll(response.body()?.results!!)
                trackAdapter.notifyDataSetChanged()
                showTrackList()
            } else if (tracks.isEmpty()) {
                showNotFoundPage()
            }
        }
    }

    private fun showTrackList() {
        binding.recyclerView.isVisible = true
        binding.notFound.isVisible = false
        binding.internetError.isVisible = false
    }

    private fun showNotFoundPage() {
        binding.recyclerView.isVisible = false
        binding.notFound.isVisible = true
        binding.internetError.isVisible = false
    }

    private fun showInternetErrorPage() {
        binding.recyclerView.isVisible = false
        binding.notFound.isVisible = false
        binding.internetError.isVisible = true
    }

    private fun showEmptyPage() {
        binding.recyclerView.isVisible = false
        binding.notFound.isVisible = false
        binding.internetError.isVisible = false
    }
}