import kotlin.math.floor

class DayTwentyfour(file: String) : Project {
    val ins = mapFileLines(file) { AluIns(it) }

    override fun part1(): Any {
        val alu = Alu(ins)

        for (i in 99_999_999_999_999 downTo 11_111_111_111_111) {
            val s = i.toString()

            if (i % 1_000_000 == 0L) {
                println(i / 99_999_999_999_999.0 * 100)
                alu.purgeCache()
            }

            if (!s.contains("0")) {
                alu.input = s
                alu.execute()

                if (alu.registry.last() == 0) {
                    return s.toLong()
                }

                alu.reset()
            }
        }

        return -1
    }

    override fun part2(): Any {
        return -1
    }

}

class AluIns(val it: String) {
    lateinit var alu: Alu
    private val split = it.split(" ").filter { it.isNotBlank() }
    private val ass = split[1]
    private val bss = if (split.size > 2) split[2] else "-1"
    private val a = getIndex(ass)
    private val b = getIndex(bss)

    private fun getIndex(s: String): Int {
        return when(s) {
            "w" -> 0
            "x" -> 1
            "y" -> 2
            "z" -> 3
            else -> -1
        }
    }

    val func: () -> Unit = when (split[0]) {
        "inp" -> {{ alu.registry[a] = readInput() }}
        "add" -> {{ alu.registry[a] = alu.registry[a] + gb(alu.registry) }}
        "mul" -> {{ alu.registry[a] = alu.registry[a] * gb(alu.registry) }}
        "div" -> {{ alu.registry[a] = floor(alu.registry[a].toDouble() / gb(alu.registry)).toInt() }}
        "mod" -> {{ alu.registry[a] = alu.registry[a] % gb(alu.registry) }}
        "eql" -> {{ alu.registry[a] = if (alu.registry[a] == gb(alu.registry)) 1 else 0 }}
        else -> {{ run {} }}
    }

    private fun gb(v: Array<Int>): Int {
        return if (b == -1) split[2].toInt() else v[b]
    }

    private fun readInput(): Int {
        return alu.input!![alu.count++].toString().toInt()
    }

    fun isInput(): Boolean {
        return it.startsWith("inp")
    }
}

class Alu(private val instructions: List<AluIns>, var count: Int = 0) {
    var input:String? = null
    private val l2Cache = HashMap<Int, HashMap<String, Array<Int>>>()
    var registry = arrayOf(0,0,0,0)
    val inputIns = ArrayList<Int>()

    init {
        instructions.forEachIndexed { i, it ->
            if (it.isInput()) {
                inputIns.add(i)
            }
        }

        for (i in 0..13) {
            l2Cache[i] = HashMap()
        }
    }

    fun execute() {
        val s = input!!
        var i = s.length
        while (i > 0) {
            val substr = s.substring(0, i)

            if (l2Cache.containsKey(substr.length) && l2Cache[substr.length]!!.containsKey(substr)) {
                registry = l2Cache[substr.length]!![substr]!!.copyOf()
                break
            }

            i--
        }

        var j = inputIns[i]
        var insCount = i

        while (j < instructions.size) {
            val it = instructions[j]
            if(it.isInput()) {
                val substr = input!!.substring(0, insCount++)
                if (substr.isNotEmpty() && substr.length < 14) {
                    l2Cache[substr.length]!![substr] = registry.copyOf()
                }
            }
            it.func.invoke()
            j++
        }
    }

    fun reset() {
        count = 0
        registry = arrayOf(0,0,0,0)
    }

    fun purgeCache() {
        l2Cache[13]?.clear()
        l2Cache[13]?.clear()
        l2Cache[14]?.clear()
    }

    init {
        instructions.forEach {
            it.alu = this
        }
    }
}
