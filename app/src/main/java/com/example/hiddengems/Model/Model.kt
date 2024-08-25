package com.example.hiddengems.Model

class Model private constructor() {

    val categories: MutableList<Category> = ArrayList()
    val gems: MutableList<Gem> = ArrayList()

    companion object {
        val instance: Model = Model()
    }

    init {

        categories.add(Category(0, "All", "ferris_svgrepo_com"))
        categories.add(Category(1, "Cafe/Restaurant", "restaurant_svgrepo_com"))
        categories.add(Category(2, "Museum", "museum_svgrepo_com"))
        categories.add(Category(3, "Park", "park_svgrepo_com"))

        gems.add(Gem(0,"Billy", "Grande Coffee", "Hidden coffee shop by the park, cozy and homey atmosphere with fantastic pastries!",
            "76 rue Leon Dierx","Paris, FR","Cafe/Restaurant", 2.5,mutableListOf(2, 3, 3)))
        gems.add(Gem(1,"Jane", "Illusion Museum","You won't believe your eyes in this illusion museum!",
            "70 Griffin St" ,"NYC, USA","Museum", 3.6,mutableListOf(5, 3, 3)))
        gems.add(Gem(2,"Bobby", "Side Street Park","Beautiful park with pastoral views",
            "3 Via Nino Martoglio", "Rome, It","Park", 3.0,mutableListOf( 3, 3)))
        gems.add(Gem(3,"Billy", "Soup-y", "Tiny restaurant run by a small family, hidden just out of view!",
            "174 Quai de Jemmapes","Paris, FR","Cafe/Restaurant",0.0, mutableListOf()))

    }


}