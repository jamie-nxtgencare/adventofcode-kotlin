import java.io.File

fun main() {
    println(part1("day1.sample-input"))
    println(part1("day1.input"))
    println(part2("day1.sample-input"))
    println(part2("day1.input"))
}

fun part1(file : String): Int {
    val map = getMap(file)

    for (entry in map) {
        val a = entry.key
        val b = 2020 - a
        if (map.contains(b)) {
            return a * b
        }
    }

    return -1
}

fun part2(file : String): Int {
    val map = getMap(file)

    for (e1 in map) {
        for (e2 in map) {
            val a = e1.key
            val b = e2.key
            val c = 2020 - (a + b)
            if (map.contains(c)) {
                return a * b * c
            }
        }
    }

    return -1
}

fun getMap(file: String): Map<Int, Int> {
    return File({}.javaClass.getResource(file).file).readLines().map { l -> Integer.parseInt(l) to 1 }.toMap()
}