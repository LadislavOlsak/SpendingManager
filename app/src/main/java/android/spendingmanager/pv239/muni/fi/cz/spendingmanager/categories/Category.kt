package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories

import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R

class Category {

    lateinit var id: String // eg. food, housing,...
    lateinit var categoryName : String
    lateinit var type : CategoryType

    var key : String? = null

    constructor()

    constructor(id: String, categoryName: String, type: CategoryType) {
        this.id = id
        this.categoryName = categoryName
        this.type = type
    }
}

enum class CategoryType {
    DEFAULT, CUSTOM
}