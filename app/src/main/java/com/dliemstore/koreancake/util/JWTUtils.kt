package com.dliemstore.koreancake.util

import android.util.Base64
import com.auth0.jwt.JWT
import org.json.JSONObject
import java.util.Date

object JWTUtils {
    fun decodePayload(token: String?): JSONObject? {
        return try {
            if (token.isNullOrEmpty()) return null

            val payload = JWT.decode(token).payload
            val decodedPayload = String(Base64.decode(payload, Base64.URL_SAFE))
            JSONObject(decodedPayload)
        } catch (e: Exception) {
            null
        }
    }

    fun getClaim(token: String?, key: String): String? {
        return decodePayload(token)?.optString(key)
    }

    fun isTokenExpired(token: String?): Boolean {
        return try {
            val jwt = JWT.decode(token)
            jwt.expiresAt?.before(Date()) ?: true
        } catch (e: Exception) {
            true
        }
    }
}