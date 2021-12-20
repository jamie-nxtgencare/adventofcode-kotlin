class DayNineteen(file: String) : Project {
    private val lines = getLines(file)
    private val scanners = ArrayList<Scanner>()

    init {
        var name: String? = null
        var beacons = ArrayList<Point3D>()
        var scanner: Scanner? = null
        for (line in lines) {
            if (name == null) {
                name = line
                scanner = Scanner(name, beacons)
            } else {
                if (line.isBlank()) {
                    scanners.add(scanner!!)
                    scanner = null
                    name = null
                    beacons = ArrayList()
                } else {
                    val point = line.split(",").map { it.toInt() }
                    scanner?.beacons?.add(Point3D(point[0], point[1], point[2]))
                }
            }
        }
        scanners.add(scanner!!)
        scanners.reversed()
    }

    override fun part1(): Any {
        var result = scan(scanners)

        while (result.second.isNotEmpty()) {
            val subResult = scan(ArrayList(result.second))
            subResult.second.add(result.first)
            subResult.second.add(subResult.first)
            result = scan(ArrayList(subResult.second))
        }

        result.first.beacons.sortBy { it.x }
        return result.first.beacons.size
    }

    private fun scan(scanners: java.util.ArrayList<Scanner>): Pair<Scanner, MutableList<Scanner>> {
        val targetScanner = scanners.first()
        val restScanners = scanners.subList(1,scanners.size)
        val knownBeacons: ArrayList<Point3D> = ArrayList(targetScanner.beacons)
        var desperationCount = 0

        giveup@ while (restScanners.isNotEmpty()) {
            var newKnownBeacons: ArrayList<Point3D>? = null
            foundOne@ for (targetBeacon in knownBeacons) {
                val testScanner = restScanners.first()
                for (testBeacon in testScanner.beacons) {
                    val restBeacons = testScanner.beacons.filter { it != testBeacon }
                    val restPerms = restBeacons.map { it.getPermutations() }

                    for (perm in testBeacon.getPermutations().values) {
                        // Let testBeacon be 0,0
                        val initTrans = perm.point.invert()
                        // Assume testBeacon is targetBeacon
                        val moveTrans = targetBeacon.clone()

                        // Get the same permutation we're getting for all the rest of the beacons
                        val resetRestBeacons = restPerms.map { it[perm.id]?.point?.translate(initTrans)?.translate(moveTrans)!! }

                        val matchedBeacons = resetRestBeacons.filter { knownBeacons.contains(it) }

                        if (matchedBeacons.size >= 11) { // 11 + the checked one is 12
                            restScanners.remove(testScanner)
                            println("Removing resolved scanner, ${restScanners.size} left")
                            newKnownBeacons = ArrayList(resetRestBeacons.filter { !knownBeacons.contains(it) })
                            break@foundOne
                        } else if (desperationCount > restScanners.size) {
                            println("Giving up and trying a different approach")
                            break@giveup
                        }
                    }
                }
            }
            if (newKnownBeacons != null) {
                knownBeacons.addAll(newKnownBeacons)
                println("Found some beacons, now at ${knownBeacons.size}")
                newKnownBeacons = null
                desperationCount = 0
            } else {
                // Shuffle scanners to check for a different one to add
                val first = restScanners.removeAt(0)
                restScanners.add(first)
                println("Trying ${restScanners.first()}")
                desperationCount++
            }
        }

        return Pair(Scanner("--- joined scanners ---", knownBeacons), restScanners)
    }

    override fun part2(): Any {
        return -1
    }

}

class Scanner(val name: String, val beacons: ArrayList<Point3D>) {
    override fun toString() = name
}

class Permutation(val id: Triple<Int, Int, Int>, val point: Point3D) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Permutation) return false

        if (point != other.point) return false

        return true
    }

    override fun hashCode(): Int {
        return point.hashCode()
    }
}

class Point3D(val x: Int, val y: Int, val z: Int) {
    var cachedPerms: java.util.HashMap<Triple<Int, Int, Int>, Permutation>? = null

    fun rotate() = Point3D(-y, x, z)
    fun roll() = Point3D(-z, y, x)
    fun pitch() = Point3D(x, -z, y)
    fun translate(amount: Point3D) = Point3D(x+amount.x, y+amount.y, z+amount.z)
    fun invert() = Point3D(-x,-y,-z)
    fun clone() = Point3D(x,y,z)

    fun getPermutations(): java.util.HashMap<Triple<Int, Int, Int>, Permutation> {
        if (cachedPerms != null) {
            return cachedPerms!!
        }
        val permutes = permute()
        cachedPerms = HashMap()

        for (permute in permutes) {
            cachedPerms!![permute.id] = permute
        }

        return cachedPerms!!
    }

    private fun permute(): LinkedHashSet<Permutation> {
        val set = LinkedHashSet<Permutation>()
        var perm = this
        set.add(Permutation(Triple(0,0,0), perm))

        for (i in 0..2) {
            for (j in 0..2) {
                for (k in 0..3) {
                    perm = perm.rotate()
                    set.add(Permutation(Triple(i,j,k+1), perm))
                }
                perm = perm.roll()
                set.add(Permutation(Triple(i,j,3), perm))
            }
            perm = perm.pitch()
            set.add(Permutation(Triple(i,2,3), perm))
        }

        return set
    }

    override fun toString() = "($x,$y,$z)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Point3D) return false

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + z
        return result
    }

}