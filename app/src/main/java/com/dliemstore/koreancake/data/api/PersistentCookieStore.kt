package com.dliemstore.koreancake.data.api

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Cookie
import okhttp3.HttpUrl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersistentCookieStore @Inject constructor(@ApplicationContext context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        PREF_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveCookies(url: HttpUrl, cookies: List<Cookie>) {
        val cookieSet = cookies.map { it.toString() }.toSet()
        sharedPreferences.edit().putStringSet(url.host, cookieSet).apply()
    }

    fun loadCookies(url: HttpUrl): List<Cookie> {
        return sharedPreferences.getStringSet(url.host, emptySet())
            ?.mapNotNull { Cookie.parse(url, it) }
            ?: emptyList()
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        private const val PREF_NAME = "secure_cookie_prefs"
    }
}