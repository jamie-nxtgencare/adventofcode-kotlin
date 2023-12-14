@file:Suppress("PackageName")

package `2023`

import Project

class DayTen(file: String) : Project() {
    val nodeRows = getLines(file).mapIndexed { y, it -> it.split("").filter { it.isNotBlank() }.mapIndexed { x, symbol -> Node(x, y, symbol) }}

    class Node(val x: Int, val y: Int, val symbol: String) {
        var mightConnectTo: Pair<Node, Node>? = null
        var connects: Pair<Node?, Node?>? = null
        var start = symbol == "S"

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
    }

    override fun part1(): Any {
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
            println("Starting with: $mightLoopNode")
            nodeRows.forEach { row ->
                row.forEach { node ->
                    print(if (node == mightLoopNode) "!" else if (node == start) "S" else if (node.connects == null) "." else node.symbol)
                }
                println()
            }

            var prev = mightLoopNode
            var curr = mightLoopNode.getOtherConnection(start)

            println()
            println()

            var count = 0
            while (curr != start && curr != null) {
                println(curr)
/*                nodeRows.forEach { row ->
                    row.forEach { node ->
                        print(if(node == curr) "!" else if(node == start) "S" else if (node.connects == null) "." else node.symbol)
                    }
                    println()
                }*/

                val tmp = curr.getOtherConnection(prev)
                prev = curr
                curr = tmp
                count++

                println()
                println()
            }

            if (curr == start) {
                return count / 2 + 1
            }
        }

        return -1
    }

    override fun part2(): Any {
        return -1
    }
}