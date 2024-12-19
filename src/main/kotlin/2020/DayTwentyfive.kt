@file:Suppress("PackageName")

package `2020`

import Project

class DayTwentyfive(file: String): Project() {
    private val lines = getLines(file)
    val cardPubKey = lines[0].toLong()
    val doorPubKey = lines[1].toLong()

    override suspend fun part1(): Any {
        var loop = 0L
        var cardLoop = 0L
        var doorLoop = 0L
        var subject = 7L
        var n = 1L

        while (cardLoop == 0L || doorLoop == 0L) {
            n *= subject
            n %= 20201227L
            loop++

            if (n == cardPubKey) {
                cardLoop = loop
            }

            if (n == doorPubKey) {
                doorLoop = loop
            }
        }

        subject = doorPubKey
        loop = 0L
        n = 1L
        while (loop < cardLoop) {
            n *= subject
            n %= 20201227L
            loop++
        }

        return n
    }

    override suspend fun part2(): Any {
        return -1
    }
}