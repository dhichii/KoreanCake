package com.dliemstore.koreancake.ui.viewmodel.order

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dliemstore.koreancake.data.source.remote.request.order.UpdateOrderRequest
import com.dliemstore.koreancake.data.source.remote.response.process.ProcessResponse
import com.dliemstore.koreancake.data.source.repository.order.OrderRepository
import com.dliemstore.koreancake.data.source.repository.process.ProcessRepository
import com.dliemstore.koreancake.domain.validation.order.OrderFormValidator
import com.dliemstore.koreancake.domain.validation.order.OrderPicturesValidator
import com.dliemstore.koreancake.ui.state.order.OrderFormState
import com.dliemstore.koreancake.ui.state.order.OrderPicturesState
import com.dliemstore.koreancake.util.ApiUtils
import com.dliemstore.koreancake.util.FileUtils
import com.dliemstore.koreancake.util.Resource
import com.dliemstore.koreancake.util.addTimeToEpochDate
import com.dliemstore.koreancake.util.epochToDate
import com.dliemstore.koreancake.util.formatTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditOrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val processRepository: ProcessRepository,
    private val fileUtils: FileUtils,
    savedStateHandle: SavedStateHandle
) :
    ViewModel() {
    val id = savedStateHandle.get<String>("id") ?: ""

    private val _editOrderState = MutableStateFlow(OrderFormState())
    val editOrderState = _editOrderState.asStateFlow()

    private var initialPictures: List<Picture> = emptyList()
    private val _selectedPictureState = MutableStateFlow(OrderPicturesState())
    val selectedPictureState = _selectedPictureState.asStateFlow()
    private var isPictureReordered: Boolean = false

    private var initialProgresses: List<String> = emptyList()
    private val _progressesState =
        MutableStateFlow<Resource<List<ProcessResponse>>>(Resource.Loading())
    val progressesState: StateFlow<Resource<List<ProcessResponse>>> = _progressesState

    init {
        fetchOrderData()
    }

    private fun fetchOrderData() {
        viewModelScope.launch {
            orderRepository.getOrderDetail(id).collect { result ->
                _editOrderState.value = when (result) {
                    is Resource.Success -> {
                        val data = result.data!!
                        val pickupTime = data.pickupTime.epochToDate().formatTime().split(":")
                        val hour = pickupTime[0]
                        val minute = pickupTime[1]

                        val progresses = data.progresses.map { it.id }
                        initialProgresses = progresses

                        val pictures =
                            data.pictures.map { Picture(it.id, it.url, Uri.parse(it.url)) }
                        initialPictures = pictures
                        _selectedPictureState.value = _selectedPictureState.value.copy(
                            pictures = pictures.map { it.uri }
                        )

                        _editOrderState.value.copy(
                            size = data.size.toString(),
                            layer = data.layer?.toString() ?: "",
                            text = data.text,
                            textColor = data.textColor,
                            pickupDate = data.pickupTime,
                            pickupHour = hour,
                            pickupMinute = minute,
                            telp = data.telp,
                            isUseTopper = data.isUseTopper,
                            price = data.price.toString(),
                            downPayment = data.downPayment.toString(),
                            progresses = progresses,
                            notes = data.notes ?: "",
                            isSuccess = false,
                            statusCode = result.code,
                            isLoading = false
                        )
                    }

                    is Resource.Error -> _editOrderState.value.copy(
                        isLoading = false,
                        errorMessage = result.msg,
                        statusCode = result.code,
                        errors = ApiUtils.extractApiFieldErrors(result.errors)
                    )

                    else -> _editOrderState.value
                }
            }
        }
    }

    fun fetchProgresses() {
        viewModelScope.launch {
            delay(300)
            processRepository.getProcesses().collect { _progressesState.value = it }
        }
    }

    fun clearErrorMessage() {
        _editOrderState.update { it.copy(errorMessage = null) }
    }

    fun onInputChange(field: String, value: Any?) {
        val state = _editOrderState.value
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

        _editOrderState.value = updatedState.copy(errors = newErrors)
    }

    fun editOrder(context: Context) {
        viewModelScope.launch {
            val selectedPicturesValidationResult =
                OrderPicturesValidator.validate(_selectedPictureState.value.pictures)
            val validationResult = OrderFormValidator.validate(
                _editOrderState.value.size,
                _editOrderState.value.layer,
                _editOrderState.value.text,
                _editOrderState.value.textColor,
                _editOrderState.value.pickupDate,
                _editOrderState.value.pickupHour,
                _editOrderState.value.pickupMinute,
                _editOrderState.value.telp,
                _editOrderState.value.price,
                _editOrderState.value.downPayment,
                _editOrderState.value.progresses
            )

            if (selectedPicturesValidationResult != null || validationResult.isNotEmpty()) {
                _editOrderState.value = _editOrderState.value.copy(errors = validationResult)
                _selectedPictureState.value =
                    _selectedPictureState.value.copy(error = selectedPicturesValidationResult)
                return@launch
            }

            _editOrderState.value = _editOrderState.value.copy(isLoading = true)

            val pickupTime = addTimeToEpochDate(
                _editOrderState.value.pickupDate ?: 0,
                _editOrderState.value.pickupHour,
                _editOrderState.value.pickupMinute
            ).toString()
            val progresses = _editOrderState.value.progresses
            val addedProgresses = progresses.filter { it !in initialProgresses }
            val deletedProgresses = initialProgresses.filter { it !in progresses }

            val deletedPictures = mapDeletedPictures(isPictureReordered)
            val addedPictures = mapAddedPictures(isPictureReordered)

            if (!isPictureReordered) delay(300)

            orderRepository.updateOrder(
                id = id,
                context = context,
                addedPictureUris = addedPictures,
                updateOrderRequest = UpdateOrderRequest(
                    size = _editOrderState.value.size.toInt(),
                    layer = _editOrderState.value.layer.toIntOrNull(),
                    text = _editOrderState.value.text,
                    textColor = _editOrderState.value.textColor,
                    isUseTopper = _editOrderState.value.isUseTopper,
                    pickupTime = pickupTime,
                    telp = _editOrderState.value.telp,
                    price = _editOrderState.value.price.toDouble(),
                    downPayment = _editOrderState.value.downPayment.toDouble(),
                    notes = _editOrderState.value.notes,
                    addedProgresses = addedProgresses,
                    deletedPictures = deletedPictures,
                    deletedProgresses = deletedProgresses
                )
            ).collect { result ->
                _editOrderState.value = when (result) {
                    is Resource.Success -> _editOrderState.value.copy(
                        isSuccess = true,
                        statusCode = result.code,
                        isLoading = false
                    )

                    is Resource.Error -> _editOrderState.value.copy(
                        isLoading = false,
                        errorMessage = result.msg,
                        statusCode = result.code,
                        errors = ApiUtils.extractApiFieldErrors(result.errors)
                    )

                    else -> _editOrderState.value
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
        isPictureReordered = true
        val updatedPictures = _selectedPictureState.value.pictures.toMutableList().apply {
            add(to, removeAt(from))
        }
        _selectedPictureState.value = _selectedPictureState.value.copy(pictures = updatedPictures)
    }

    fun removePicture(uri: Uri) {
        _selectedPictureState.value =
            _selectedPictureState.value.copy(pictures = _selectedPictureState.value.pictures.filter { it != uri })
    }

    private fun mapDeletedPictures(isReordered: Boolean): List<String> {
        val selectedPictures = _selectedPictureState.value
        return if (isReordered) {
            initialPictures.map { it.id }
        } else {
            initialPictures.filter { it.uri !in selectedPictures.pictures }.map { it.id }
        }
    }

    private suspend fun mapAddedPictures(isReordered: Boolean): List<Uri> {
        val initialUris = initialPictures.associateBy { it.uri }
        val selectedPictures = _selectedPictureState.value.pictures
        return if (isReordered) {
            selectedPictures.map { uri ->
                initialUris[uri]?.let {
                    fileUtils.downloadFileToUri(it.url)
                } ?: uri
            }
        } else {
            selectedPictures.filter { uri -> initialUris[uri] == null }
        }
    }
}

private data class Picture(val id: String, val url: String, val uri: Uri)
