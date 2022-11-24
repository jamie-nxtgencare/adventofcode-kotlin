import java.util.*

class DayTwentytwo(val file: String): Project {
    private var debug = false
    private var gameCounter = 2

    override fun part1(): Any {
        val winningDeck = playGame(whitelineSeperatedGrouper(file, { Deck(it) }, { it }), 1)

        var sum = 0
        for (i in winningDeck.cards.size downTo 1) {
            sum += winningDeck.cards.remove() * i
        }
        return sum
    }

    override fun part2(): Any {
        debug = false
        val winningDeck = playGame(whitelineSeperatedGrouper(file, { Deck(it) }, { it }), 2)

        var sum = 0
        for (i in winningDeck.cards.size downTo 1) {
            sum += winningDeck.cards.remove() * i
        }
        return sum
    }

    private fun playGame(decks: List<Deck>, part: Int, gameNum: Int = 1): Deck {
        if (debug) {
            println("=== Game $gameNum ===")
        }

        val gamesMap: HashMap<Int, Int> = HashMap()
        var instantWin = false
        var round = 1

        while (decks[0].cards.size > 0 && decks[1].cards.size > 0 && !instantWin) {
            if (debug) {
                println("\n-- Round $round (Game $gameNum) --")
                println("Player 1's deck: " + decks[0].cards.joinToString())
                println("Player 2's deck: " + decks[1].cards.joinToString())
            }

            if (part == 1) {
                determinePart1RoundWinner(decks)
            } else {
                instantWin = determinePart2RoundWinner(gamesMap, decks, gameNum, round)
            }
            round++
        }

        val winningDeck = if (decks[0].cards.size > 0 || instantWin) decks[0] else decks[1]
        val winningPlayerNum = winningDeck.player

        if (debug) {
            println("The winner of game $gameNum is player $winningPlayerNum!")
        }

        return winningDeck
    }

    private fun determinePart1RoundWinner(decks: List<Deck>) {
        val deck0Card = decks[0].cards.remove()
        val deck1Card = decks[1].cards.remove()

        if (deck0Card > deck1Card) {
            decks[0].cards.add(deck0Card)
            decks[0].cards.add(deck1Card)
        } else if (deck1Card > deck0Card) {
            decks[1].cards.add(deck1Card)
            decks[1].cards.add(deck0Card)
        }
    }

    private fun determinePart2RoundWinner(roundsMap: HashMap<Int, Int>, decks: List<Deck>, gameNum: Int, round: Int): Boolean {
        val roundKey = decks[0].hashCode()+decks[1].hashCode()

        if (roundsMap.containsKey(roundKey)) {
            return true
        }

        roundsMap[roundKey] = 1

        val deck0Card = decks[0].cards.remove()
        val deck1Card = decks[1].cards.remove()
        if (debug) {
            println("Player 1 plays: $deck0Card")
            println("Player 2 plays: $deck1Card")
        }

        var winningPlayer = decks[0].player
        if (decks[0].cards.size >= deck0Card && decks[1].cards.size >= deck1Card) {
            // Recursive combat!
            val deck0Copy = Deck(decks[0], deck0Card)
            val deck1Copy = Deck(decks[1], deck1Card)

            if (debug) {
                println("Playing a sub-game to determine the winner...")
            }
            val subGameWinner = playGame(listOf(deck0Copy, deck1Copy), 2, gameCounter++)
            if (debug) {
                println("\n...anyway, back to game $gameNum.")
            }

            if (subGameWinner.player == 1)  {
                decks[0].cards.add(deck0Card)
                decks[0].cards.add(deck1Card)
            } else {
                decks[1].cards.add(deck1Card)
                decks[1].cards.add(deck0Card)
                winningPlayer = decks[1].player
            }
        } else {
            if (deck0Card > deck1Card) {
                decks[0].cards.add(deck0Card)
                decks[0].cards.add(deck1Card)
            } else {
                decks[1].cards.add(deck1Card)
                decks[1].cards.add(deck0Card)
                winningPlayer = decks[1].player
            }

        }

        if (debug) {
            println("Player $winningPlayer wins round $round of game $gameNum!")
        }

        return false
    }
}

class Deck() {
    var player = 0
    val cards = LinkedList<Int>()

    constructor(lines: List<String>): this() {
        lines.forEach {
            if (it.startsWith("Player")) {
                player = it.split("Player ")[1].trim(':').toInt()
            } else {
                cards.add(it.toInt())
            }
        }
    }

    constructor(deck: Deck, quantity: Int): this() {
        player = deck.player
        cards.addAll(deck.cards.subList(0, quantity))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Deck) return false

        if (player != other.player) return false
        if (cards != other.cards) return false

        return true
    }

    override fun hashCode(): Int {
        return player + cards.fold(0, { it, it2 -> (it + it2) shl 2 })
    }
}