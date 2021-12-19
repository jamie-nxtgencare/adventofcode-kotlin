import kotlin.math.ceil
import kotlin.math.floor

class DayEighteen(file: String) : Project {
    private val equations = mapFileLines(file) { parse(it) }

    override fun part1(): Any {
        val aaa = reducedSum(equations)
        return aaa.getMagnitude()
    }

    override fun part2(): Any {
        return -1
    }

    companion object {
        fun reducedSum(equations: List<Equation>): Equation {
            return equations.reduce { eq1, eq2 ->
                val new = eq1.plus(eq2)
                new.reduce()
                new
            }
        }

        fun parse(input: String): Equation {
            val prep = input.replace(Regex("([\\[\\],]|\\d{1,2})"), "|$1")
            val tokens = prep.split("|").filter { it.isNotBlank() }

            val root = Equation()
            var curr = root

            for (token in tokens) {
                when (token) {
                    "[" -> {
                        val next = Equation()
                        next.parent = curr
                        curr.leftChild = next
                        curr = next
                    }
                    "]" -> {
                        curr = curr.parent!!
                    }
                    "," -> {
                        val next = Equation()
                        next.parent = curr.parent
                        curr.parent?.rightChild = next
                        curr = next
                    }
                    else -> {
                        curr.value = token.toInt()
                    }
                }
            }

            return root
        }
    }
}

class Equation {
    var value: Int? = null
    var parent: Equation? = null
    var leftChild: Equation? = null
    var rightChild: Equation? = null

    override fun toString(): String {
        if (hasValue()) {
            return value.toString()
        }
        return "[" +
            (if (hasLeft()) leftChild.toString() else "") +
           "," +
            (if (hasRight()) rightChild.toString() else "") +
            "]"
    }

    private fun hasValue(): Boolean {
        return value != null
    }

    private fun hasLeft(): Boolean {
        return leftChild != null
    }

    private fun hasRight(): Boolean {
        return rightChild != null
    }

    fun getMagnitude(): Long {
        if (hasValue()) return value?.toLong()!!

        val left = if (hasLeft()) 3 * leftChild?.getMagnitude()!! else 0
        val right = if (hasRight()) 2 * rightChild?.getMagnitude()!! else 0
        return left + right
    }

    fun plus(eq2: Equation): Equation {
        val newRoot = Equation()
        newRoot.leftChild = this
        newRoot.rightChild = eq2
        this.parent = newRoot
        eq2.parent = newRoot
        return newRoot
    }

    fun reduce() {
        var changed = true
        while (changed) {
            changed = explode()
            if (!changed) {
                changed = split()
            }
        }
    }

    fun explode(): Boolean {
        val traversal = this.traverse()

        for (curr in traversal) {
            if (curr.shouldExplode()) {
                curr.doExplode(traversal)
                return true
            }
        }

        return false
    }

    private fun doExplode(traversal: ArrayList<Equation>) {
        val index = traversal.indexOf(this)

        if (index > 0) {
            // will always be exploding the left child
            traversal[index - 1].value = value?.let { traversal[index - 1].value?.plus(it) }
        }
        if (index < traversal.size - 2) {
            // the right child will be the other have of this pair, so it it to the NEXT one
            traversal[index + 2].value = traversal[index + 1].value?.let { traversal[index + 2].value?.plus(it) }
        }

        this.parent?.leftChild = null
        this.parent?.rightChild = null
        this.parent?.value = 0
    }

    fun split(): Boolean {
        val traversal = this.traverse()

        for (curr in traversal) {
            if (curr.shouldSplit()) {
                curr.doSplit()
                return true
            }
        }

        return false
    }

    private fun doSplit() {
        val left = Equation()
        val right = Equation()
        val split = Equation()
        split.parent = parent
        split.leftChild = left
        split.rightChild = right
        left.parent = split
        right.parent = split
        left.value = floor(value?.div(2.0) ?: 0.0).toInt()
        right.value = ceil(value?.div(2.0) ?: 0.0).toInt()

        if (parent?.leftChild == this) {
            parent?.leftChild = split
        } else {
            parent?.rightChild = split
        }
    }

    fun traverse(): ArrayList<Equation> {
        if (hasValue()) {
            return ArrayList(listOf(this))
        }

        val traversal = if (hasLeft()) leftChild?.traverse()!! else ArrayList()

        if (hasRight()) {
            traversal.addAll(rightChild?.traverse()!!)
        }

        return traversal
    }

    private fun shouldExplode(): Boolean {
        return getDepth() > 4 && parent?.rightChild?.hasValue()!!
    }

    private fun shouldSplit(): Boolean {
        return value!! >= 10
    }

    private fun getDepth(): Int {
        var i = 0
        var curr = this
        while (curr.parent != null) {
            curr = curr.parent!!
            i++
        }

        return i
    }
}