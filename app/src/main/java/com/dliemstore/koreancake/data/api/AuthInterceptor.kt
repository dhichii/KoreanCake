package com.dliemstore.koreancake.data.api

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.auth0.jwt.JWT
import com.auth0.jwt.exceptions.JWTDecodeException
import com.dliemstore.koreancake.MainActivity
import com.dliemstore.koreancake.data.api.service.AuthService
import okhttp3.Interceptor
import okhttp3.Response
import java.util.Date
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val context: Context,
    private val tokenManager: TokenManager,
    private val cookieStore: PersistentCookieStore,
    private val authServiceProvider: Provider<AuthService>
) : Interceptor {
    @Volatile
    private var isRefreshing = false
    private val lock = Any()

    override fun intercept(chain: Interceptor.Chain): Response {
        var token = tokenManager.getToken()
        val isTokenExpired = isTokenExpired(token)
        val request = chain.request().newBuilder()

        if (token != null && isTokenExpired && !isRefreshing) {
            synchronized(lock) {
                // make sure to check if the token is not updated yet
                if (isTokenExpired(tokenManager.getToken())) {
                    isRefreshing = true
                    val newToken = refreshToken()
                    isRefreshing = false

                    newToken?.let { tokenManager.saveToken(it) }
                }
            }
            token = tokenManager.getToken()
        }

        token?.let {
            request.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(request.build())
    }

    private fun isTokenExpired(token: String?): Boolean {
        return try {
            val decodedJWT = JWT.decode(token)
            decodedJWT.expiresAt.before(Date())
        } catch (e: JWTDecodeException) {
            true
        }
    }

    private fun refreshToken(): String? {
        return try {
            val authService = authServiceProvider.get()
            val response = authService.refresh().execute()

            if (!response.isSuccessful || response.body()?.data?.access == null) {
                logoutSession()
                return null
            }

            response.body()?.data?.access
        } catch (e: Exception) {
            logoutSession()
            null
        }
    }

    private fun logoutSession() {
        tokenManager.clearToken()
        cookieStore.clear()

        // switch to main thread to show toast
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(
                context,
                "Sesi habis, silahkan login lagi.",
                Toast.LENGTH_SHORT
            ).show()
        }

        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }
}