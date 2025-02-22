package com.dliemstore.koreancake.util

object ValidationHelper {
    fun required(value: String?, fieldName: String): String? {
        return if (value.isNullOrBlank()) "$fieldName tidak boleh kosong." else null
    }

    fun minLength(value: String?, minLength: Int, fieldName: String): String? {
        return if (value != null && value.length < minLength) {
            "$fieldName minimal harus $minLength karakter."
        } else null
    }

    fun maxLength(value: String?, maxLength: Int, fieldName: String): String? {
        return if (value != null && value.length > maxLength) {
            "$fieldName tidak boleh lebih dari $maxLength karakter."
        } else null
    }
}