package com.example.classictestapp

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object SecurePreferences {

    private const val PREFS_FILENAME = "secure_prefs"
    private const val API_KEY = "api_key"

    private fun getEncryptedSharedPreferences(context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        return EncryptedSharedPreferences.create(
            context,
            PREFS_FILENAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun setApiKey(context: Context, apiKey: String) {
        getEncryptedSharedPreferences(context).edit().putString(API_KEY, apiKey).apply()
    }

    fun getApiKey(context: Context): String? {
        return getEncryptedSharedPreferences(context).getString(API_KEY, null)
    }
}