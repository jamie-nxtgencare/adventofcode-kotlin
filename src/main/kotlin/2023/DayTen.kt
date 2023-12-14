@file:Suppress("PackageName")

package `2023`

import Project
import java.util.*

class DayTen(file: String) : Project() {
    private val lines = getLines(file)
    private val expandedLines = expand(lines)
    private val nodeRows = expandedLines.mapIndexed { y, it -> it.split("").filter { it.isNotBlank() }.mapIndexed { x, symbol -> Node(x, y, symbol) }}

    private val path = mutableListOf<Node?>()
    var length = -1

    private fun expand(lines: List<String>): List<String> {
        val newLines = mutableListOf<String>()

        lines.forEach { line ->
            var line1 = ""
            var line2 = ""
            var line3 = ""

            line.split("").forEach {
                if (it == "|") {
                    line1 += ".|."
                    line2 += ".|."
                    line3 += ".|."
                } else if (it == "-") {
                    line1 += "..."
                    line2 += "---"
                    line3 += "..."
                } else if (it == "L") {
                    line1 += ".|."
                    line2 += ".L-"
                    line3 += "..."
                } else if (it == "J") {
                    line1 += ".|."
                    line2 += "-J."
                    line3 += "..."
                } else if (it == "7") {
                    line1 += "..."
                    line2 += "-7."
                    line3 += ".|."
                } else if (it == "F") {
                    line1 += "..."
                    line2 += ".F-"
                    line3 += ".|."
                } else if (it == ".") {
                    line1 += "..."
                    line2 += "..."
                    line3 += "..."
                } else if (it == "S") {
                    line1 += ".|."
                    line2 += "-S-"
                    line3 += ".|."
                }
            }

            newLines.add(line1)
            newLines.add(line2)
            newLines.add(line3)
        }

        return newLines
    }

    init {
        var start: Node? = null
        val mightLoop = mutableListOf<Node>()

        for (y in nodeRows.indices) {
            for (x in nodeRows[y].indices) {
                val node = nodeRows[y][x]
                if (node.symbol == "|" && y - 1 >= 0 && y + 1 < nodeRows.size) {
                    val top = nodeRows[y - 1][x]
                    val bottom = nodeRows[y + 1][x]
                    node.mightConnectTo = Pair(top, bottom)
                } else if (node.symbol == "-" && x - 1 >= 0 && x + 1 < nodeRows[y].size) {
                    val left = nodeRows[y][x - 1]
                    val right = nodeRows[y][x + 1]
                    node.mightConnectTo = Pair(left, right)
                } else if (node.symbol == "L" && y - 1 >= 0 && x + 1 < nodeRows[y].size) {
                    val top = nodeRows[y - 1][x]
                    val right = nodeRows[y][x + 1]
                    node.mightConnectTo = Pair(top, right)
                } else if (node.symbol == "J" && y - 1 >= 0 && x - 1 >= 0) {
                    val top = nodeRows[y - 1][x]
                    val left = nodeRows[y][x - 1]
                    node.mightConnectTo = Pair(top, left)
                } else if (node.symbol == "7" && y + 1 < nodeRows.size && x - 1 >= 0) {
                    val bottom = nodeRows[y + 1][x]
                    val left = nodeRows[y][x - 1]
                    node.mightConnectTo = Pair(bottom, left)
                } else if (node.symbol == "F" && y + 1 < nodeRows.size && x + 1 < nodeRows[y].size) {
                    val bottom = nodeRows[y + 1][x]
                    val right = nodeRows[y][x + 1]
                    node.mightConnectTo = Pair(bottom, right)
                } else if (node.symbol == "S") {
                    start = node
                }
                if (node.mightConnectTo?.first?.start == true || node.mightConnectTo?.second?.start == true) {
                    mightLoop.add(node)
                }
            }
        }

        nodeRows.forEach { row ->
            row.forEach { node ->
                if (node.mightConnectTo?.first != null && (node.mightConnectTo?.first?.canConnectTo(node) == true || node.mightConnectTo?.first == start)) {
                    node.connects = Pair(node.mightConnectTo?.first, node.connects?.second)
                }
                if (node.mightConnectTo?.second != null && (node.mightConnectTo?.second?.canConnectTo(node) == true || node.mightConnectTo?.second == start)) {
                    node.connects = Pair(node.connects?.first, node.mightConnectTo?.second)
                }
            }
        }

        for (mightLoopNode in mightLoop) {
            path.clear()
            path.add(start)
            path.add(mightLoopNode)
            nodeRows.forEach { it.forEach { it.path = false }}

            /*println("Starting with: $mightLoopNode")
            nodeRows.forEach { row ->
                row.forEach { node ->
                    print(if (node == mightLoopNode) "!" else if (node == start) "S" else if (node.connects == null) "." else node.symbol)
                }
                println()
            }*/

            var prev = mightLoopNode
            var curr = mightLoopNode.getOtherConnection(start)
            path.add(curr)
            mightLoopNode.path = true
            start?.let { it.path = true }
            curr?.let { it.path = true }

/*            println()
            println()*/

            var count = 0
            while (curr != start && curr != null) {
                //println(curr)

                val tmp = curr.getOtherConnection(prev)
                prev = curr
                curr = tmp
                path.add(curr)
                curr?.let { it.path = true }
                count++

/*                println()
                println()*/
            }

            if (curr == start) {
                length = count / 2 + 1
            }
        }

        val nodes = nodeRows.flatten()
        val outside = nodes.filter { it.isTouchingOutside(nodeRows) }.toMutableSet()
        outside.forEach { it.outside = true }

        var oldSize = 0
        val nonPathNodes = nodes.filter { !it.path }

        while (outside.size != oldSize) {
            oldSize = outside.size
            val newOutside = nonPathNodes.filter { node ->
                val nonPathNeighbours = node.neighbours(nodeRows)
                nonPathNeighbours.any { it.outside }
            }

            newOutside.forEach { it.outside = true }
            outside.addAll(newOutside)
        }
    }

    class Node(val x: Int, val y: Int, val symbol: String) {
        var mightConnectTo: Pair<Node, Node>? = null
        var connects: Pair<Node?, Node?>? = null
        var start = symbol == "S"
        var outside = false
        var path = false

        fun coords(): String {
            return "($x, $y)"
        }

        fun getOtherConnection(node: Node?): Node? {
            if (connects?.first == node) {
                return connects?.second
            }
            return connects?.first
        }

        fun canConnectTo(node: Node?): Boolean {
            return mightConnectTo?.first == node || mightConnectTo?.second == node
        }

        override fun toString(): String {
            return "$symbol ${coords()}: connects ${connects?.first?.coords() ?: "nothing"} to ${connects?.second?.coords() ?: "nothing"}"
        }

        fun isTouchingOutside(nodeRows: List<List<Node>>): Boolean {
            return y == 0 || y == nodeRows.size - 1 || x == 0 || x == nodeRows[0].size - 1
        }

        fun neighbours(nodeRows: List<List<Node>>): List<Node> {
            return listOfNotNull(
                get(y - 1, x - 1, nodeRows), get(y - 1, x, nodeRows), get(y - 1, x + 1, nodeRows),
                get(y, x - 1, nodeRows), /*get(y,x, nodeRows),*/ get(y, x + 1, nodeRows),
                get(y + 1, x - 1, nodeRows), get(y + 1, x, nodeRows), get(y + 1, x + 1, nodeRows)
            )
        }

        private fun get(y: Int, x: Int, nodeRows: List<List<Node>>): Node? {
            if (y >= 0 && y <= nodeRows.size - 1 && x >= 0 && x <= nodeRows[y].size - 1) {
                return nodeRows[y][x]
            }
            return null
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Node) return false

            if (x != other.x) return false
            return y == other.y
        }

        override fun hashCode(): Int {
            var result = x
            result = 31 * result + y
            return result
        }
    }

    override fun part1(): Any {
        return length / 3
    }

    override fun part2(): Any {
        nodeRows.forEach { row ->
            row.forEach { node ->
                print(if (path.contains(node)) "▓" else if (node.outside) "O" else ".")
            }
            println()
        }

        val newNodeRows = mutableListOf<MutableList<Node>>()

        for (y in 1..nodeRows.size step 3) {
            val newRow = mutableListOf<Node>()
            for (x in 1..nodeRows[y].size step 3) {
                newRow.add(nodeRows[y][x])
            }
            newNodeRows.add(newRow)
        }

        newNodeRows.forEach { row ->
            row.forEach { node ->
                print(if (path.contains(node)) "▓" else if (node.outside) "O" else ".")
            }
            println()
        }

        return newNodeRows.flatten().filter { !it.outside && !it.path }.size
    }
}