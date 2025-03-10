package com.dliemstore.koreancake.ui.viewmodel.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dliemstore.koreancake.data.source.remote.response.order.OrderDetailResponse
import com.dliemstore.koreancake.data.source.repository.order.OrderRepository
import com.dliemstore.koreancake.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(private val orderRepository: OrderRepository) :
    ViewModel() {
    private val _orderDetailState =
        MutableStateFlow<Resource<OrderDetailResponse>>(Resource.Loading())
    val orderDetailState: StateFlow<Resource<OrderDetailResponse>> = _orderDetailState

    private val _updateProgressState = MutableStateFlow<Resource<Unit>>(Resource.Loading())
    val updateProgressState: StateFlow<Resource<Unit>> = _updateProgressState

    fun fetchOrderDetail(id: String) {
        viewModelScope.launch {
            delay(300)
            orderRepository.getOrderDetail(id).collect { _orderDetailState.value = it }
        }
    }

    fun updateProgress(orderId: String, progressId: String, isFinish: Boolean) {
        viewModelScope.launch {
            _updateProgressState.value = Resource.Loading()
            orderRepository.updateOrderProgress(orderId, progressId, isFinish).collect { result ->
                _updateProgressState.value = result
                if (result is Resource.Success) {
                    _orderDetailState.value = _orderDetailState.value.let { currentState ->
                        if (currentState is Resource.Success) {
                            val updatedProgresses = currentState.data?.progresses?.map {
                                if (it.id == progressId) it.copy(isFinish = isFinish) else it
                            } ?: emptyList()

                            val updatedOrder = currentState.data?.copy(
                                status = if (updatedProgresses.all { it.isFinish }) "Selesai" else "Proses",
                                progresses = updatedProgresses
                            )
                            Resource.Success(updatedOrder, result.code)
                        } else {
                            currentState
                        }
                    }
                }
            }
        }
    }
}