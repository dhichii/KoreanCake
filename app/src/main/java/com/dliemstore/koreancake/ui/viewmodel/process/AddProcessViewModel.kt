package com.dliemstore.koreancake.ui.viewmodel.process

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dliemstore.koreancake.data.source.repository.process.ProcessRepository
import com.dliemstore.koreancake.domain.validation.process.AddProcessValidator
import com.dliemstore.koreancake.ui.state.process.AddProcessState
import com.dliemstore.koreancake.util.ApiUtils
import com.dliemstore.koreancake.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProcessViewModel @Inject constructor(private val processRepository: ProcessRepository) :
    ViewModel() {
    private val _addProcessState = MutableStateFlow(AddProcessState())
    val addProcessState = _addProcessState.asStateFlow()

    fun clearErrorMessage() {
        _addProcessState.update { it.copy(errorMessage = null) }
    }

    fun onInputChange(field: String, value: String) {
        val state = _addProcessState.value
        val newErrors = state.errors.toMutableMap()

        if (state.errorMessage != null) {
            clearErrorMessage()
        }

        val fieldError = AddProcessValidator.validate(
            name = if (field == "name") value else state.name,
            step = if (field == "step") value else state.step
        )[field]

        // set error if exist and remove if fixed
        if (fieldError != null) {
            newErrors[field] = fieldError
        } else {
            newErrors.remove(field)
        }

        _addProcessState.update {
            it.copy(
                name = if (field == "name") value else it.name,
                step = if (field == "step") value else it.step,
                errors = newErrors
            )
        }
    }

    fun addProcess() {
        val validationResult = AddProcessValidator.validate(
            _addProcessState.value.name,
            _addProcessState.value.step
        )
        if (validationResult.isNotEmpty()) {
            _addProcessState.value = _addProcessState.value.copy(errors = validationResult)
            return
        }

        _addProcessState.value = _addProcessState.value.copy(isLoading = true)
        viewModelScope.launch {
            delay(300)
            processRepository.addProcess(
                name = _addProcessState.value.name,
                step = _addProcessState.value.step.toInt()
            ).collect { result ->
                _addProcessState.value = when (result) {
                    is Resource.Success -> _addProcessState.value.copy(
                        isSuccess = true,
                        statusCode = result.code,
                        isLoading = false
                    )

                    is Resource.Error -> _addProcessState.value.copy(
                        isLoading = false,
                        errorMessage = result.msg,
                        statusCode = result.code,
                        errors = ApiUtils.extractApiFieldErrors(result.errors)
                    )

                    else -> _addProcessState.value
                }
            }
        }
    }
}