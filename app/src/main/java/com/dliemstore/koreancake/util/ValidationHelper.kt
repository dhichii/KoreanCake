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

    fun isValidInteger(value: String?, fieldName: String): String? {
        return if (value != null && value.toIntOrNull() == null) "$fieldName hanya boleh berupa angka." else null
    }

    fun minInteger(value: String?, minValue: Int, fieldName: String): String? {
        val number = value?.toIntOrNull()
        return if (number != null && number < minValue) "$fieldName tidak boleh kurang dari $minValue." else null
    }
}