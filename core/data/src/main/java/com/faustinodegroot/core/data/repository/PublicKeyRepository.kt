package com.faustinodegroot.core.data.repository

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PublicKeyRepository @Inject constructor(private val sharedPreferences: SharedPreferences) {

    fun savePublicKey(identifier: String, key: String) {
        sharedPreferences.edit().putString(identifier, key).apply()
    }

    fun loadPublicKey(identifier: String): String? {
        return sharedPreferences.getString(identifier, null)
    }

    fun getAllPublicKeys(): Map<String, String> {
        val allEntries = mutableMapOf<String, String>()
        for (entry in sharedPreferences.all.entries) {
            val key = entry.key
            val value = entry.value as? String
            if (value != null) {
                allEntries[key] = value
            }
        }
        return allEntries
    }
}