package com.dliemstore.koreancake.data.api.service

import com.dliemstore.koreancake.data.source.remote.response.PaginationSuccessResponse
import com.dliemstore.koreancake.data.source.remote.response.order.OrdersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OrderService {
    @GET("orders")
    suspend fun getAll(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("status") status: String
    ): Response<PaginationSuccessResponse<OrdersResponse>>
}