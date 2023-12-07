@file:Suppress("PackageName")

package `2023`

import Project
import java.lang.Character.isDigit
import java.lang.Integer.parseInt

class DaySeven(file: String) : Project() {
    val lines = getLines(file).map { it.split(" ") }
    val hands = lines.map { Hand(it[0].toList(), parseInt(it[1]))  }

    class Hand(val cards: List<Char>, val bid: Int): Comparable<Hand> {
        private val cardCounts = HashMap<Char, Int>()
        private val rank: Int

        init {
            cards.forEach {
                cardCounts.merge(it, 1, Integer::sum)
            }
            rank = getRank()
        }

        fun getRank(): Int {
            if (cardCounts.any { it.value == 5 }) {
                return 6 // 5 of a kind
            } else if (cardCounts.any { it.value == 4 }) {
                return 5 // 4 of a kind
            } else if (cardCounts.any { it.value == 3 } && cardCounts.any { it.value == 2}) {
                return 4 // full house
            } else if (cardCounts.any { it.value == 3 }) {
                return 3 // 3 of a kind
            } else if (cardCounts.filter { it.value == 2 }.size == 2) {
                return 2 // 2 pair
            } else if (cardCounts.any { it.value == 2 }) {
                return 1 // pair
            }
            return 0
        }

        override fun compareTo(hand: Hand): Int {
            if (rank == hand.rank) {
                for (i in cards.indices) {
                    val rank1 = getCardRank(cards[i])
                    val rank2 = getCardRank(hand.cards[i])

                    if (rank1 != rank2) {
                        return rank1 - rank2
                    }
                }
            }

            return rank - hand.rank
        }

        private fun getCardRank(card: Char): Int {
            if (isDigit(card)) {
                return parseInt(card.toString())
            }

            when (card) {
                'T' -> return 10
                'J' -> return 11
                'Q' -> return 12
                'K' -> return 13
                'A' -> return 14
            }

            return -1
        }
    }

    override fun part1(): Any {
        val sortedHands = hands.sorted()

        var sum = 0

        for (i in sortedHands.indices) {
            val rank = i + 1
            sum += sortedHands[i].bid * rank
        }

        return sum
    }

    override fun part2(): Any {
        return -1
    }

}