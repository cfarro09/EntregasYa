package com.delycomps.entregasya.cache

import android.content.Context
import android.content.SharedPreferences

class SharedPrefsCache(context: Context) {

    private val pref: SharedPreferences = context.getSharedPreferences("MyPref", 0)

    fun getToken(): String{
        return pref.getString("token", "string")!!
    }

    fun get(key: String, type: String): Any? {
        return when (type) {
            "int" -> pref.getInt(key, 0)
            "bool" -> pref.getBoolean(key, false)
            "float" -> pref.getFloat(key, 0.toFloat())
            "string" -> pref.getString(key, "")
            else -> {
                null
            }
        }
    }

    fun set(key: String, value: Any, type: String) {
        val editor: SharedPreferences.Editor = pref.edit()
        when (type) {
            "int" -> editor.putInt(key, value as Int)
            "bool" -> editor.putBoolean(key, value as Boolean)
            "float" -> editor.putFloat(key, value as Float)
            "string" -> editor.putString(key, value as String)
        }
        editor.apply() // commit changes
    }
}