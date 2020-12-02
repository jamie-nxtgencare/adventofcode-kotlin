import java.io.File

fun main(args: Array<String>) {
    if (args[0] == "1") {
        val sample = DayOne("day1.sample-input")
        println(sample.part1())
        println(sample.part2())

        val input = DayOne("day1.input")
        println(input.part1())
        println(input.part2())
    } else if (args[0] == "2") {
        val sample = DayTwo("day2.sample-input")
        println(sample.part1())
        println(sample.part2())

        val input = DayTwo("day2.input")
        println(input.part1())
        println(input.part2())
    }
}

fun getLines(file: String) : List<String> {
    return File({}.javaClass.getResource(file).file).readLines()
}