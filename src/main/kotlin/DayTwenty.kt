class DayTwenty(file: String): Project {
    val pieces: List<Piece> = whitelineSeperatedGrouper(file, { Piece(it) }, { it.split("").subList(1, it.length + 1)})

    class Piece() {
        var number = 0L
        var rows = ArrayList<ArrayList<String>>()

        constructor(rawRows: List<List<String>>) : this() {
            rawRows.forEach {
                if (it[0] == "T") {
                    number = it.joinToString("").split(" ")[1].split(":")[0].toLong()
                } else {
                    rows.add(ArrayList(it))
                }
            }
        }

        constructor(number: Long, rows: ArrayList<ArrayList<String>>) : this() {
            this.number = number
            this.rows = rows
        }

        fun rotate(): Piece {
            val newPiece = clone()

            rows.forEachIndexed { y, yit ->
                yit.forEachIndexed { x, _ ->
                    newPiece.rows[y][x] = rows[rows.size - x - 1][y]
                }
            }

            return newPiece
        }

        fun flip(): Piece {
            val newPiece = clone()

            rows.forEachIndexed { y, yit ->
                yit.forEachIndexed { x, _ ->
                    newPiece.rows[y][x] = rows[rows.size - y - 1][x]
                }
            }

            return newPiece
        }

        fun fits(other: Piece): Piece? {
            var workingOther = other.clone()

            for (j in 0..1) {
                for (i in 0..3) {
                    // top bottom
                    val topsMatch = getTop() == workingOther.getBottom()
                    // bottom top
                    val bottomsMatch = getBottom() == workingOther.getTop()
                    // left right
                    val leftsMatch = getLeft() == workingOther.getRight()
                    // right left
                    val rightsMatch = getRight() == workingOther.getLeft()

                    if (topsMatch || bottomsMatch || leftsMatch || rightsMatch) {
                        return workingOther
                    }
                    workingOther = workingOther.rotate()
                }
                workingOther = workingOther.rotate().flip()
            }

            return null
        }

        fun getTop(): List<String> {
            return rows[0]
        }

        fun getBottom(): List<String> {
            return rows[rows.size-1]
        }

        fun getLeft(): List<String> {
            return rows.map { it[0] }
        }

        fun getRight(): List<String> {
            return rows.map { it[it.size-1] }
        }

        fun getBorderLessRows(): ArrayList<ArrayList<String>> {
            val subList: ArrayList<ArrayList<String>> = ArrayList(rows.subList(1, rows.size - 1))
            return ArrayList(subList.map { ArrayList(it.subList(1, it.size-1)) })
        }

        private fun clone(): Piece {
            return Piece(number, clone(rows))
        }

        private fun clone(toCopy: ArrayList<ArrayList<String>>): ArrayList<ArrayList<String>> {
            val clone: ArrayList<ArrayList<String>> = toCopy.clone() as ArrayList<ArrayList<String>>
            return clone.map { it.clone() as ArrayList<String>} as ArrayList<ArrayList<String>>
        }

        override fun toString(): String {
            return "Tile $number:\n" + rows.map { it.joinToString("") }.joinToString("\n")
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Piece) return false

            if (number != other.number) return false

            return true
        }

        override fun hashCode(): Int {
            return number.hashCode()
        }
    }

    override fun part1(): Any {
        val fitCounts = HashMap<Long, Int?>()

        pieces.forEach { a ->
            pieces.forEach { b ->
                if (a != b) {
                    if (a.fits(b) != null) {
                        fitCounts.computeIfAbsent(a.number) { 0 }
                        fitCounts[a.number] = fitCounts[a.number]?.plus(1)
                    }
                }
            }
        }

        return fitCounts.filter { it.value == 2 }.map { it.key }.fold(1L, {a, b -> a * b})
    }

    override fun part2(): Any {
        var assembledPuzzle:ArrayList<ArrayList<Piece>>? = null

        while (assembledPuzzle == null) {
            val fitMap: MutableMap<Piece, MutableList<Piece>> = HashMap()
            pieces.forEach { a ->
                pieces.forEach { b ->
                    if (a != b) {
                        if (a.fits(b) != null) {
                            fitMap.computeIfAbsent(a) { ArrayList() }
                            fitMap[a]?.add(b)
                        }
                    }
                }
            }

            assembledPuzzle = getPicture(fitMap)
        }

        val picture = printPicture(assembledPuzzle)

        val head = "..................#."
        val mon1 = head.toRegex()
        val mon2 = "#....##....##....###".toRegex()
        val mon3 = ".#..#..#..#..#..#...".toRegex()

        var bigPiece = Piece(picture.map { it.split("").subList(1,it.length+1) })
        var hasMonster = false
        var monsterCount = 0

        out@ for (a in 0..1) {
            for (b in 0..3) {
                for (i in 0..picture.size - 3) {
                    val line1 = bigPiece.rows[i].joinToString("")
                    val line2 = bigPiece.rows[i + 1].joinToString("")
                    val line3 = bigPiece.rows[i + 2].joinToString("")

                    for (j in 0..line1.length - head.length) {
                        val line1Window = line1.substring(j, j + head.length)
                        val line2Window = line2.substring(j, j + head.length)
                        val line3Window = line3.substring(j, j + head.length)

                        if (mon1.matches(line1Window) && mon2.matches(line2Window) && mon3.matches(line3Window)) {
                            monsterCount++
                        }
                    }
                }
                bigPiece = bigPiece.rotate()
            }
            bigPiece = bigPiece.rotate().flip()
        }

        val hashes = bigPiece.toString().count { '#' == it }
        val monsterHashes = 15 * monsterCount
        return hashes - monsterHashes
    }

    private fun printPicture(picture:ArrayList<ArrayList<Piece>>?): List<String> {
        val lines = ArrayList<String>()

        picture?.forEach { row ->
            for (i in 0 until row[0].getBorderLessRows().size) {
                lines.add(row.joinToString("") { it.getBorderLessRows()[i].joinToString("") })
            }
        }

        return lines
    }

    private fun getPicture(fitMap: MutableMap<Piece, MutableList<Piece>>): ArrayList<ArrayList<Piece>>? {
        val cornerEntries: List<MutableMap.MutableEntry<Piece, MutableList<Piece>>> = fitMap.entries.filter { it.value.size == 2 }
        val corners = cornerEntries.map { it.key }
        val cornerEntry: MutableMap.MutableEntry<Piece, MutableList<Piece>> = cornerEntries.first()
        var corner = cornerEntry.key
        val connections = cornerEntry.value

        // Make a piece fit to the right
        var rightConnection: Piece? = orientAndFindRight(corner, connections)

        if (rightConnection == null) {
            corner.flip()
            rightConnection = orientAndFindRight(corner, connections)!!
        }

        connections.remove(rightConnection)
        if (orientAndFindDown(corner, connections) == null) {
            corner = corner.flip()
            rightConnection = rightConnection.flip()
        }

        val picture = ArrayList<ArrayList<Piece>>()
        var row = ArrayList<Piece>()
        row.add(corner)
        row.add(rightConnection)

        var next = rightConnection
        fitMap[next]?.remove(corner)
        fitMap[corner]?.remove(next)

        var rowStart = corner

        var piecesPlaced = 2

        while (piecesPlaced < pieces.size) {
            val last = next
            next = orientAndFindRight(next!!, fitMap[next])!!

            fitMap[last]?.remove(next)
            fitMap[next]?.remove(last)

            row.add(next)
            piecesPlaced++


            if (corners.contains(next) || (picture.size > 0 && row.size == picture[picture.size - 1].size)) {
                picture.add(row)
                val lastRowStart = rowStart

                if (piecesPlaced < pieces.size) {
                    rowStart = orientAndFindDown(rowStart, fitMap[rowStart])!!
                    fitMap[rowStart]?.remove(lastRowStart)
                    fitMap[lastRowStart]?.remove(rowStart)

                    row = ArrayList()
                    row.add(rowStart)
                    piecesPlaced++
                    next = rowStart
                } else {
                    break
                }
            }
        }

        return picture
    }

    private fun orientAndFindDown(next: Piece, connections: MutableList<Piece>?): Piece? {
        return connections?.map { next.fits(it) }?.firstOrNull { next.getBottom() == it?.getTop() }
    }

    private fun orientAndFindRight(next: Piece, connections: MutableList<Piece>?): Piece? {
        return connections?.map { next.fits(it) }?.firstOrNull { next.getRight() == it?.getLeft() }
    }
}