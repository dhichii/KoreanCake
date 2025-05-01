package com.dliemstore.koreancake.data.api.service

import com.dliemstore.koreancake.data.source.remote.request.order.UpdateOrderProgressRequest
import com.dliemstore.koreancake.data.source.remote.response.CreatedResponse
import com.dliemstore.koreancake.data.source.remote.response.PaginationSuccessResponse
import com.dliemstore.koreancake.data.source.remote.response.SuccessResponse
import com.dliemstore.koreancake.data.source.remote.response.order.OrderDetailResponse
import com.dliemstore.koreancake.data.source.remote.response.order.OrdersResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface OrderService {
    @Multipart
    @POST("orders")
    suspend fun add(
        @Part pictures: List<MultipartBody.Part>,
        @Part("data") data: RequestBody
    ): Response<SuccessResponse<CreatedResponse<String>>>

    @GET("orders")
    suspend fun getAll(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("status") status: String
    ): Response<PaginationSuccessResponse<OrdersResponse>>

    @GET("orders/{id}")
    suspend fun getById(@Path("id") id: String): Response<SuccessResponse<OrderDetailResponse>>

    @Multipart
    @PUT("orders/{id}")
    suspend fun updateById(
        @Path("id") id: String,
        @Part addedPictures: List<MultipartBody.Part>,
        @Part("data") data: RequestBody
    ): Response<Unit>

    @DELETE("orders/{id}")
    suspend fun deleteById(@Path("id") id: String): Response<Unit>

    @PUT("orders/{id}/progresses/{progressId}")
    suspend fun updateOrderProgress(
        @Path("id") id: String,
        @Path("progressId") progressId: String,
        @Body request: UpdateOrderProgressRequest
    ): Response<Unit>
}