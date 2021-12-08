class DayFour(file: String) : Project {
    private val game = Game(getLines(file))

    override fun part1(): Any {
        return game.getScore()
    }

    override fun part2(): Any {
        return game.playToLoseScore()
    }

}

class Game(lines: List<String>) {
    private val marks: List<Int> = lines[0].split(",").map { it.toInt() }
    private val cards: ArrayList<Card> = getCards(lines.subList(2, lines.size))

    private fun getCards(cardsString: List<String>): ArrayList<Card> {
        var lineNo = 0
        val cards = ArrayList<Card>()

        while (lineNo < cardsString.size) {
            cards.add(Card(cardsString.subList(lineNo, lineNo+5)))
            lineNo += 6
        }

        return cards
    }

    fun getScore(): Int {
        for (mark in marks) {
            for (card in cards) {
                if (card.mark(mark)) {
                    return card.getScore(mark)
                }
            }
        }

        return -1
    }

    fun playToLoseScore(): Any {
        cards.forEach { it.reset() }
        val cardsToRemove = HashSet<Card>()
        for (mark in marks) {
            for (card in cards) {
                val wins = card.mark(mark)

                if (wins) {
                    cardsToRemove.add(card)
                }
            }
            cards.removeAll(cardsToRemove)
            if (cards.size == 0) {
                return cardsToRemove.first().getScore(mark)
            }
            cardsToRemove.clear()
        }
        return -1
    }

}

class Card(cardLines: List<String>) {
    private val numbers = HashMap<Int, Boolean>()
    private val card = ArrayList<List<Int>>()
    private var lines = ArrayList<List<Int>>()

    init {
        cardLines.forEach {
            val rowNumbers = ArrayList(it.split("\\s+".toRegex()).filter { s -> s.isNotBlank() }.map { it2 ->
                it2.toInt()
            })
            numbers.putAll(rowNumbers.map { it2 -> Pair(it2, false) })
            card.add(rowNumbers)
        }
        lines = findLines()
    }

    fun mark(mark: Int): Boolean {
        if (numbers.containsKey(mark)) {
            numbers[mark] = true
        }

        return isWinningCard(mark)
    }

    fun getScore(justCalled: Int): Int {
        return getUnmarkedSum() * justCalled
    }

    fun reset() { numbers.forEach { numbers[it.key] = false }}

    private fun isWinningCard(mark: Int): Boolean {
        val lines:List<List<Int>> = findLinesContainingMark(mark)

        if (lines.any { line -> line.all { numbers[it] == true }}) {
            return true
        }
        return false
    }

    private fun findLinesContainingMark(mark: Int): List<List<Int>> {
        return lines.filter { it.contains(mark) }
    }

    private fun findLines(): ArrayList<List<Int>> {
        val lines = ArrayList<List<Int>>(card)

        for (i in 0 until 5) {
            val row = ArrayList<Int>()
            card.forEach {
                row.add(it[i])
            }
            lines.add(row)
        }

        return lines
    }

    private fun getUnmarkedSum(): Int { return numbers.filter { !it.value }.keys.sum() }
}
