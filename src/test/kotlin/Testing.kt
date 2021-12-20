import DaySeventeen.Companion.shoot
import org.junit.Assert.assertEquals
import org.junit.Test

class Testing {
    @Test
    fun day1Sample() {
        testSample(1, 7, 5)
    }

    @Test
    fun day1() {
        test(1, 1602, 1633)
    }

    @Test
    fun day2Sample() {
        testSample(2, 150, 900)
    }

    @Test
    fun day2() {
        test(2, 1660158, 1604592846)
    }

    @Test
    fun day3Sample() {
        testSample(3, 198, 230)
    }

    @Test
    fun day3() {
        test(3, 4006064, 5941884)
    }

    @Test
    fun day4Sample() {
        testSample(4, 4512, 1924)
    }

    @Test
    fun day4() {
        test(4, 38594, 21184)
    }

    @Test
    fun day5Sample() {
        testSample(5, 5, 12)
    }

    @Test
    fun day5() {
        test(5, 6189, 19164)
    }

    @Test
    fun day6Sample() {
        testSample(6, 5934L, 26984457539L)
    }

    @Test
    fun day6() {
        test(6, 372984L, 1681503251694)
    }

    @Test
    fun day7Sample() {
        testSample(7, 37, 168)
    }

    @Test
    fun day7() {
        test(7, 356179, 99788435)
    }

    @Test
    fun day8Sample() {
        testSample(8, 26, 61229)
    }

    @Test
    fun day8() {
        test(8, 344, 1048410)
    }

    @Test
    fun day9Sample() {
        testSample(9, 15, 1134)
    }

    @Test
    fun day9() {
        test(9, 564, 1038240)
    }

    @Test
    fun day10Sample() {
        testSample(10, 26397, 288957L)
    }

    @Test
    fun day10() {
        test(10, 392097, 4263222782L)
    }

    @Test
    fun day11Sample() {
        testSample(11, 1656, 195)
    }

    @Test
    fun day11() {
        test(11, 1719, 232)
    }

    @Test
    fun day12Sample() {
        testSample(12, 18, 103)
    }

    @Test
    fun day12() {
        test(12, 3485, 85062)
    }

    @Test
    fun day13Sample() {
        testSample(13, 17, 16)
    }

    @Test
    fun day13() {
        test(13, 755, 101)
    }

    @Test
    fun day14Sample() {
        testSample(14, 1588L, 2188189693529L)
    }

    @Test
    fun day14() {
        test(14, 2435L, 2587447599164L)
    }

    @Test
    fun day15Sample() {
        testSample(15, 40, 315)
    }

    @Test
    fun day15() {
        test(15, 698, 3022)
    }

    @Test
    fun day16Sample() {
        testSample(16, 31, 54L)
    }

    @Test
    fun day16() {
        test(16, 886, 184487454837L)
    }

    @Test
    fun day16Tests() {
        assertEquals(getPacket("38006F45291200").getVersionSum(), 9)
        assertEquals(getPacket("EE00D40C823060").getVersionSum(), 14)
        assertEquals(getPacket("8A004A801A8002F478").getVersionSum(), 16)
        assertEquals(getPacket("620080001611562C8802118E34").getVersionSum(), 12)
        assertEquals(getPacket("C0015000016115A2E0802F182340").getVersionSum(), 23)
        assertEquals(getPacket("A0016C880162017C3686B18A3D4780").getVersionSum(), 31)

        assertEquals(getPacket("C200B40A82").getResult(), 3)
        assertEquals(getPacket("04005AC33890").getResult(), 54)
        assertEquals(getPacket("880086C3E88112").getResult(), 7)
        assertEquals(getPacket("CE00C43D881120").getResult(), 9)
        assertEquals(getPacket("D8005AC2A8F0").getResult(), 1)
        assertEquals(getPacket("F600BC2D8F").getResult(), 0)
        assertEquals(getPacket("9C005AC2F8F0").getResult(), 0)
        assertEquals(getPacket("9C0141080250320F1802104A08").getResult(), 1)
    }

    fun getPacket(input: String): Packet {
        return Packet(input.toCharArray().asList().map { c -> String.format("%04d", Integer.parseInt(Integer.toBinaryString(Integer.parseInt(c.toString(), 16))))}.joinToString(""))
    }

    @Test
    fun day17Tests() {
        val ranges = DaySeventeen.parseLine("target area: x=20..30, y=-10..-5")
        assertEquals(shoot(Pair(7L, 2L), ranges), 3L)
        assertEquals(shoot(Pair(6L, 3L), ranges), 6L)
        assertEquals(shoot(Pair(9L, 0L), ranges), 0L)
        assertEquals(shoot(Pair(17L, -4L), ranges), Long.MIN_VALUE)
    }

    @Test
    fun day17Sample() {
        testSample(17, 45L, 112)
    }

    @Test
    fun day17() {
        test(17, 11175L, 3540)
    }

    @Test
    fun day18Tests() {
        val parseTests = listOf(
            Pair("[1,2]", listOf(1,2)),
            Pair("[[1,2],3]", listOf(1,2,3)),
            Pair("[9,[8,7]]", listOf(9,8,7)),
            Pair("[[1,9],[8,5]]", listOf(1,9,8,5)),
            Pair("[[[[1,2],[3,4]],[[5,6],[7,8]]],9]", listOf(1,2,3,4,5,6,7,8,9)),
            Pair("[[[9,[3,8]],[[0,9],6]],[[[3,7],[4,9]],3]]", listOf(9,3,8,0,9,6,3,7,4,9,3)),
            Pair("[[[[1,3],[5,3]],[[1,3],[8,7]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]", listOf(1,3,5,3,1,3,8,7,4,9,6,9,8,2,7,3))
        )

        for (test in parseTests) {
            val parsed = DayEighteen.parse(test.first)
            assertEquals(test.first, parsed.toString())
            assertEquals(test.second.joinToString(), parsed.traverse().joinToString())
        }

        val eq1 = DayEighteen.parse("[1,2]")
        val eq2 = DayEighteen.parse("[[3,4],5]")

        assertEquals(eq1.plus(eq2).toString(), "[[1,2],[[3,4],5]]")

        val explodeTests = listOf(
            Pair("[[[[[9,8],1],2],3],4]", "[[[[0,9],2],3],4]"),
            Pair("[7,[6,[5,[4,[3,2]]]]]", "[7,[6,[5,[7,0]]]]"),
            Pair("[[6,[5,[4,[3,2]]]],1]", "[[6,[5,[7,0]]],3]"),
            Pair("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]", "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]"),
            Pair("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]", "[[3,[2,[8,0]]],[9,[5,[7,0]]]]")
        )

        for (test in explodeTests) {
            val parsed = DayEighteen.parse(test.first)
            val exploded = parsed.explode()
            assertEquals(exploded, true)
            assertEquals(parsed.toString(), test.second)
        }

        val splitTests = listOf(
            Pair("[10,0]", "[[5,5],0]"),
            Pair("[11,0]", "[[5,6],0]"),
            Pair("[12,0]", "[[6,6],0]"),
        )

        for (test in splitTests) {
            val parsed = DayEighteen.parse(test.first)
            val split = parsed.split()
            assertEquals(split, true)
            assertEquals(parsed.toString(), test.second)
        }

        val req1 = DayEighteen.parse("[[[[4,3],4],4],[7,[[8,4],9]]]")
        val req2 = DayEighteen.parse("[1,1]")

        val toReduce = req1.plus(req2)
        toReduce.reduce()

        assertEquals(toReduce.toString(), "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]")
        assertEquals(DayEighteen.parse("[9,1]").getMagnitude(), 29L)
        assertEquals(DayEighteen.parse("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]").getMagnitude(), 3488L)

        val reducedSumTests = listOf(
            Pair(
                listOf(
                    DayEighteen.parse("[1,1]"),
                    DayEighteen.parse("[2,2]"),
                    DayEighteen.parse("[3,3]"),
                    DayEighteen.parse("[4,4]")
                ),
                "[[[[1,1],[2,2]],[3,3]],[4,4]]"
            ),
            Pair(
                listOf(
                    DayEighteen.parse("[1,1]"),
                    DayEighteen.parse("[2,2]"),
                    DayEighteen.parse("[3,3]"),
                    DayEighteen.parse("[4,4]"),
                    DayEighteen.parse("[5,5]")
                ),
                "[[[[3,0],[5,3]],[4,4]],[5,5]]"
            ),
            Pair(
                listOf(
                    DayEighteen.parse("[1,1]"),
                    DayEighteen.parse("[2,2]"),
                    DayEighteen.parse("[3,3]"),
                    DayEighteen.parse("[4,4]"),
                    DayEighteen.parse("[5,5]"),
                    DayEighteen.parse("[6,6]")
                ),
                "[[[[5,0],[7,4]],[5,5]],[6,6]]"
            )
        )

        reducedSumTests.forEach { assertEquals(DayEighteen.reducedSum(it.first).toString(), it.second) }
    }

    @Test
    fun day18BrokenTest() {
        val aaa = DayEighteen.parse("[[[[7,7],[7,7]],[[8,7],[8,7]]],[[[7,0],[7,7]],9]]")
        val bbb = aaa.plus(DayEighteen.parse("[[[[4,2],2],6],[8,7]]"))

        bbb.reduce()

        assertEquals(bbb.toString(), "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]")
    }

    @Test
    fun day18Sample() {
        testSample(18, 4140L, 3993L)
    }

    @Test
    fun day18() {
        test(18, 4017L, 4583L)
    }

    @Test
    fun day19Tests() {
        assertEquals(Point3D(1,2,3).getPermutations().size, 24)
    }

    @Test
    fun day19Sample() {
        testSample(19, 79, -1)
    }

    @Test
    fun day19() {
        test(19, -1, -1)
    }

    @Test
    fun day20Sample() {
        testSample(20, -1, -1)
    }

    @Test
    fun day20() {
        test(20, -1, -1)
    }

    @Test
    fun day21Sample() {
        testSample(21, -1, -1)
    }

    @Test
    fun day21() {
        test(21, -1, -1)
    }

    @Test
    fun day22Sample() {
        testSample(22, -1, -1)
    }

    @Test
    fun day22() {
        test(22, -1, -1)
    }

    @Test
    fun day23Sample() {
        testSample(23, -1, -1)
    }

    @Test
    fun day23() {
        test(23, -1, -1)
    }

    @Test
    fun day24Sample() {
        testSample(24, -1, -1)
    }

    @Test
    fun day24() {
        test(24, -1, -1)
    }

    @Test
    fun day25Sample() {
        testSample(25, -1, -1)
    }

    @Test
    fun day25() {
        test(25, -1, -1)
    }

    private fun testSample(number: Int, part1: Any, part2: Any) {
        baseTest("day%d.sample-input".format(number), number, part1, part2)
    }

    private fun test(number: Int, part1: Any, part2: Any) {
        baseTest("day%d.input".format(number), number, part1, part2)
    }

    private fun baseTest(file: String, number: Int, part1: Any, part2: Any) {
        val day = Day.byNumber(number)
        val dayStr = getDayStr(day)
        @Suppress("UNCHECKED_CAST") val projectClass: Class<out Project> = Class.forName("Day$dayStr") as Class<Project>
        val constructor = projectClass.getDeclaredConstructor(String::class.java)

        val project = constructor.newInstance(file.format(number))

        assertEquals(project?.part1(), part1)
        assertEquals(project?.part2(), part2)
    }
}