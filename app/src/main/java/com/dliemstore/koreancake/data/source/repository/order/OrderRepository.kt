package com.dliemstore.koreancake.data.source.repository.order

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dliemstore.koreancake.data.api.service.OrderService
import com.dliemstore.koreancake.data.paging.BasePagingSource
import com.dliemstore.koreancake.data.source.remote.response.order.OrdersResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val orderService: OrderService
) {
    fun getOrders(status: String): Flow<PagingData<OrdersResponse>> {
        val limit = 10
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
}