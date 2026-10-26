package com.example.deckworkout

/**
 * Eine einzelne Spielkarte mit Farbe, Wert und zugehöriger Übung.
 */
data class Card(
    val suit: Suit,
    val rank: Rank,
    val exercise: Exercise
) {
    /**
     * Gibt die Wiederholungsanzahl für diese Karte zurück.
     * 2–10 = Kartenwert, Bube/Dame/König = 10, Ass = 1.
     */
    fun repetitions(): Int {
        return when (rank) {
            Rank.ACE -> 1
            Rank.JACK, Rank.QUEEN, Rank.KING -> 10
            Rank.TWO -> 2
            Rank.THREE -> 3
            Rank.FOUR -> 4
            Rank.FIVE -> 5
            Rank.SIX -> 6
            Rank.SEVEN -> 7
            Rank.EIGHT -> 8
            Rank.NINE -> 9
            Rank.TEN -> 10
        }
    }

    fun displayName(): String = "${rank.symbol}${suit.symbol}"

    fun colorResId(): Int {
        return when (suit) {
            Suit.HEARTS, Suit.DIAMONDS -> R.color.colorRed
            Suit.CLUBS, Suit.SPADES -> R.color.colorBlack
        }
    }
}

/**
 * Die vier Farben eines Spielkarten-Decks.
 */
enum class Suit(val symbol: String) {
    HEARTS("♥"),
    DIAMONDS("♦"),
    CLUBS("♣"),
    SPADES("♠");
}

/**
 * Die 13 Werte eines Spielkarten-Decks.
 */
enum class Rank(val symbol: String) {
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9"),
    TEN("10"),
    JACK("J"),
    QUEEN("Q"),
    KING("K"),
    ACE("A");
}

/**
 * Die drei möglichen Übungen im Deck mit übersetzbaren String-Ressourcen.
 */
enum class Exercise(val nameResId: Int) {
    PUSH_UPS(R.string.label_pushups),
    PULL_UPS(R.string.label_pullups),
    DIPS(R.string.label_dips);
}
