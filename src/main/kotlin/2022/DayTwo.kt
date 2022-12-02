@file:Suppress("PackageName")

package `2022`

import Project

class DayTwo(file: String) : Project {
    val games = mapFileLines(file) { RPSGame(it) }

    enum class Move() {
        ROCK,
        PAPER,
        SCISSORS
    }

    class RPSGame(game: String) {
        private val theirMove: Move = if (game[0] == 'A') Move.ROCK else if (game[0] == 'B') Move.PAPER else Move.SCISSORS;
        private val ourMove: Move = if (game[2] == 'X') Move.ROCK else if (game[2] == 'Y') Move.PAPER else Move.SCISSORS;

        private fun ourScore(): Int {
            return if (ourMove == Move.ROCK) 1 else if (ourMove == Move.PAPER) 2 else 3;
        }

        private fun outcomeScore() : Int {
            if (theirMove == Move.ROCK) {
                if (ourMove == Move.PAPER) {
                    return 6
                } else if (ourMove == Move.SCISSORS) {
                    return 0
                }
                return 3
            }

            if (theirMove == Move.PAPER) {
                if (ourMove == Move.PAPER) {
                    return 3
                } else if (ourMove == Move.SCISSORS) {
                    return 6
                }
                return 0
            }

            // SCISSORS
            if (ourMove == Move.PAPER) {
                return 0
            } else if (ourMove == Move.SCISSORS) {
                return 3
            }
            return 6
        }

        fun matchScore(): Int {
            return ourScore() + outcomeScore()
        }
    }

    override fun part1(): Any {
       return games.sumOf { it.matchScore() }
    }

    override fun part2(): Any {
        return -1
    }

}