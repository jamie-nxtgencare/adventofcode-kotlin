class DayEight(file: String) : Project {
    private val inputOutput: List<List<String>> = mapFileLines(file) { it.split(" | ") }
    private val input = inputOutput.map { it.first().split(" ") }
    private val output = inputOutput.map { it.last().split(" ") }

    private val digitLists = input.map { it.map { i -> Digit(i) }}
    private val outputList = output.map { it.map { i -> Digit(i) }}

    override fun part1(): Any {
        val oneFourSevenAndEight = output.flatten().filter { it.length == 2 || it.length == 4 || it.length == 3 || it.length == 7 }
        return oneFourSevenAndEight.size
    }

    override fun part2(): Any {
        var sum = 0
        for (i in digitLists.indices) {
            val digitList = digitLists[i];
            val outputs = outputList[i];

            val solvedInputs = solve(digitList)
            val solvedOutput = outputs.map { output -> solvedInputs.first { output.letters == it.letters }}

            sum += solvedOutput.map { it.output }.joinToString("").toInt()
        }

        return sum
    }

    private fun solve(digitList: List<Digit>): ArrayList<Digit> {
        val one = digitList.first { it.letters.size == 2 }.harden(1)
        val four = digitList.first { it.letters.size == 4 }.harden(4)
        val seven = digitList.first { it.letters.size == 3 }.harden(7)
        val eight = digitList.first { it.letters.size == 7 }.harden(8)

        val found = ArrayList<Digit>()
        listOf(one, four, seven, eight).toCollection(found)

        val remaining = ArrayList(digitList.filter { !found.contains(it) })
        val six = remaining.first { it.letters.size == 6 && !it.contains(one) }.harden(6)
        remaining.remove(six)
        found.add(six)

        val zero = remaining.first { it.letters.size == 6 && it.subtract(one).subtract(four).letters.size == 3 }.harden(0)
        remaining.remove(zero)
        found.add(zero)

        val nine = remaining.first { it.letters.size == 6 && it.subtract(one).letters.size == 4 }.harden(9)
        remaining.remove(nine)
        found.add(nine)

        val three = remaining.first { it.letters.size == 5 && it.contains(one) }.harden(3)
        remaining.remove(three)
        found.add(three)

        val two = remaining.first { it.letters.size == 5 && it.subtract(nine).letters.size == 1 }.harden(2)
        remaining.remove(two)
        found.add(two)

        val five = remaining.first().harden(5)
        found.add(five)
        return found
    }

}

class Digit(i: String) {
    val letters = i.split("").filter { it.isNotEmpty() }.sorted()
    var output: Int = -1

    fun contains(digit: Digit): Boolean {
        return letters.containsAll(digit.letters)
    }

    fun subtract(digit: Digit): Digit {
        return Digit(letters.filter { !digit.letters.contains(it) }.joinToString(""))
    }

    fun harden(output: Int) : Digit {
        this.output = output
        return this
    }
}
