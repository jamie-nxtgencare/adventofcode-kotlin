import kotlin.math.floor

class DayTwentyfour(file: String) : Project {
    val ins = mapFileLines(file) { AluIns(it) }

    override fun part1(): Any {
        return solve(true)
    }

    override fun part2(): Any {
        return solve(false)
    }

    private fun solve(largest: Boolean): Any {
        val alu = Alu(ins)

        var prevOutputs: Set<Int>
        var outputs: HashSet<Int>
        val validLastIndexes = HashMap<Int, Int>()
        var solved = 13
        var lastSolvedValue = 0
        val range = if (largest) 9 downTo 1 else 1..9

        while (solved >= 0) {
            prevOutputs = setOf(0)
            loop@ for (moduleIndex in 0..solved) {
                println("Module $moduleIndex, ${prevOutputs.size}")
                outputs = HashSet()

                for (input1 in range) {
                    println("${floor((input1 - 1) / 8.0 * 100).toInt()}% (outputs ${outputs.size})")
                    val input1Str = input1.toString()
                    val module = alu.getModule(moduleIndex)
                    for (input2 in prevOutputs) {
                        module.reset()
                        module.input = input1Str
                        module.registry[3] = input2
                        module.execute()

                        outputs.add(module.registry[3])

                        if (moduleIndex == solved && module.registry[3] == lastSolvedValue) {
                            lastSolvedValue = input2
                            validLastIndexes[solved] = input1
                            println("Solved ${solved--}, $input1, searching for $input2")
                            break@loop
                        }
                    }
                }
                prevOutputs = outputs
            }
        }

        var output = ""

        for (i in 0..13) {
            output += validLastIndexes[i]!!.toString()
        }

        return output.toInt()

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
    var registry = arrayOf(0,0,0,0)
    val inputIns = ArrayList<Int>()

    init {
        instructions.forEachIndexed { i, it ->
            if (it.isInput()) {
                inputIns.add(i)
            }
        }
    }

    fun getModule(i: Int): Alu {
        var inputCount = 0
        var index = 0
        for (j in instructions) {
            if (j.it.startsWith("inp")) {
                if (inputCount == i) {
                    break
                }
                inputCount++
            }
            index++
        }

        val subinstructions = instructions.subList(index, instructions.size)

        val out = ArrayList<AluIns>()
        out.add(subinstructions.first())

        val rest = subinstructions.subList(1,subinstructions.size)
        out.addAll(rest.takeWhile { !it.it.startsWith("inp") })

        return Alu(out)
    }

    fun execute() {
        val s = input!!
        var i = s.length
        while (i > 0) {
            val substr = s.substring(0, i)
            i--
        }

        var j = inputIns[i]
        var insCount = i

        while (j < instructions.size) {
            val it = instructions[j]
            it.func.invoke()
            j++
        }
    }

    fun reset() {
        count = 0
        registry = arrayOf(0,0,0,0)
    }

    init {
        instructions.forEach {
            it.alu = this
        }
    }
}
