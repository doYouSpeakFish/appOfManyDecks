package com.example.appofmanydecks.ui.deckslist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.appofmanydecks.extensions.collectAsStateWithLifecycle

@Composable
fun DecksList(modifier: Modifier = Modifier, deckNames: List<String>) {
    Column(modifier) {
        deckNames.forEach {
            Text(text = it)
        }
    }
}

@Composable
fun DecksList(
    modifier: Modifier = Modifier,
    viewModel: DecksListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle(initial = null)
    DecksList(modifier = modifier, deckNames = state?.deckNames ?: emptyList())
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    DecksList(Modifier.fillMaxSize(), deckNames = listOf("Deck 1", "Deck 2"))
}
