@file:Suppress("PackageName")

package `2020`

import Project

class DayNineteen(file: String): Project() {
    private val rules = HashMap<Int, Rule>()
    private val messages = ArrayList<String>()

    init {
        var readingRules = true
        getLines(file).forEach {
            if (it.isBlank()) {
                readingRules = false
            } else {
                if (readingRules) {
                    val rule = Rule(it)
                    rules[rule.index] = rule
                } else {
                    messages.add(it)
                }
            }
        }
    }

    override suspend fun part1(): Any {
        val startingRule = rules[0]
        val regex = ("^" + startingRule?.toRegex(rules) + "$").toRegex()
        return messages.filter { it.matches(regex) }.size
    }

    override suspend fun part2(): Any {
        rules[8] = Rule("8: 42 | 42 8", "(" + rules[42]?.toRegex(rules) + ")+")
        rules[11] = Rule("11: 42 31 | 42 11 31", getGrossRepeatingBackreferenceRegex(rules[42]?.toRegex(rules), rules[31]?.toRegex(rules)))

        val startingRule = rules[0]
        val regex = ("^" + startingRule?.toRegex(rules) + "$").toRegex()
        return messages.filter { it.matches(regex) }.size
    }

    private fun getGrossRepeatingBackreferenceRegex(regex1: String?, regex2: String?): String { // 277 = too low, //; 297 == too high
        val out = ArrayList<String>()
        for (i in 1..10) {
            out.add("(($regex1){$i}($regex2){$i})")
        }

        return "(" + out.joinToString("|") + ")"
    }


    class Rule(rule: String, val regex:String = "") {

        var index = 0
        private val ruleType = RuleType.fromRuleString(rule)
        private val subRules = ArrayList<Int>()
        private val condition1 =  ArrayList<Int>()
        private val condition2 = ArrayList<Int>()
        var letter: String? = null

        init {
            val ruleTokens = rule.split(": ")
            index = ruleTokens[0].toInt()

            when (ruleType) {
                RuleType.RULE_LIST -> subRules.addAll(ruleTokens[1].split(" ").map { it.toInt() })
                RuleType.CONDITIONAL_RULE -> {
                    val conditions: List<List<Int>> = ruleTokens[1].split(" | ").map { it.split(" ").map { it2 -> it2.toInt()}}
                    condition1.addAll(conditions[0])
                    condition2.addAll(conditions[1])
                }
                RuleType.LETTER_MATCH -> letter = ruleTokens[1].split("\"")[1]
            }
        }

        fun toRegex(rules: Map<Int, Rule>): String? {
            if (regex.isNotBlank()) {
                return regex
            }

            return when(ruleType) {
                RuleType.LETTER_MATCH -> letter
                RuleType.RULE_LIST -> subRules.joinToString("") { rules[it]?.toRegex(rules) ?: "" }
                RuleType.CONDITIONAL_RULE -> "(" + listOf(condition1.joinToString("") { rules[it]?.toRegex(rules) ?: "" }, condition2.joinToString("") { rules[it]?.toRegex(rules) ?: "" }).joinToString("|") + ")"
            }
        }
    }

    enum class RuleType {
        RULE_LIST,
        CONDITIONAL_RULE,
        LETTER_MATCH;

        companion object {
            fun fromRuleString(ruleString: String): RuleType {
                return if (ruleString.contains('\"')) LETTER_MATCH else if (ruleString.contains('|')) CONDITIONAL_RULE else RULE_LIST
            }
        }
    }
}