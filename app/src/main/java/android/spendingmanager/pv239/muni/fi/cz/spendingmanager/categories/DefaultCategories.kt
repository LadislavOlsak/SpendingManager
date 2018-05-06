package android.spendingmanager.pv239.muni.fi.cz.spendingmanager.categories

class DefaultCategories {

    companion object {
        fun getDefaultCategories() : Collection<Category> {
            return listOf(
                    Category("food", "Food & Drinks", CategoryType.DEFAULT),
                    Category("housing", "Housing", CategoryType.DEFAULT),
                    Category("shopping", "Shopping", CategoryType.DEFAULT),
                    Category("entertainment", "Entertainment", CategoryType.DEFAULT),
                    Category("others", "Others", CategoryType.DEFAULT)
            )
        }
    }
}