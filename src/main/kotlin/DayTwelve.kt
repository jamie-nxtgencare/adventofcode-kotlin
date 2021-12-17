class DayTwelve(file: String) : Project {
    private val lines = getLines(file)
    private val nodes = HashMap<String, Node>()

    init {
        lines.forEach {
            val linkStr = it.split("-")
            val node1Str = linkStr[0]
            val node2Str = linkStr[1]

            val node1 = nodes.computeIfAbsent(node1Str) { s -> Node(s) }
            val node2 = nodes.computeIfAbsent(node2Str) { s -> Node(s) }

            node1.links.add(node2)
            node2.links.add(node1)
        }
    }

    override fun part1(): Any {
        return getPaths(1).filter { it.any { node -> node.isSmall }}.size
    }


    override fun part2(): Any {
        return getPaths(2).size
    }

    private fun getPaths(part: Int): ArrayList<ArrayList<Node>> {
        val start = nodes["start"]!!
        val end = nodes["end"]!!
        var workingPaths = ArrayList<ArrayList<Node>>()
        val paths = ArrayList<ArrayList<Node>>()

        val initialPath = ArrayList<Node>()
        initialPath.add(start)
        workingPaths.add(initialPath)

        while (workingPaths.isNotEmpty()) {
            val newWorkingPaths = ArrayList<ArrayList<Node>>()
            workingPaths.forEach {
                val nextSteps = it.last().links.filter { link -> link != start && !isVisited(it, link, part) }

                nextSteps.forEach { node ->
                    val newPath = ArrayList(it)
                    newPath.add(node)
                    if (newPath.last() == end) {
                        paths.add(newPath)
                    } else {
                        newWorkingPaths.add(newPath)
                    }
                }
            }
            workingPaths = newWorkingPaths
        }
        return paths
    }

    private fun isVisited(path: ArrayList<Node>, node: Node, part: Int): Boolean {
        val visited = node.isSmall && path.any { it == node }
        val hasSmallDoubleVisit = path.any { it.isSmall && path.count { maybeIt -> maybeIt == it } > 1 }
        return if (part == 1) visited else visited && hasSmallDoubleVisit
    }

}

class Node(label: String) {
    val links = ArrayList<Node>()
    val isSmall = label.matches(Regex("[a-z]*")) && label != "start" && label != "end"
}
