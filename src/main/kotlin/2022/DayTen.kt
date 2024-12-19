@file:Suppress("PackageName")

package `2022`

import Project
import java.util.*

class DayTen(file: String, isTest: Boolean = false) : Project(file, isTest) {
    private val instructions = mapFileLines(file) { Instruction(it) }

    class Instruction(val s: String) {
        private val tokens = s.split(" ")
        val opCode = OpCode.valueOf(tokens[0].uppercase(Locale.getDefault()))
        val args = tokens.subList(1, tokens.size)
    }

    enum class OpCode(val cycles: Int, val func: (Program, List<String>) -> Unit) {
        NOOP(1, { _, _ -> }),
        ADDX(2, { p, args -> p.registers["X"] = p.registers["X"]!! + args.first().toInt() })
    }

    class InstructionExecutionContext(val index: Int, val instruction: Instruction) {
        var runTime = 0

        fun executionComplete(): Boolean {
            return runTime == instruction.opCode.cycles
        }

        fun execute(program: Program) {
            instruction.opCode.func(program, instruction.args)
        }
    }

    class Program(var runningInstruction: InstructionExecutionContext) {
        var tickCount = 0
        var registers = HashMap<String, Int>()
        var crtDisplay = ""

        init {
            registers["X"] = 1
        }

        fun tick(): Pair<Int, Int> {
            crtDisplay += currentPixel()
            tickCount++
            runningInstruction.runTime++
            println(tickCount.toString() + " : during: " + registers["X"]!!)
            val out = Pair(tickCount, signalStrength())

            if (runningInstruction.executionComplete()) {
                runningInstruction.execute(this)
                println("${tickCount}: ${runningInstruction.instruction.s} (line: ${runningInstruction.index + 1}) -- complete")
            }
            println(tickCount.toString() + " : after: " + registers["X"]!!)
            return out
        }

        private fun currentPixel(): String {
            val pixel = tickCount % 40
            val outputPixel = registers["X"]!!
            if (pixel in outputPixel - 1..outputPixel + 1) {
                return "#"
            }
            return "."
        }

        fun signalStrength(): Int {
            return tickCount * registers["X"]!!
        }
    }


    override suspend fun part1(): Any {
        val program = Program(InstructionExecutionContext(0, instructions[0]))
        val interestingSignals = listOf(20, 60, 100, 140, 180, 220)
        var signalStrength = 0

        instructions.forEachIndexed { index, it ->
            program.runningInstruction = InstructionExecutionContext(index, it)
            println("${program.tickCount + 1}: ${program.runningInstruction.instruction.s} (line: ${program.runningInstruction.index + 1}) -- started")

            while (!program.runningInstruction.executionComplete()) {
                val out = program.tick()
                if (interestingSignals.contains(out.first)) {
                    signalStrength += out.second
                }
            }
        }

        return signalStrength
    }

    override suspend fun part2(): Any {
        val program = Program(InstructionExecutionContext(0, instructions[0]))

        instructions.forEachIndexed { index, it ->
            program.runningInstruction = InstructionExecutionContext(index, it)
            println("${program.tickCount + 1}: ${program.runningInstruction.instruction.s} (line: ${program.runningInstruction.index + 1}) -- started")

            while (!program.runningInstruction.executionComplete()) {
                program.tick()
            }
        }

        for (i in 0 until program.crtDisplay.length step 40) {
            println(program.crtDisplay.substring(i, i+40))
        }
        return -1
    }

}