@file:Suppress("PackageName")

package `2023`

import Project
import java.lang.Character.isDigit
import java.lang.Integer.parseInt

class DaySeven(file: String, isTest: Boolean = false) : Project(file, isTest) {
    val lines = getLines(file).map { it.split(" ") }
    val hands = lines.map { Hand(it[0].toList(), parseInt(it[1]))  }

    class Hand(val cards: List<Char>, val bid: Int): Comparable<Hand> {
        private val cardCounts = HashMap<Char, Int>()
        private val jokerLessCardCounts = HashMap<Char, Int>()
        private var jokers = 0
        var rank: Int = 0
        var useJokers = false

        init {
            cards.forEach {
                cardCounts.merge(it, 1, Integer::sum)
                if (it != 'J') {
                    jokerLessCardCounts.merge(it, 1, Integer::sum)
                } else {
                    jokers++
                }
            }
            rank = computeRank()
        }

        fun computeRank(): Int {
            if (!useJokers) {
                if (cardCounts.any { it.value == 5 }) {
                    return 6 // 5 of a kind
                } else if (cardCounts.any { it.value == 4 }) {
                    return 5 // 4 of a kind
                } else if (cardCounts.any { it.value == 3 } && cardCounts.any { it.value == 2 }) {
                    return 4 // full house
                } else if (cardCounts.any { it.value == 3 }) {
                    return 3 // 3 of a kind
                } else if (cardCounts.filter { it.value == 2 }.size == 2) {
                    return 2 // 2 pair
                } else if (cardCounts.any { it.value == 2 }) {
                    return 1 // pair
                }
            } else {
                if (jokerLessCardCounts.any { it.value + jokers == 5} || jokers == 5) {
                    return 6 // 5 of a kind
                } else if (jokerLessCardCounts.any { it.value + jokers == 4 }) {
                    return 5 // 4 of a kind
                } else if (jokerLessCardCounts.any { it.value == 3 } && jokerLessCardCounts.any { it.value == 2 } || (jokerLessCardCounts.filter { it.value == 2 }.size == 2 && jokers == 1)) {
                    return 4 // full house
                } else if (jokerLessCardCounts.any { it.value + jokers == 3 }) {
                    return 3 // 3 of a kind
                } else if (jokerLessCardCounts.filter { it.value == 2 }.size == 2) {
                    return 2 // 2 pair
                } else if (jokerLessCardCounts.any { it.value + jokers == 2 }) {
                    return 1 // pair
                }
            }

            return 0
        }

        override fun compareTo(other: Hand): Int {
            if (rank == other.rank) {
                for (i in cards.indices) {
                    val rank1 = getCardRank(cards[i])
                    val rank2 = getCardRank(other.cards[i])

                    if (rank1 != rank2) {
                        return rank1 - rank2
                    }
                }
            }

            return rank - other.rank
        }

        private fun getCardRank(card: Char): Int {
            if (isDigit(card)) {
                return parseInt(card.toString())
            }

            when (card) {
                'T' -> return 10
                'J' -> return if (useJokers) 1 else 11
                'Q' -> return 12
                'K' -> return 13
                'A' -> return 14
            }

            return -1
        }
    }

    override suspend fun part1(): Any {
        val sortedHands = hands.sorted()

        var sum = 0

        for (i in sortedHands.indices) {
            val rank = i + 1
            sum += sortedHands[i].bid * rank
        }

        return sum
    }

    override suspend fun part2(): Any {
        hands.forEach {
            it.useJokers = true
            it.rank = it.computeRank()
        }
        val sortedHands = hands.sorted()
        var sum = 0

        for (i in sortedHands.indices) {
            val rank = i + 1
            sum += sortedHands[i].bid * rank
        }

        return sum
    }

}