package com.example.appofmanydecks.ui.deckslist

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DecksListViewModel @Inject constructor() : ViewModel() {
    val state = MutableStateFlow(DecksListUiState(listOf("Deck 1", "Deck 2"))).asStateFlow()
}
