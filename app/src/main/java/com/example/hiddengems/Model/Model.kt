package com.example.hiddengems.Model

class Model private constructor() {

    val categories: MutableList<Category> = ArrayList()
    val gems: MutableList<Gem> = ArrayList()
    val cities: MutableList<String> = ArrayList()
    var currUser: User

    companion object {
        val instance: Model = Model()
    }

    init {


       // val user:User = User("Billy","Experienced Traveller", mutableListOf(0,2), mutableListOf(1))
        //currUser = user
        val user:User = User()
        currUser = user

        cities.add("All")
        cities.add("Paris, FR")
        cities.add("NYC, USA")
        cities.add("Rome, IT")
        cities.add("London, UK")

        categories.add(Category(0, "All", "ferris_svgrepo_com"))
        categories.add(Category(1, "Cafe/Restaurant", "restaurant_svgrepo_com"))
        categories.add(Category(2, "Museum", "museum_svgrepo_com"))
        categories.add(Category(3, "Park", "park_svgrepo_com"))

        val comments1: MutableList<Comment> = ArrayList()
        comments1.add(Comment("Billy","My favorite cafe"))
        comments1.add(Comment("Jane","Thanks for the recommendation"))
        comments1.add(Comment("Alfy","Great service!"))
        gems.add(Gem(0,"Billy", "Grande Coffee", "Hidden coffee shop by the park, cozy and homey atmosphere with fantastic pastries!",
            "76 rue Leon Dierx","Paris, FR","Cafe/Restaurant", 2.5,0,mutableListOf(2, 3, 3),comments1))

        val comments2: MutableList<Comment> = ArrayList()
        comments2.add(Comment("Billy","What a trip!"))
        comments2.add(Comment("Jane","Great museum!"))
        gems.add(Gem(1,"Jane", "Illusion Museum","You won't believe your eyes in this illusion museum!",
            "70 Griffin St" ,"NYC, USA","Museum", 3.6,1,mutableListOf(5, 3, 3),comments2))

        gems.add(Gem(2,"Bobby", "Side Street Park","Beautiful park with pastoral views",
            "3 Via Nino Martoglio", "Rome, It","Park", 3.0,-1,mutableListOf( 3, 3)))
        gems.add(Gem(3,"Billy", "Soup-y", "Tiny restaurant run by a small family, hidden just out of view!",
            "174 Quai de Jemmapes","Paris, FR","Cafe/Restaurant",1.0, 0,mutableListOf(1)))

    }


}