class DaySixteen(file: String) : Project {
    private val packet = Packet(mapLettersPerLines(file) { it.map { c -> String.format("%04d", Integer.parseInt(Integer.toBinaryString(Integer.parseInt(c.toString(), 16)))) }}.flatten().joinToString(""))

    override fun part1(): Any {
        return packet.getVersionSum()
    }

    override fun part2(): Any {
        return -1
    }
}

class Packet(val input: String) {
    private val LITERAL = "100"

    private var i = 0
    private var version = -1
    private var type = ""
    private var lengthTypeId = ""
    private var literal = -1
    private val subpackets = ArrayList<Packet>()

    init {
        version = read(3).toInt(2)
        type = read(3)

        if (type == LITERAL) {
            var cont = read(1)
            var rest = read(4)
            while(cont == "1") {
                cont = read(1)
                rest += read(4)
            }
            literal = rest.toInt(2)
        } else {
            lengthTypeId = read(1)

            if (lengthTypeId == "0") {
                val subPacketBits = read(15).toInt(2)
                var rest = read(subPacketBits)

                while (rest.isNotBlank()) {
                    val packet = Packet(rest)
                    subpackets.add(packet)
                    rest = packet.rest()
                }
            } else {
                var remainingPackets = read(11).toInt(2)
                var rest = rest()
                while (remainingPackets-- > 0) {
                    val packet = Packet(rest)
                    subpackets.add(packet)
                    rest = packet.rest()
                }
            }
        }
    }

    private fun rest(): String {
        val rest = input.substring(i, input.length)
        i = input.length
        return rest
    }

    private fun read(count: Int): String {
        val out = input.substring(i, i + count)
        i += count
        return out
    }

    fun getVersionSum(): Int {
        return version + subpackets.sumOf { it.getVersionSum() }
    }
}
