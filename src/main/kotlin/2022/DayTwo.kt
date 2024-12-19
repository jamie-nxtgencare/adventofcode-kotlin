@file:Suppress("PackageName")

package `2022`

import Project

class DayTwo(file: String, isTest: Boolean = false) : Project(file, isTest) {
    val games = mapFileLines(file) { RPSGame(it) }

    enum class Move() {
        ROCK,
        PAPER,
        SCISSORS
    }

    enum class Outcome() {
        LOSS,
        DRAW,
        WIN
    }

    class RPSGame(game: String) {
        private val theirMove: Move = if (game[0] == 'A') Move.ROCK else if (game[0] == 'B') Move.PAPER else Move.SCISSORS
        private val ourMove: Move = if (game[2] == 'X') Move.ROCK else if (game[2] == 'Y') Move.PAPER else Move.SCISSORS
        private val outcome: Outcome = if (game[2] == 'X') Outcome.LOSS else if (game[2] == 'Y') Outcome.DRAW else Outcome.WIN
        private val ourMove2: Move = moveForOutcome(outcome)

        private fun ourScore(): Int {
            return if (ourMove == Move.ROCK) 1 else if (ourMove == Move.PAPER) 2 else 3
        }

        private fun ourScore2(): Int {
            return if (ourMove2 == Move.ROCK) 1 else if (ourMove2 == Move.PAPER) 2 else 3
        }

        private fun outcome(move2: Move) : Int {
            if (theirMove == Move.ROCK) {
                if (move2 == Move.PAPER) {
                    return 6
                } else if (move2 == Move.SCISSORS) {
                    return 0
                }
                return 3
            }

            if (theirMove == Move.PAPER) {
                if (move2 == Move.PAPER) {
                    return 3
                } else if (move2 == Move.SCISSORS) {
                    return 6
                }
                return 0
            }

            // SCISSORS
            if (move2 == Move.PAPER) {
                return 0
            } else if (move2 == Move.SCISSORS) {
                return 3
            }
            return 6
        }

        private fun moveForOutcome(outcome: Outcome) : Move {
            if (theirMove == Move.ROCK) {
                if (outcome == Outcome.WIN) {
                    return Move.PAPER
                } else if (outcome == Outcome.DRAW) {
                    return Move.ROCK
                }
                return Move.SCISSORS
            }

            if (theirMove == Move.PAPER) {
                if (outcome == Outcome.WIN) {
                    return Move.SCISSORS
                } else if (outcome == Outcome.DRAW) {
                    return Move.PAPER
                }
                return Move.ROCK
            }

            // SCISSORS
            if (outcome == Outcome.WIN) {
                return Move.ROCK
            } else if (outcome == Outcome.DRAW) {
                return Move.SCISSORS
            }
            return Move.PAPER
        }

        fun matchScore(): Int {
            return ourScore() + outcome(ourMove)
        }

        fun matchScore2(): Int {
            return ourScore2() + outcome(ourMove2)
        }
    }

    override suspend fun part1(): Any {
       return games.sumOf { it.matchScore() }
    }

    override suspend fun part2(): Any {
        return games.sumOf { it.matchScore2() }
    }

}