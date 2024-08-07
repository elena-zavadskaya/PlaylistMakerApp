package com.practicum.playlistmakerapp.domain.usecases

interface UseCase<in Params, out Type> {
    fun execute(params: Params): Type
}