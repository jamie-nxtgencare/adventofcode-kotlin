@file:Suppress("PackageName")

package `2020`

import Project

class DaySix(file: String) : Project {
    private val customsForms : List<CustomsFormGroup> = getCustomsForms(file)

    private fun getCustomsForms(file: String): List<CustomsFormGroup> {
        return whitelineSeperatedGrouper(file, { CustomsFormGroup(it) }, { CustomsForm(it.toCharArray().map { it2 -> it2 to true }.toMap()) })
    }

    override fun part1(): Int {
        return customsForms.map { it.countAnswers() }.sum()
    }

    override fun part2(): Int {
        return customsForms.map { it.countAllYes() }.sum()
    }
}

class CustomsFormGroup(private val forms: List<CustomsForm>) {
    private val uniqueAnswers = forms.fold(emptySet<Char>(), {form, curr -> form.union(curr.answers.keys) })

    fun countAnswers(): Int {
        return uniqueAnswers.size
    }

    fun countAllYes(): Int {
        return uniqueAnswers.filter { forms.all { form -> form.answers.contains(it) } }.size
    }
}

class CustomsForm(val answers: Map<Char, Boolean>)
