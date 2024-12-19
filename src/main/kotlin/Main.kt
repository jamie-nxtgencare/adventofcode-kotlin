import java.lang.Integer.parseInt
import java.time.Duration
import java.time.LocalDateTime

fun main(args: Array<String>) {
    val year = if (args.size > 1) parseInt(args[0]) else 2023
    val day = if (args.isEmpty()) 1 else parseInt(args[args.size - 1])
    val isSample = args.size > 2 && args[1].lowercase() == "sample"
    val dayStr = getDayStr(Day.byNumber(day))
    @Suppress("UNCHECKED_CAST") val project: Class<out Project> = Class.forName("${year}.Day$dayStr") as Class<Project>

    println("===Startup===")
    var start = LocalDateTime.now()
    val constructor = project.getDeclaredConstructor(String::class.java)
    try {
        constructor.newInstance("day${day}.empty")
    } catch (e: Exception) {
        // Ignore resource not found during startup
    }
    var next = LocalDateTime.now()
    println("Using reflection to load day class: %.${2}fms".format(Duration.between(start, next).toNanos()/1_000_000.0))

    println("===${if (isSample) "Sample" else "Actual"}===")
    start = LocalDateTime.now()
    val instance = constructor.newInstance("day${day}.${if (isSample) "sample-input" else "input"}")
    next = LocalDateTime.now()
    println("Construct/Parse Input: %.${2}fms".format(Duration.between(start, next).toNanos()/1_000_000.0))
    instance.run(next)
}

fun getDayStr(day: Day): String {
    val dayStr = day.toString()
    return Character.toTitleCase(dayStr[0]) + dayStr.substring(1).lowercase()
}

enum class Day(val arg : Int) {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    ELEVEN(11),
    TWELVE(12),
    THIRTEEN(13),
    FOURTEEN(14),
    FIFTEEN(15),
    SIXTEEN(16),
    SEVENTEEN(17),
    EIGHTEEN(18),
    NINETEEN(19),
    TWENTY(20),
    TWENTYONE(21),
    TWENTYTWO(22),
    TWENTYTHREE(23),
    TWENTYFOUR(24),
    TWENTYFIVE(25);

    companion object { fun byNumber(number: Int) : Day { return values().findLast { it.arg == number } ?: ONE } }
}