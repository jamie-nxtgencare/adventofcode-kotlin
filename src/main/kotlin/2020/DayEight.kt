@file:Suppress("PackageName")

package `2020`

import Project
import program.OpCode
import program.Program
import program.ProgramContext

class DayEight(file: String) : Project() {
    val program = Program(getLines(file)/*, debug = true*/)

    override fun part1(): Int {
        program.run()
        return program.context.acc
    }

    override fun part2(): Int {
        var c = 0
        while(program.context.state != ProgramContext.State.COMPLETE) {
            program.reset()
            for (i in c until program.instructions.size) {
                if (program.instructions[i].opCode == OpCode.JMP) {
                    program.instructions[i].opCode = OpCode.NOP
                    c = i + 1
                    break
                }
            }
            program.run()
        }

        return program.context.acc
    }
}