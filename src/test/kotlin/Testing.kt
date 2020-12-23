import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigInteger

class Testing {
    @Test
    fun day1Sample() {
        testSample(1, 514579, 241861950)
    }

    @Test
    fun day1() {
        test(1, 445536, 138688160)
    }

    @Test
    fun day2Sample() {
        testSample(2, 2, 1)
    }

    @Test
    fun day2() {
        test(2, 460, 251)
    }

    @Test
    fun day3Sample() {
        testSample(3, 7, 336)
    }

    @Test
    fun day3() {
        test(3, 220, 2138320800)
    }

    @Test
    fun day4Sample() {
        testSample(4, 2, 2)
    }

    @Test
    fun day4() {
        test(4, 245, 133)
    }

    @Test
    fun day5Sample() {
        testSample(5, 820, -1)
    }

    @Test
    fun day5() {
        test(5, 878, 504)
    }

    @Test
    fun day6Sample() {
        testSample(6, 11, 6)
    }

    @Test
    fun day6() {
        test(6, 6587, 3235)
    }

    @Test
    fun day7Sample() {
        testSample(7, 4, 32)
    }

    @Test
    fun day7() {
        test(7, 185, 89084)
    }

    @Test
    fun day8Sample() {
        testSample(8, 5, 8)
    }

    @Test
    fun day8() {
        test(8, 1818, 631)
    }

    @Test
    fun day9Sample() {
        testSample(9, BigInteger.valueOf(127), BigInteger.valueOf(62))
    }

    @Test
    fun day9() {
        test(9, BigInteger.valueOf(15353384), BigInteger.valueOf(2466556))
    }

    @Test
    fun day10Sample() {
        testSample(10, 220, BigInteger.valueOf(19208))
    }

    @Test
    fun day10() {
        test(10, 2484, BigInteger.valueOf(15790581481472))
    }

    @Test
    fun day11Sample() {
        testSample(11, 37, 26)
    }

    @Test
    fun day11() {
        test(11, 2164, 1974)
    }

    @Test
    fun day12Sample() {
        testSample(12, 25, 286)
    }

    @Test
    fun day12() {
        test(12, 923, 24769)
    }

    @Test
    fun day13Sample() {
        testSample(13, 295L, 1068781L)
    }

    @Test
    fun day13() {
        test(13, 2305L, 552612234243498L)
    }

    @Test
    fun day14Sample() {
        testSample(14, 51L, 208L)
    }

    @Test
    fun day14() {
        test(14, 10717676595607L, 3974538275659L)
    }

    @Test
    fun day15Sample() {
        testSample(15, 436, 175594)
    }

    @Test
    fun day15() {
        test(15, 614, 1065)
    }

    @Test
    fun day16Sample() {
        testSample(16, 71, 1L)
    }

    @Test
    fun day16() {
        test(16, 29019, 517827547723L)
    }

    @Test
    fun day17Sample() {
        testSample(17, 112, 848)
    }

    @Test
    fun day17() {
        test(17, 368, 2696)
    }

    @Test
    fun day18Sample() {
        testSample(18, 13632L, 23340L)
    }

    @Test
    fun day18() {
        test(18, 75592527415659L, 360029542265462L)
    }

    @Test
    fun day19Sample() {
        testSample(19, 3, 12)
    }

    @Test
    fun day19() {
        test(19, 122, 287)
    }

    @Test
    fun day20Sample() {
        testSample(20, 20899048083289, 273)
    }

    @Test
    fun day20() {
        test(20, 29293767579581, 1989)
    }

    @Test
    fun day21Sample() {
        testSample(21, 5, "mxmxvkd,sqjhc,fvjkl")
    }

    @Test
    fun day21() {
        test(21, 1685, "ntft,nhx,kfxr,xmhsbd,rrjb,xzhxj,chbtp,cqvc")
    }

    @Test
    fun day22Sample() {
        testSample(22, 306, 291)
    }

    @Test
    fun day22() {
        test(22, 32677, 33661)
    }

    @Test
    fun day23Sample() {
        testSample(23, 67384529, 149245887792)
    }

    @Test
    fun day23() {
        test(23, 32897654, 186715244496)
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