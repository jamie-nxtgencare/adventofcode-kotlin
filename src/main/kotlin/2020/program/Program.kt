package program

class Program(private val instructionStrs: List<String>, private val debug: Boolean = false) {
    var context = ProgramContext()
    var instructions : List<Instruction> = instructionStrs.map { Instruction(it) }
    private var history = HashMap<Int, ProgramContext>()

    fun step(): Boolean {
        history[context.i] = context.copy()
        context.count++
        val instruction = instructions[context.i]

        when(instruction.opCode) {
            OpCode.ACC -> context.acc += instruction.parameters[0].toInt()
            OpCode.JMP -> context.i += instruction.parameters[0].toInt()
            else -> {}
        }

        if (instruction.opCode != OpCode.JMP) {
            context.i++
        }

        if (history.contains(context.i)) {
            context.state = ProgramContext.State.HALTED
            return false
        } else if (context.i == instructions.size) {
            context.state = ProgramContext.State.COMPLETE
            return false
        }

        return true
    }

    fun reset() {
        history = HashMap()
        instructions = instructionStrs.map { Instruction(it) }
        context = ProgramContext()
    }

    fun run() {
        context.state = ProgramContext.State.RUNNING
        if (debug) {
            println("============= Starting ===========")
            printContext()
        }
        while(step()) {
            if (debug) {
                printContext()
            }
        }
        if (debug) {
            printContext()
        }
    }

    private fun printContext() {
        val c = context.count
        val i = context.i
        val state = context.state
        val ins = if (i < instructions.size) instructions[i].opCode else null
        val params =  if (i < instructions.size) instructions[i].parameters.joinToString() else null
        val acc = context.acc
        println("$c: $i: $ins $params | $acc | $state")
    }
}

class ProgramContext(var count: Int = 0, var i: Int = 0, var acc: Int = 0, var state: State = State.WAITING) {
    enum class State {
        WAITING,
        RUNNING,
        HALTED,
        COMPLETE
    }

    fun copy(): ProgramContext {
        return ProgramContext(count, i, acc, state)
    }
}

class Instruction(s: String) {
    private val tokens = s.split(" ")
    var opCode = OpCode.from(tokens[0])
    val parameters = tokens.subList(1, tokens.size)
}

enum class OpCode {
    ACC,
    JMP,
    NOP;

    companion object {
        fun from(s: String): OpCode {
            return values().findLast { s.toUpperCase() == it.toString() } ?: NOP
        }
    }
}
