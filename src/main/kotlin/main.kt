import java.lang.Integer.parseInt

fun main(args: Array<String>) {

    val number = parseInt(args[0])
    val day = Day.byNumber(number)
    val dayStr = getDayStr(day)
    @Suppress("UNCHECKED_CAST") val project: Class<out Project> = Class.forName("Day$dayStr") as Class<Project>
    val constructor = project.getDeclaredConstructor(String::class.java)

    constructor.newInstance("day%d.sample-input".format(number)).run()
    constructor.newInstance("day%d.input".format(number)).run()

}

fun getDayStr(day: Day): String {
    val dayStr = day.toString()
    return Character.toTitleCase(dayStr[0]) + dayStr.substring(1).toLowerCase()
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