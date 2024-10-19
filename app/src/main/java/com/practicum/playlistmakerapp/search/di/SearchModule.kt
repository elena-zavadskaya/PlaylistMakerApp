package com.practicum.playlistmakerapp.search.di

import android.content.Context
import com.practicum.playlistmakerapp.search.data.impl.SearchHistoryRepositoryImpl
import com.practicum.playlistmakerapp.search.data.network.NetworkClient
import com.practicum.playlistmakerapp.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmakerapp.search.data.network.SearchRepositoryImpl
import com.practicum.playlistmakerapp.search.domain.SearchHistoryInteractor
import com.practicum.playlistmakerapp.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmakerapp.search.domain.api.SearchInteractor
import com.practicum.playlistmakerapp.search.domain.api.SearchRepository
import com.practicum.playlistmakerapp.search.domain.impl.SearchHistoryInteractorImpl
import com.practicum.playlistmakerapp.search.domain.impl.SearchInteractorImpl
import com.practicum.playlistmakerapp.search.ui.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchModule = module {

    single<SearchRepository> { SearchRepositoryImpl(get()) }

    single<SearchInteractor> { SearchInteractorImpl(get()) }

    single<NetworkClient> { RetrofitNetworkClient() }

    single { androidContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE) }

    single<SearchHistoryRepository> { SearchHistoryRepositoryImpl(get()) }

    single<SearchHistoryInteractor> { SearchHistoryInteractorImpl(get()) }

    viewModel { SearchViewModel(get(), get(), get()) }
}