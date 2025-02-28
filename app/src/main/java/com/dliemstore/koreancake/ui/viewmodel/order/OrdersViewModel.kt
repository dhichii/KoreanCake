package com.dliemstore.koreancake.ui.viewmodel.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dliemstore.koreancake.data.source.remote.response.order.OrdersResponse
import com.dliemstore.koreancake.data.source.repository.order.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(private val orderRepository: OrderRepository) :
    ViewModel() {
    fun fetchOrders(status: String): Flow<PagingData<OrdersResponse>> {
        return orderRepository.getOrders(status).cachedIn(viewModelScope)
    }
}