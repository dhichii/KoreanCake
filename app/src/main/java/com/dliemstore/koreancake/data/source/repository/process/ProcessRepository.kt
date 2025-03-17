package com.dliemstore.koreancake.data.source.repository.process

import com.dliemstore.koreancake.data.api.service.ProcessService
import com.dliemstore.koreancake.data.source.remote.request.process.AddProcessRequest
import com.dliemstore.koreancake.data.source.remote.request.process.UpdateProcessesStepRequest
import com.dliemstore.koreancake.util.ApiUtils
import com.dliemstore.koreancake.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import javax.inject.Inject

class ProcessRepository @Inject constructor(
    private val processService: ProcessService
) {
    fun addProcess(name: String, step: Int) = flow {
        emit(Resource.Loading())

        try {
            val response = processService.add(AddProcessRequest(name, step))
            if (response.isSuccessful) {
                emit(Resource.Success(Unit, response.code()))
            } else {
                val errorResponse = runCatching { ApiUtils.parseError(response) }.getOrNull()
                val errorMessage = when (response.code()) {
                    500 -> "Terjadi kesalahan pada server"
                    else -> errorResponse?.message ?: "Permintaan tidak valid."
                }

                emit(Resource.Error(errorMessage, response.code(), errorResponse?.errors))
            }
        } catch (e: Exception) {
            val errorMessage = if (e is IOException) {
                "Tidak ada koneksi internet."
            } else {
                "Terjadi kesalahan yang tidak terduga"
            }
            emit(Resource.Error(errorMessage))
        }
    }.flowOn(Dispatchers.IO)

    fun getProcesses() = flow {
        emit(Resource.Loading())

        try {
            val response = processService.getAll()
            if (response.isSuccessful) {
                emit(Resource.Success(response.body()?.data, response.code()))
            } else {
                val errorResponse = runCatching { ApiUtils.parseError(response) }.getOrNull()
                val errorMessage = when (response.code()) {
                    500 -> "Terjadi kesalahan pada server"
                    else -> errorResponse?.message ?: "Permintaan tidak valid."
                }

                emit(Resource.Error(errorMessage, response.code(), errorResponse?.errors))
            }
        } catch (e: Exception) {
            val errorMessage = if (e is IOException) {
                "Tidak ada koneksi internet."
            } else {
                "Terjadi kesalahan yang tidak terduga"
            }
            emit(Resource.Error(errorMessage))
        }
    }.flowOn(Dispatchers.IO)

    fun delete(id: String) = flow {
        emit(Resource.Loading())

        try {
            val response = processService.delete(id)
            if (response.isSuccessful) {
                emit(Resource.Success(Unit, response.code()))
            } else {
                val errorResponse = runCatching { ApiUtils.parseError(response) }.getOrNull()
                val errorMessage = when (response.code()) {
                    500 -> "Terjadi kesalahan pada server"
                    else -> errorResponse?.message ?: "Permintaan tidak valid."
                }

                emit(Resource.Error(errorMessage, response.code(), errorResponse?.errors))
            }
        } catch (e: Exception) {
            val errorMessage = if (e is IOException) {
                "Tidak ada koneksi internet."
            } else {
                "Terjadi kesalahan yang tidak terduga"
            }
            emit(Resource.Error(errorMessage))
        }
    }.flowOn(Dispatchers.IO)

    fun updateProcessesStep(processes: List<UpdateProcessesStepRequest>) = flow {
        emit(Resource.Loading())

        try {
            val response = processService.updateProcessesStep(processes)
            if (response.isSuccessful) {
                emit(Resource.Success(Unit, response.code()))
            } else {
                val errorResponse = runCatching { ApiUtils.parseError(response) }.getOrNull()
                val errorMessage = when (response.code()) {
                    500 -> "Terjadi kesalahan pada server"
                    else -> errorResponse?.message ?: "Permintaan tidak valid."
                }

                emit(Resource.Error(errorMessage, response.code(), errorResponse?.errors))
            }
        } catch (e: Exception) {
            val errorMessage = if (e is IOException) {
                "Tidak ada koneksi internet."
            } else {
                "Terjadi kesalahan yang tidak terduga"
            }
            emit(Resource.Error(errorMessage))
        }
    }.flowOn(Dispatchers.IO)
}