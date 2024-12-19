@file:Suppress("PackageName")

package `2023`

import Project
import java.lang.Integer.parseInt

class DayOne(file: String, isTest: Boolean = false) : Project(file, isTest) {
	val lines = getLines(file)

	val numbers = mapOf(
		"0" to 0,
		"1" to 1,
		"2" to 2,
		"3" to 3,
		"4" to 4,
		"5" to 5,
		"6" to 6,
		"7" to 7,
		"8" to 8,
		"9" to 9,
		"zero" to 0,
		"one" to 1,
		"two" to 2,
		"three" to 3,
		"four" to 4,
		"five" to 5,
		"six" to 6,
		"seven" to 7,
		"eight" to 8,
		"nine" to 9
	)

	private fun myIsDigit(it: String, i: Int, reversed: Boolean): Boolean {
		val substring = it.substring(i)

		for (number in numbers) {
			val s = if (reversed) number.key.reversed() else number.key
			if (substring.startsWith(s)) {
				return true
			}
		}

		return false
	}

	private fun getDigit(it: String, i: Int, reversed: Boolean): Int {
		val substring = it.substring(i)

		for (number in numbers) {
			val s = if (reversed) number.key.reversed() else number.key
			if (substring.startsWith(s)) {
				return number.value
			}
		}

		return -1
	}

	override suspend fun part1(): Any {
		return lines.sumOf {
			var first = ""
			var last = ""
			for (i in it.indices) {
				if (myIsDigit(it, i, false)) {
					first = getDigit(it, i, false).toString()
					break
				}
			}
			for (i in it.indices) {
				if (myIsDigit(it.reversed(), i, true)) {
					last = getDigit(it.reversed(), i, true).toString()
					break
				}
			}

			parseInt(first + last)
		}
	}

	override suspend fun part2(): Any {
		return lines.sumOf {
			var first = ""
			var last = ""
			for (i in it.indices) {
				if (myIsDigit(it, i, false)) {
					first = getDigit(it, i, false).toString()
					break
				}
			}
			for (i in it.indices) {
				if (myIsDigit(it.reversed(), i, true)) {
					last = getDigit(it.reversed(), i, true).toString()
					break
				}
			}

			parseInt(first + last)
		}
	}
}