package com.example.appofmanydecks

sealed class Screen(val route: String) {
    object DecksList : Screen("DecksList")
}
