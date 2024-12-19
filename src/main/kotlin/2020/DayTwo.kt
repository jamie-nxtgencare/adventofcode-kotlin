@file:Suppress("PackageName")

package `2020`

import Project
import java.lang.Integer.parseInt

class DayTwo(file: String, isTest: Boolean = false) : Project(file, isTest) {
    private val passwordAndRules = getPasswordAndRules(file)

    override suspend fun part1(): Any {
       return countValidPasswords { it?.isValid() }
    }

    override suspend fun part2(): Any {
        return countValidPasswords { it?.isValid2() }
    }

    private fun countValidPasswords(f: (r: PasswordAndRule?) -> Boolean?): Int {
        return passwordAndRules.filter { f(it) ?: false }.count()
    }

    private fun getPasswordAndRules(file: String): List<PasswordAndRule?> {
        val rule = "(.+)-(.+) (.): (.*)".toRegex()
        return mapFileLines(file) {
            var passwordAndRule : PasswordAndRule? = null
            val matches = rule.findAll(it)
            matches.forEach { matchResult -> passwordAndRule = PasswordAndRule(
                parseInt(matchResult.groupValues[1]),
                parseInt(matchResult.groupValues[2]),
                matchResult.groupValues[3],
                matchResult.groupValues[4]
            ) }
            passwordAndRule
        }
    }

    class PasswordAndRule(private val arg1: Int, private val arg2: Int, private val letter: String, private val password: String) {
        fun isValid() : Boolean {
            return letter.toRegex().findAll(password).count() in arg1..arg2
        }

        fun isValid2() : Boolean {
            val aOk = password[arg1 - 1] == letter[0]
            val bOk = password[arg2 - 1] == letter[0]
            return (aOk || bOk) && aOk != bOk
        }
    }
}