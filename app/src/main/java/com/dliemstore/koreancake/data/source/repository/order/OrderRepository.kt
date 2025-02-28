package com.dliemstore.koreancake.data.source.repository.order

import com.dliemstore.koreancake.data.api.service.OrderService
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val orderService: OrderService
) {
}