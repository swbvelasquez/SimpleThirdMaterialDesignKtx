package com.swbvelasquez.simplethirdmaterialdesignktx

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class ArtistApplication:Application() {
    companion object{
        lateinit var gson: Gson
    }

    override fun onCreate() {
        super.onCreate()

        try {
            gson = GsonBuilder().create()
        }catch (ex:Exception){
            ex.printStackTrace()
        }
    }
}