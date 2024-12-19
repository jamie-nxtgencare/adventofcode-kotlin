@file:Suppress("PackageName")

package `2020`

import Project

class DayTwentyone(file: String): Project() {
    private lateinit var unmapped: Set<String>
    private var unmappedCount: Int = 0
    private val lines = getLines(file)
    private val ingredientCounts = HashMap<String, Int>()
    private var recipes = ArrayList<Recipe>()

    init {
        recipes.addAll(lines.map { line ->
            val tokens = line.split("(contains ")
            val ingredients = tokens[0].split(" ").filter { it.isNotEmpty() }

            ingredients.forEach {
                ingredientCounts.computeIfAbsent(it) { 0 }
                ingredientCounts[it] = ingredientCounts[it]?.plus(1) ?: 0
            }

            val allergens = tokens[1].trim(')').split(" ").filter { it.isNotEmpty() }.map { it.trim(',') }
            Recipe(ArrayList(allergens), ArrayList(ingredients))
        })

        ingredientCounts.entries.sortedByDescending { it.value }.forEach {
            val allergens = recipes.filter { recipe -> recipe.allergens.size > 0 }.firstOrNull { recipe -> recipe.ingredients.contains(it.key) }?.allergens

            if (allergens != null && allergens.size > 0) {
                val allergen = allergens.first()
                recipes.forEach { recipe ->
                    recipe.ingredients.remove(it.key)
                    recipe.allergens.remove(allergen)
                }
            }

            if (recipes.all { recipe -> recipe.allergens.size == 0 }) {
                unmapped = recipes.map { recipe -> recipe.ingredients }.flatten().toSet()
                unmappedCount = recipes.map { recipe -> recipe.ingredients.count() }.sum()
            }
        }
    }

    class Recipe(val allergens: ArrayList<String>, val ingredients: ArrayList<String>)

    override suspend fun part1(): Any {
        return unmappedCount
    }

    override suspend fun part2(): Any {
        val allergens = HashSet<String>()
        recipes.clear()
        recipes.addAll(lines.map { line ->
            val tokens = line.split("(contains ")
            val ingredients = tokens[0].split(" ").filter { it.isNotEmpty() }
            val recipeAllergens = tokens[1].trim(')').split(" ").filter { it.isNotEmpty() }.map { it.trim(',') }
            allergens.addAll(recipeAllergens)
            Recipe(ArrayList(recipeAllergens), ArrayList(ingredients))
        })

        recipes.forEach {
            it.ingredients.removeAll(unmapped)
        }

        val allergenIngredients = HashMap<String, String>()
        while(allergens.isNotEmpty()) {
            val toRemove = ArrayList<String>()
            allergens.forEach { allergen ->
                val allergenRecipes = recipes.filter { recipe -> recipe.allergens.contains(allergen) }
                val recipeIngredients = allergenRecipes.map { it.ingredients }
                val uniqueIngredients = recipeIngredients.flatten().toSet()

                val ingredientsInAllRecipes = uniqueIngredients.filter { recipeIngredients.all { recipe -> recipe.contains(it) } }

                if (ingredientsInAllRecipes.size == 1) {
                    val ingredient = ingredientsInAllRecipes.first()
                    toRemove.add(allergen)
                    allergenIngredients[allergen] = ingredient
                    recipes.forEach { recipe -> recipe.ingredients.remove(ingredient) }
                }
            }
            allergens.removeAll(toRemove)
        }

        return allergenIngredients.entries.sortedBy { e -> e.key }.joinToString(",") { e -> e.value }
    }
}