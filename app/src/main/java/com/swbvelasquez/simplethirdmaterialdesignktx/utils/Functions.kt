package com.swbvelasquez.simplethirdmaterialdesignktx.utils

import android.content.Context
import android.widget.Toast
import com.google.gson.reflect.TypeToken
import com.swbvelasquez.simplethirdmaterialdesignktx.ArtistApplication
import java.lang.reflect.Type

object Functions {
    inline fun <reified T> T.toJson():String{
        return ArtistApplication.gson.toJson(this)
    }

    inline fun <reified T> String.fromJson():T{
        val type: Type = object : TypeToken<T>() {}.type
        return ArtistApplication.gson.fromJson(this,type)
    }

    fun showMessage(context:Context, stringId:Int){
        Toast.makeText(context,stringId,Toast.LENGTH_SHORT).show()
    }
}