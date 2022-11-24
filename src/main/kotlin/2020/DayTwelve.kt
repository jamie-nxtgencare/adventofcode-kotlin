@file:Suppress("PackageName")

package `2020`

import Project
import kotlin.math.abs

class DayTwelve(file: String) : Project {
    val debug = false
    val directions = mapFileLines(file) { Direction(it[0], it.removeRange(0,1).toInt()) }

    override fun part1(): Any {
        var facing = 90
        var x = 0
        var y = 0

        directions.forEach {
            if (debug) {
                println(it.command + " " + it.amount.toString())
            }

            var doCommand = it
            if (doCommand.command == 'F') {
                val dir = if (facing == 90) 'E' else if (facing == 180) 'S' else if (facing == 270) 'W' else 'N'
                doCommand = Direction(dir, it.amount)
            }

            y = when(doCommand.command) {
                'N' -> y + it.amount
                'S' -> y - it.amount
                else -> y
            }

            x = when(doCommand.command) {
                'E' -> x + it.amount
                'W' -> x - it.amount
                else -> x
            }

            facing = when(doCommand.command) {
                'L' -> (facing - it.amount) % 360
                'R' -> (facing + it.amount) % 360
                else -> facing
            }

            if (facing < 0) {
                facing += 360
            }

            if (debug) {
                println("facing: $facing | $x,$y")
            }
        }

        return abs(x) + abs(y)
    }

    override fun part2(): Any {
        var wx = 10
        var wy = 1

        var x = 0
        var y = 0

        directions.forEach {
            if (debug) {
                println(it.command + " " + it.amount.toString())
            }

            if (it.command == 'F') {
                x += wx * it.amount
                y += wy * it.amount
            }

            wy = when(it.command) {
                'N' -> wy + it.amount
                'S' -> wy - it.amount
                else -> wy
            }

            wx = when(it.command) {
                'E' -> wx + it.amount
                'W' -> wx - it.amount
                else -> wx
            }

            when(it.command) {
                'L' -> {
                    when (it.amount) {
                        90 -> {
                            val tmp = wx
                            wx = wy * -1
                            wy = tmp
                        }
                        180 -> {
                            wx *= -1
                            wy *= -1
                        }
                        else -> {
                            val tmp = wx
                            wx = wy
                            wy = tmp * -1
                        }
                    }
                }
                'R' -> {
                    when (it.amount) {
                        90 -> {
                            val tmp = wx
                            wx = wy
                            wy = tmp * -1
                        }
                        180 -> {
                            wx *= -1
                            wy *= -1
                        }
                        else -> {
                            val tmp = wx
                            wx = wy * -1
                            wy = tmp
                        }
                    }
                }
            }

            if (debug) {
                println("$wx,$wy | $x,$y")
            }
        }

        return abs(x) + abs(y)
    }

}

class Direction(val command: Char, val amount: Int) {

}
