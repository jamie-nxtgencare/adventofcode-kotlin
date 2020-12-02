import java.lang.Integer.parseInt

class DayTwo(file: String) {
    private val passwordAndRules = getPasswordAndRules(file)

   fun part1(): Int {
       return countValidPasswords { it?.isValid() }
    }

    fun part2(): Int {
        return countValidPasswords { it?.isValid2() }
    }

    private fun countValidPasswords(f: (r: PasswordAndRule?) -> Boolean?): Int {
        return passwordAndRules.filter { f(it) ?: false }.count()
    }

    private fun getPasswordAndRules(file: String): List<PasswordAndRule?> {
        val rule = "(.+)-(.+) (.): (.*)".toRegex()
        return getLines(file).map {
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