package com.swbvelasquez.simplethirdmaterialdesignktx.entities

data class Artist(
    var id:Long=0,
    val firstName:String,
    val lastName:String,
    val birthDate:Long=0,
    val birthPlace:String,
    val height:Int,
    val notes:String,
    var order:Int=0,
    val photoUrl:String="",
){
    val fullName: String
        get() = "$firstName $lastName"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Artist

        if (id != other.id) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (birthDate != other.birthDate) return false
        if (birthPlace != other.birthPlace) return false
        if (height != other.height) return false
        if (notes != other.notes) return false
        if (order != other.order) return false
        if (photoUrl != other.photoUrl) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
