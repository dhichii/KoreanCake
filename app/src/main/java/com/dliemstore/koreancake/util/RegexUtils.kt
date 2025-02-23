package com.dliemstore.koreancake.util

object RegexUtils {
    private val EMAIL_REGEX = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$".toRegex()
    private val USERNAME_REGEX = "^[a-zA-Z0-9]*$".toRegex()
    private val PHONE_REGEX = "^628\\d{8,12}$".toRegex()

    fun isValidEmail(email: String) = EMAIL_REGEX.matches(email)

    fun isValidUsername(username: String) = USERNAME_REGEX.matches(username)

    fun isValidPhoneNumber(phone: String) = PHONE_REGEX.matches(phone)
}