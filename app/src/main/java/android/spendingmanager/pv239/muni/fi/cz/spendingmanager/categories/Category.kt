package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories

import android.os.Bundle
import android.spendingmanager.pv239.muni.fi.cz.spendingmanager.R
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Category : Serializable{

    @SerializedName("id")
    var id: String = ""// eg. food, housing,...

    @SerializedName("categoryName")
    var categoryName : String = ""

    @SerializedName("type")
    var type = CategoryType.DEFAULT

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