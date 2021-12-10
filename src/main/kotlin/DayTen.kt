import java.util.*

class DayTen(file: String) : Project {
    private val lines = getLines(file)
    private val pairs = mapOf("(" to ")", "[" to "]", "<" to ">", "{" to "}")
    private val points = mapOf(")" to 3, "]" to 57, "}" to 1197, ">" to 25137)

    override fun part1(): Any {
        val illegalChars = ArrayList<String>()

        lines.forEach {
            val stack = Stack<String>()
            val chars = it.split("").filter { char -> char.isNotBlank() }

            for (char in chars) {
                if (stack.isNotEmpty() && pairs[stack.peek()] == char) {
                    stack.pop()
                    continue
                }
                if (pairs.containsKey(char)) {
                    stack.push(char)
                    continue
                }

                illegalChars.add(char)
                break
            }
        }


        return illegalChars.map { points[it] }.sumOf { it ?: 0 }
    }

    override fun part2(): Any {
        return -1
    }

}