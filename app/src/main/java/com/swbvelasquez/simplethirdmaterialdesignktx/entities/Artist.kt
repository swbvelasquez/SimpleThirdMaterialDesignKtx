package com.swbvelasquez.simplethirdmaterialdesignktx.entities

data class Artist(
    var id:Long=0,
    var firstName:String,
    var lastName:String,
    var birthDate:Long=0,
    var birthPlace:String,
    var height:Int,
    var notes:String,
    var order:Int=0,
    var photoUrl:String="",
){
    val fullName: String
        get() = "$firstName $lastName"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Artist

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
