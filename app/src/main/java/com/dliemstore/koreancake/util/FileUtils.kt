package com.dliemstore.koreancake.util

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.buffer
import okio.sink
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class FileUtils @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun uriToFile(uri: Uri): File {
        val contentResolver = context.contentResolver
        val file = File(context.cacheDir, "temp_${System.currentTimeMillis()}.jpg")

        contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        return file
    }

    suspend fun downloadFileToUri(url: String): Uri? = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder().url(url).build()
            val client = OkHttpClient()
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) return@withContext null

            val fileName = url.substringAfterLast("/")
            val file = File(context.cacheDir, fileName)
            val sink = file.sink().buffer()
            sink.writeAll(response.body!!.source())
            sink.close()

            file.toUri()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}