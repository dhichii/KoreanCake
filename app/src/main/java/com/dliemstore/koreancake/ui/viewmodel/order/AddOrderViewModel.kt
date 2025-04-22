package com.dliemstore.koreancake.ui.viewmodel.order

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dliemstore.koreancake.data.source.remote.request.order.AddOrderRequest
import com.dliemstore.koreancake.data.source.remote.response.process.ProcessResponse
import com.dliemstore.koreancake.data.source.repository.order.OrderRepository
import com.dliemstore.koreancake.data.source.repository.process.ProcessRepository
import com.dliemstore.koreancake.domain.validation.order.OrderFormValidator
import com.dliemstore.koreancake.domain.validation.order.OrderPicturesValidator
import com.dliemstore.koreancake.ui.state.order.OrderFormState
import com.dliemstore.koreancake.ui.state.order.OrderPicturesState
import com.dliemstore.koreancake.util.ApiUtils
import com.dliemstore.koreancake.util.Resource
import com.dliemstore.koreancake.util.addTimeToEpochDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddOrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val processRepository: ProcessRepository
) :
    ViewModel() {
    private val _addOrderState = MutableStateFlow(OrderFormState())
    val addOrderState = _addOrderState.asStateFlow()

    private val _selectedPictureState = MutableStateFlow(OrderPicturesState())
    val selectedPictureState = _selectedPictureState.asStateFlow()

    private val _progressesState =
        MutableStateFlow<Resource<List<ProcessResponse>>>(Resource.Loading())
    val progressesState: StateFlow<Resource<List<ProcessResponse>>> = _progressesState

    fun fetchProgresses() {
        viewModelScope.launch {
            delay(300)
            processRepository.getProcesses().collect { _progressesState.value = it }
        }
    }

    fun clearErrorMessage() {
        _addOrderState.update { it.copy(errorMessage = null) }
    }

    fun onInputChange(field: String, value: Any?) {
        val state = _addOrderState.value
        val newErrors = state.errors.toMutableMap()

        if (state.errorMessage != null) {
            clearErrorMessage()
        }

        val updatedState = when (field) {
            "size" -> state.copy(
                size = value as? String ?: state.size
            )

            "layer" -> state.copy(
                layer = value as? String ?: state.layer
            )

            "text" -> state.copy(
                text = value as? String ?: state.text
            )

            "textColor" -> state.copy(
                textColor = value as? String ?: state.textColor
            )

            "isUseTopper" -> state.copy(
                isUseTopper = value as? Boolean ?: state.isUseTopper
            )

            "pickupDate" -> state.copy(
                pickupDate = value as? Long ?: state.pickupDate
            )

            "pickupHour" -> state.copy(
                pickupHour = value as? String ?: state.pickupHour
            )

            "pickupMinute" -> state.copy(
                pickupMinute = value as? String ?: state.pickupMinute
            )

            "telp" -> state.copy(
                telp = value as? String ?: state.telp
            )

            "price" -> state.copy(
                price = value as? String ?: state.price
            )

            "downPayment" -> state.copy(
                downPayment = value as? String ?: state.downPayment
            )

            "progresses" -> state.copy(
                progresses = if (value is List<*>) value.filterIsInstance<String>() else state.progresses
            )

            "notes" -> state.copy(
                notes = value as? String ?: state.notes
            )

            else -> state
        }

        val fieldError = OrderFormValidator.validate(
            size = updatedState.size,
            layer = updatedState.layer,
            text = updatedState.text,
            textColor = updatedState.textColor,
            pickupDate = updatedState.pickupDate,
            pickupHour = updatedState.pickupHour,
            pickupMinute = updatedState.pickupMinute,
            telp = updatedState.telp,
            price = updatedState.price,
            downPayment = updatedState.downPayment,
            progresses = updatedState.progresses,
        )[field]

        if (fieldError != null) {
            newErrors[field] = fieldError
        } else {
            newErrors.remove(field)
        }

        _addOrderState.value = updatedState.copy(errors = newErrors)
    }

    fun addOrder(context: Context) {
        val selectedPicturesValidationResult =
            OrderPicturesValidator.validate(_selectedPictureState.value.pictures)
        val validationResult = OrderFormValidator.validate(
            _addOrderState.value.size,
            _addOrderState.value.layer,
            _addOrderState.value.text,
            _addOrderState.value.textColor,
            _addOrderState.value.pickupDate,
            _addOrderState.value.pickupHour,
            _addOrderState.value.pickupMinute,
            _addOrderState.value.telp,
            _addOrderState.value.price,
            _addOrderState.value.downPayment,
            _addOrderState.value.progresses
        )

        if (selectedPicturesValidationResult != null || validationResult.isNotEmpty()) {
            _addOrderState.value = _addOrderState.value.copy(errors = validationResult)
            _selectedPictureState.value =
                _selectedPictureState.value.copy(error = selectedPicturesValidationResult)
            return
        }

        _addOrderState.value = _addOrderState.value.copy(isLoading = true)
        val pickupTime = addTimeToEpochDate(
            _addOrderState.value.pickupDate ?: 0,
            _addOrderState.value.pickupHour,
            _addOrderState.value.pickupMinute
        ).toString()
        viewModelScope.launch {
            delay(300)
            orderRepository.addOrder(
                context = context,
                pictureUris = _selectedPictureState.value.pictures,
                addOrderRequest = AddOrderRequest(
                    size = _addOrderState.value.size.toInt(),
                    layer = _addOrderState.value.layer.toIntOrNull(),
                    text = _addOrderState.value.text,
                    textColor = _addOrderState.value.textColor,
                    isUseTopper = _addOrderState.value.isUseTopper,
                    pickupTime = pickupTime,
                    telp = _addOrderState.value.telp,
                    price = _addOrderState.value.price.toDouble(),
                    downPayment = _addOrderState.value.downPayment.toDouble(),
                    notes = _addOrderState.value.notes,
                    progresses = _addOrderState.value.progresses
                )
            ).collect { result ->
                _addOrderState.value = when (result) {
                    is Resource.Success -> _addOrderState.value.copy(
                        isSuccess = true,
                        statusCode = result.code,
                        isLoading = false
                    )

                    is Resource.Error -> _addOrderState.value.copy(
                        isLoading = false,
                        errorMessage = result.msg,
                        statusCode = result.code,
                        errors = ApiUtils.extractApiFieldErrors(result.errors)
                    )

                    else -> _addOrderState.value
                }
            }
        }
    }

    fun addPictures(newUris: List<Uri>) {
        val updatedPictures = _selectedPictureState.value.pictures.toMutableList().apply {
            addAll(newUris.filterNot { it in this })
        }
        _selectedPictureState.value = _selectedPictureState.value.copy(pictures = updatedPictures)
    }

    fun reorderPictures(from: Int, to: Int) {
        val updatedPictures = _selectedPictureState.value.pictures.toMutableList().apply {
            add(to, removeAt(from))
        }
        _selectedPictureState.value = _selectedPictureState.value.copy(pictures = updatedPictures)
    }

    fun removePicture(uri: Uri) {
        _selectedPictureState.value =
            _selectedPictureState.value.copy(pictures = _selectedPictureState.value.pictures.filter { it != uri })
    }
}