class DayTwentyone(file: String) : Project {
    val lines = getLines(file)
    var p1 = lines[0].last().toString().toInt() - 1
    var p2 = lines[1].last().toString().toInt() - 1

    override fun part1(): Any {
        var roll = 1
        var p1Score = 0
        var p2Score = 0

        while (p1Score < 1000 && p2Score < 1000) {
            if (roll % 2 == 1) {
                p1 = (p1 + (roll * 3) + 3) % 10
                p1Score += p1 + 1
            } else {
                p2 = (p2 + (roll * 3) + 3) % 10
                p2Score += p2 + 1
            }
            roll += 3
        }

        val loser = if (p1Score > p2Score) p2Score else p1Score
        return loser * (roll - 1)
    }

    override fun part2(): Any {
        return -1
    }

}