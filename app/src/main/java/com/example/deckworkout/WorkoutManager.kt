package com.example.deckworkout

/**
 * Verwaltet das 52-kartige Deck, die aktuelle Position und die Übungsreihenfolge.
 * Reihenfolge: Push-ups → Pull-ups → Dips → Push-ups → ...
 */
class WorkoutManager {
    private val cards: MutableList<Card> = mutableListOf()
    private var currentIndex: Int = -1
    private val totalPushUps: MutableInt = MutableInt(0)
    private val totalPullUps: MutableInt = MutableInt(0)
    private val totalDips: MutableInt = MutableInt(0)

    init {
        shuffleDeck()
    }

    private fun buildDeck() {
        cards.clear()
        val suits = listOf(Suit.HEARTS, Suit.DIAMONDS, Suit.CLUBS, Suit.SPADES)
        val ranks = listOf(
            Rank.TWO, Rank.THREE, Rank.FOUR, Rank.FIVE, Rank.SIX,
            Rank.SEVEN, Rank.EIGHT, Rank.NINE, Rank.TEN,
            Rank.JACK, Rank.QUEEN, Rank.KING, Rank.ACE
        )

        val cardPairs = mutableListOf<Pair<Suit, Rank>>()
        for (suit in suits) {
            for (rank in ranks) {
                cardPairs.add(Pair(suit, rank))
            }
        }

        // Spielkarten zufällig mischen
        cardPairs.shuffle()

        // Feste Reihenfolge zuweisen: Push-ups -> Pull-ups -> Dips -> Push-ups -> ...
        val exerciseList = exercises()
        for ((index, pair) in cardPairs.withIndex()) {
            val exercise = exerciseList[index % exerciseList.size]
            cards.add(Card(pair.first, pair.second, exercise))
        }
    }

    private var completedCardsCount: Int = 0

    private fun shuffleDeck() {
        buildDeck()
        currentIndex = -1
        completedCardsCount = 0
        totalPushUps.value = 0
        totalPullUps.value = 0
        totalDips.value = 0
    }

    private fun exercises(): List<Exercise> {
        return listOf(Exercise.PUSH_UPS, Exercise.PULL_UPS, Exercise.DIPS)
    }

    /** Gibt true zurück, wenn noch Karten im Deck sind. */
    fun hasNext(): Boolean = currentIndex + 1 < cards.size

    /** Zieht die nächste Karte aus dem Deck. */
    fun nextCard(): Card? {
        if (!hasNext()) return null
        currentIndex++
        return cards[currentIndex]
    }

    /** Markiert die aktuelle Karte als erledigt und addiert die Wiederholungen zur Statistik. */
    fun completeCurrentCard(repsDone: Int? = null) {
        val card = currentCard() ?: return
        val reps = repsDone ?: card.repetitions()
        when (card.exercise) {
            Exercise.PUSH_UPS -> totalPushUps.value += reps
            Exercise.PULL_UPS -> totalPullUps.value += reps
            Exercise.DIPS -> totalDips.value += reps
        }
        completedCardsCount++
    }

    fun getCompletedCardsCount(): Int = completedCardsCount

    /** Gibt die aktuell gezogene Karte zurück (oder null, wenn noch keine gezogen wurde). */
    fun currentCard(): Card? {
        if (currentIndex < 0 || currentIndex >= cards.size) return null
        return cards[currentIndex]
    }

    /** Gibt den aktuellen Karten-Index zurück (0-basiert). */
    fun currentCardIndex(): Int = currentIndex

    /** Gibt die Gesamtzahl der verbleibenden Karten zurück. */
    fun remainingCards(): Int = cards.size - (currentIndex + 1)

    fun getTotalPushUps(): Int = totalPushUps.value
    fun getTotalPullUps(): Int = totalPullUps.value
    fun getTotalDips(): Int = totalDips.value
    fun getTotalRepetitions(): Int = totalPushUps.value + totalPullUps.value + totalDips.value
    fun getTotalCards(): Int = cards.size
}

/** Einfacher Wrapper für mutable Int (da Kotlin kein primitives mutable int hat). */
private class MutableInt(var value: Int)