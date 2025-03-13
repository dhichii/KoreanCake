package com.dliemstore.koreancake.data.api.service

import com.dliemstore.koreancake.data.source.remote.request.order.UpdateOrderProgressRequest
import com.dliemstore.koreancake.data.source.remote.response.PaginationSuccessResponse
import com.dliemstore.koreancake.data.source.remote.response.SuccessResponse
import com.dliemstore.koreancake.data.source.remote.response.order.OrderDetailResponse
import com.dliemstore.koreancake.data.source.remote.response.order.OrdersResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface OrderService {
    @GET("orders")
    suspend fun getAll(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("status") status: String
    ): Response<PaginationSuccessResponse<OrdersResponse>>

    @GET("orders/{id}")
    suspend fun getById(@Path("id") id: String): Response<SuccessResponse<OrderDetailResponse>>

    @DELETE("orders/{id}")
    suspend fun deleteById(@Path("id") id: String): Response<Unit>

    @PUT("orders/{id}/progresses/{progressId}")
    suspend fun updateOrderProgress(
        @Path("id") id: String,
        @Path("progressId") progressId: String,
        @Body request: UpdateOrderProgressRequest
    ): Response<Unit>
}