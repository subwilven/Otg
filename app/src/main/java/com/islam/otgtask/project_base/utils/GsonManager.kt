package com.islam.otgtask.project_base.utils

import android.text.TextUtils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import java.util.ArrayList
import java.util.Arrays
import java.util.Objects

object GsonManager {

    //TODO need to be tested
    //you should ovveride toString in the POJO to store only the values you waint
    fun <T> convertListToString(items: List<T>): String {
        Objects.requireNonNull(items)

        val gson = Gson()
        val objStrings = ArrayList<String>()
        for (item in items) {
            objStrings.add(gson.toJson(item))
        }
        return TextUtils.join("‚‗‚", objStrings)
    }

    //TODO need to be tested
    fun <T> convertStringToList(string: String): List<T> {
        Objects.requireNonNull(string)

        val gson = Gson()

        // retrieve the stored string and split it into array
        val objStrings = ArrayList(Arrays.asList(*TextUtils.split(string, "‚‗‚")))

        val items = ArrayList<T>()
        val type = object : TypeToken<T>() {

        }.type

        for (jObjString in objStrings) {
            val value = gson.fromJson<T>(jObjString, type)
            items.add(value)
        }
        return items
    }

}
