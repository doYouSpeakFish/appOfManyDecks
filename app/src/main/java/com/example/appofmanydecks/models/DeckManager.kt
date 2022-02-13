package com.example.appofmanydecks.models

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.Serializable

class DeckManager(initialDeckState: Deck) {
    private val _deckState = MutableStateFlow(initialDeckState)
    val deckState = _deckState.asStateFlow()

    val history = mutableListOf<List<CardEvent>>()
    val undone = mutableListOf<List<CardEvent>>()

    fun undo() {
        history.firstOrNull()?.let { events ->
            events.forEach { event ->
                when (event) {
                    is CardEvent.Add -> removeCard(event.card)
                    is CardEvent.Remove -> addCard(event.card)
                    is CardEvent.Pull -> _deckState.value.run { copy(pulls = pulls - event.card) }
                }
            }
            undone.add(events)
        }
    }

    fun redo() {
        undone.firstOrNull()?.let { events ->
            events.forEach { event ->
                when (event) {
                    is CardEvent.Add -> addCard(event.card)
                    is CardEvent.Pull -> removeCard(event.card)
                    is CardEvent.Remove -> _deckState.value.run { copy(cards = cards - event.card) }
                }
            }
            history.add(events)
        }
    }

    fun addCard(card: Card) {
        _deckState.value = _deckState.value.run { copy(cards = cards + card) }
    }

    fun removeCard(card: Card) {
        _deckState.value = _deckState.value.run { copy(cards = cards - card) }
    }

    fun pullCard() {
        val card = deckState.value.cards.random()
        when (deckState.value.discardOnPull) {
            Deck.DiscardOnPull.YES -> {
                removeCard(card)
                CardPull.PullAndDiscard(card)
            }
            Deck.DiscardOnPull.NO -> CardPull.PullAndPutBack(card)
            Deck.DiscardOnPull.USER_DECIDES -> CardPull.Pull(card) { discard ->
                if (discard) removeCard(card)
            }
        }
    }

    sealed class CardPull {
        abstract val card: Card
        data class PullAndDiscard(override val card: Card) : CardPull()
        data class PullAndPutBack(override val card: Card) : CardPull()
        data class Pull(override val card: Card, val discard: (Boolean) -> Unit) : CardPull()
    }

    sealed class CardEvent {
        abstract val card: Card
        data class Add(override val card: Card) : CardEvent()
        data class Remove(override val card: Card) : CardEvent()
        data class Pull(override val card: Card) : CardEvent()
    }
}

@Serializable
data class Deck(
    val name: String,
    val type: DeckType,
    var cards: List<Card>,
    val pulls: List<Card>,
    val discardOnPull: DiscardOnPull,
) {
    enum class DiscardOnPull { YES, NO, USER_DECIDES }
    fun addCard(card: Card): Deck {
        require(card in type.cards) {
            "Card: ${card.name} does not belong to deck type: ${type.name}"
        }
        return this.copy(cards = cards + card)
    }

    fun removeCard(card: Card): Deck {
        require(card in cards) {
            "Cannot remove card that is not in this deck. Card: ${card.name}, deck: $name"
        }
        return this.copy(cards = cards - card)
    }
}

@Serializable
data class DeckType(val name: String, val cards: List<Card>)

@Serializable
data class Card(val name: String, val description: String)
