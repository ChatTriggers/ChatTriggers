package com.chattriggers.ctjs.utils

import gg.essential.vigilance.data.Category
import gg.essential.vigilance.data.SortingBehavior

object CategorySorting : SortingBehavior() {
    override fun getCategoryComparator(): Comparator<in Category> {
        return Comparator { o1, o2 ->
            val categories = listOf("General", "Console")

            if (o1.name !in categories || o2.name !in categories) {
                throw IllegalArgumentException("All categories must be in the list of categories")
            }

            categories.indexOf(o1.name) - categories.indexOf(o2.name)
        }
    }
}
