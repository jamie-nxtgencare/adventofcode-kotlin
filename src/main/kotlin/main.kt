import java.io.File
import java.lang.Integer.parseInt

fun main(args: Array<String>) {

    val number = parseInt(args[0])
    val constructor = Day.byNumber(number).clazz.getDeclaredConstructor(String::class.java)
    constructor.newInstance("day%d.sample-input".format(number)).run()
    constructor.newInstance("day%d.input".format(number)).run()

}

enum class Day(val arg : Int, val clazz : Class<out Project>) {
    ONE(1, DayOne::class.java),
    TWO(2, DayTwo::class.java),
    THREE(3, DayThree::class.java),
    FOUR(4, DayFour::class.java),
    FIVE(5, DayFive::class.java),
    SIX(6, DaySix::class.java);

    companion object { fun byNumber(number: Int) : Day { return values().findLast { it.arg == number } ?: ONE } }
}

fun getLines(file: String) : List<String> {
    return File({}.javaClass.getResource(file).file).readLines()
}