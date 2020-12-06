class DaySix(file: String) : Project {
    private val customsForms : List<CustomsFormGroup> = getCustomsForms(file)

    private fun getCustomsForms(file: String): List<CustomsFormGroup> {
        val output = ArrayList<CustomsFormGroup>()
        var group = ArrayList<CustomsForm>()
        getLines(file).forEach {
            if (it.isBlank()) {
                output.add(CustomsFormGroup(group))
                group = ArrayList()
            } else {
                group.add(CustomsForm(it))
            }
        }
        output.add(CustomsFormGroup(group))

        return output
    }

    override fun part1(): Int {
        return customsForms.map { it.countAnswers() }.sum()
    }

    override fun part2(): Int {
        return customsForms.map { it.countAllYes() }.sum()
    }
}

class CustomsFormGroup(private val forms: List<CustomsForm>) {
    private val uniqueAnswers = forms.fold(emptySet<String>(), {form, curr -> form.union(curr.answers.keys) })

    fun countAnswers(): Int {
        return uniqueAnswers.size
    }

    fun countAllYes(): Int {
        return uniqueAnswers.filter { forms.all { form -> form.answers.contains(it) } }.size
    }
}

class CustomsForm(val answerStr: String = "") {
    val answers = answerStr.split("").filter{ it != "" }.map { it to true }.toMap()
}
