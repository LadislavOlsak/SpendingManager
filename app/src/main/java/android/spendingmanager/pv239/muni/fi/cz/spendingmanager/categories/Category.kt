package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories

import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R

class Category(
        var id: String, // eg. food, housing,...
        var categoryName : String,
        var type : CategoryType
) {
}

enum class CategoryType {
    DEFAULT, CUSTOM
}