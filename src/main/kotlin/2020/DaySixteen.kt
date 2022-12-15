@file:Suppress("PackageName")

package `2020`

import Project

class DaySixteen(file: String): Project() {
    private val tickets: MutableList<List<Int>> = ArrayList()
    private lateinit var yourTicket: List<Int>
    private val rules: HashMap<String, Rule> = HashMap()
    private val lines = getLines(file)
    private val ruleRegex = "(.*): (\\d+)-(\\d+) or (\\d+)-(\\d+)".toRegex()

    init {
        var step = 0
        lines.forEach {
            if (it.isBlank()) {
                step++
            } else {
                when (step) {
                    0 -> {
                        addRule(it)
                    }
                    1 -> {
                        if (it != "your ticket:") {
                            createYourTicket(it)
                        }
                    }
                    2 -> {
                        if (it != "nearby tickets:") {
                            addNearbyTicket(it)
                        }
                    }
                }
            }
        }
    }

    private fun createYourTicket(it: String) {
        yourTicket = it.split(",").map { it.toInt() }
    }

    private fun addNearbyTicket(it: String) {
        tickets.add(it.split(",").map { it.toInt() })
    }

    private fun addRule(it: String) {
        val matchResults = ruleRegex.findAll(it)
        matchResults.forEach {
            rules[it.groupValues[1]] = Rule(
                it.groupValues[1], it.groupValues[2].toInt(), it.groupValues[3].toInt(), it.groupValues[4].toInt(), it.groupValues[5].toInt()
            )
        }
    }

    class Rule(val name: String, rangeAStart: Int, rangeAEnd: Int, rangeBStart: Int, rangeBEnd: Int) {
        private val rangeA = rangeAStart..rangeAEnd
        private val rangeB = rangeBStart..rangeBEnd

        fun isValid(it: Int): Boolean {
            return rangeA.contains(it) || rangeB.contains(it)
        }
    }

    override fun part1(): Any {
        return tickets.map { ticket -> ticket.filter { num -> rules.values.all { !it.isValid(num) } } }.flatten().sum()
    }

    override fun part2(): Any {
        val validTickets = tickets.filter { ticket -> ticket.all { num -> rules.values.any { it.isValid(num) } } }
        val fieldRules: MutableMap<Int, Map<String, Rule>> = HashMap()
        // For each field, find all rules that match every ticket
        for (i in validTickets[0].indices) {
            fieldRules[i] = rules.filter { rule -> validTickets.all { rule.value.isValid(it[i]) } }
        }

        var sortedFieldMap = fieldRules.toList().sortedBy { (_, value) -> value.size }.toMap().toMutableMap()
        val fieldOut: Array<String?> = arrayOfNulls(validTickets[0].size)

        while (sortedFieldMap.isNotEmpty()) {
            val key = sortedFieldMap.entries.first().key
            val fieldName = sortedFieldMap.entries.first().value.keys.first()
            fieldOut[key] = fieldName

            for (mapKey in sortedFieldMap.keys) {
                sortedFieldMap[mapKey] = sortedFieldMap[mapKey]?.filter { (_, rule) -> rule.name != fieldName } ?: HashMap()
            }

            sortedFieldMap.remove(key)
            sortedFieldMap = sortedFieldMap.toList().sortedBy { (_, value) -> value.size }.toMap().toMutableMap()
        }

        var product = 1L

        fieldOut.forEachIndexed { i, it ->
            if (it?.startsWith("departure") == true) {
                product *= yourTicket[i]
            }
        }

        return product
    }
}