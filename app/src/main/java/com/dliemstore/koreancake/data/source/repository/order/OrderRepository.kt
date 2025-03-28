package com.dliemstore.koreancake.data.source.repository.order

import android.content.Context
import android.net.Uri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dliemstore.koreancake.data.api.service.OrderService
import com.dliemstore.koreancake.data.paging.BasePagingSource
import com.dliemstore.koreancake.data.source.remote.request.order.AddOrderRequest
import com.dliemstore.koreancake.data.source.remote.request.order.UpdateOrderProgressRequest
import com.dliemstore.koreancake.data.source.remote.response.order.OrdersResponse
import com.dliemstore.koreancake.util.ApiUtils
import com.dliemstore.koreancake.util.FileUtils.uriToFile
import com.dliemstore.koreancake.util.Resource
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val orderService: OrderService
) {
    fun addOrder(
        context: Context,
        pictureUris: List<Uri>,
        addOrderRequest: AddOrderRequest
    ) = flow {
        emit(Resource.Loading())

        val jsonBody = Gson().toJson(addOrderRequest)
        val data = jsonBody.toRequestBody("application/json".toMediaType())
        val fileParts = pictureUris.map { uri ->
            val file = uriToFile(context, uri)
            val mimeType: String = context.contentResolver.getType(uri) ?: "image/jpeg"
            val requestFile = file.asRequestBody(mimeType.toMediaType())
            MultipartBody.Part.createFormData("pictures", file.name, requestFile)
        }

        try {
            val response = orderService.add(fileParts, data)
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
    }

    fun getOrders(status: String): Flow<PagingData<OrdersResponse>> {
        val limit = 20
        return Pager(
            config = PagingConfig(
                pageSize = limit,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                BasePagingSource { page ->
                    orderService.getAll(page, limit, status)
                }
            }
        ).flow.flowOn(Dispatchers.IO)
    }

    fun getOrderDetail(id: String) = flow {
        emit(Resource.Loading())

        try {
            val response =
                orderService.getById(id)
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

    fun deleteOrder(id: String) = flow {
        emit(Resource.Loading())

        try {
            val response = orderService.deleteById(id)
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

    fun updateOrderProgress(orderId: String, progressId: String, isFinish: Boolean) = flow {
        emit(Resource.Loading())

        try {
            val response =
                orderService.updateOrderProgress(
                    orderId,
                    progressId,
                    UpdateOrderProgressRequest(isFinish)
                )
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