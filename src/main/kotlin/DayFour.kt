class DayFour(file: String) : Project {
    private val passports = getPassports(file)

    private fun getPassports(file: String): List<Passport> {
        var s = ""
        val output = ArrayList<Passport>()
        getLines(file).forEach {
            if (it.isBlank()) {
                output.add(Passport(s))
                s = ""
            } else {
                s += "$it "
            }
        }
        output.add(Passport(s))

        return output
    }

    override fun part1(): Int {
        return passports.filter { it.isValid() }.size
    }

    override fun part2(): Int {
        return passports.filter { it.isValid2() }.size
    }

    class Passport(arg1: String) {
        private val fields = setFields(arg1)
        companion object Constants {
            private val EyeColors = listOf(
                "amb",
                "blu",
                "brn",
                "gry",
                "grn",
                "hzl",
                "oth"
            )

            val RequiredFields : Map<String, (String) -> Boolean> = mapOf(
                "byr" to { t: String -> t.toIntOrNull() ?: -1 in 1920..2002 },
                "iyr" to { t: String -> t.toIntOrNull() ?: -1 in 2010..2020 },
                "eyr" to { t: String -> t.toIntOrNull() ?: -1 in 2020..2030 },
                "hgt" to { t: String -> validHeight(t) },
                "hcl" to { t: String -> t.matches("#[0-9a-f]{6}".toRegex()) },
                "ecl" to { t: String -> EyeColors.contains(t) },
                "pid" to { t: String -> t.matches("[\\d]{9}".toRegex()) }
                // "cid" now Optional
            )

            private fun validHeight(t: String): Boolean {
                val cms = t.endsWith("cm")
                val inches =  t.endsWith("in")

                if (!cms && !inches) return false

                val num = t.replace("(cm|in)$".toRegex(), "").toIntOrNull() ?: -1
                return if (cms) num in 150..193 else num in 59..76
            }
        }

        private fun setFields(arg1: String): HashMap<String, String> {
            val output = HashMap<String, String>()

            arg1.split(" ").forEach {
                val split = it.split(":")
                if (split[0].isNotBlank() && split[1].isNotBlank()) {
                    output[split[0]] = split[1]
                }
            }

            return output
        }

        fun isValid() : Boolean {
            return RequiredFields.keys.all { fields.containsKey(it) }
        }

        fun isValid2() : Boolean {
            return RequiredFields.keys.all {
                fields.containsKey(it) && (RequiredFields[it] ?: { false }).invoke(fields[it] ?: "")
            }
        }
    }
}