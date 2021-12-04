class DayFour(file: String) : Project {
    private val game = Game(getLines(file))

    override fun part1(): Any {
        return game.getScore()
    }

    override fun part2(): Any {
        return -1
    }

}

class Game(lines: List<String>) {
    private val marks: List<Int> = lines[0].split(",").map { it.toInt() }
    private val cards: List<Card> = getCards(lines.subList(2, lines.size))

    private fun getCards(cardsString: List<String>): List<Card> {
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
                val wins = card.mark(mark)

                if (wins) {
                    return card.getScore(mark)
                }
            }
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

        // Columns
        for (i in 0 until 5) {
            val row = ArrayList<Int>()
            card.forEach {
                row.add(it[i])
            }
            lines.add(row)
        }

        // Diagonals
        val diagonal = ArrayList<Int>()
        val diagonal2 = ArrayList<Int>()

        for (i in 0 until 5) {
            diagonal.add(lines[i][i])
            diagonal2.add(lines[i][4-i])
        }
        /*
         * Read the instructions dummy, diagonals don't count
         * lines.add(diagonal)
         *  lines.add(diagonal2)
         **/

        return lines
    }

    fun getScore(justCalled: Int): Int {
        return getUnmarkedSum() * justCalled
    }

    private fun getUnmarkedSum(): Int {
        return numbers.filter { !it.value }.keys.sum()
    }
}
