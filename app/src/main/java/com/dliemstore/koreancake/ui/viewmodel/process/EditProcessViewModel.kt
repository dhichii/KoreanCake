package com.dliemstore.koreancake.ui.viewmodel.process

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dliemstore.koreancake.data.source.repository.process.ProcessRepository
import com.dliemstore.koreancake.domain.validation.process.EditProcessValidator
import com.dliemstore.koreancake.ui.state.process.EditProcessState
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
class EditProcessViewModel @Inject constructor(
    private val processRepository: ProcessRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val initialName = savedStateHandle.get<String>("name") ?: ""
    val initialStep = savedStateHandle.get<String>("step") ?: "0"

    private val _editProcessState = MutableStateFlow(EditProcessState(initialName, initialStep))
    val editProcessState = _editProcessState.asStateFlow()

    fun clearErrorMessage() {
        _editProcessState.update { it.copy(errorMessage = null) }
    }

    fun onInputChange(field: String, value: String) {
        val state = _editProcessState.value
        val newErrors = state.errors.toMutableMap()

        if (state.errorMessage != null) {
            clearErrorMessage()
        }

        val fieldError = EditProcessValidator.validate(
            name = if (field == "name") value else state.name,
            step = if (field == "step") value else state.step
        )[field]

        // set error if exist and remove if fixed
        if (fieldError != null) {
            newErrors[field] = fieldError
        } else {
            newErrors.remove(field)
        }

        _editProcessState.update {
            it.copy(
                name = if (field == "name") value else it.name,
                step = if (field == "step") value else it.step,
                errors = newErrors
            )
        }
    }

    fun editProcess(id: String) {
        val validationResult = EditProcessValidator.validate(
            _editProcessState.value.name,
            _editProcessState.value.step
        )
        if (validationResult.isNotEmpty()) {
            _editProcessState.value = _editProcessState.value.copy(errors = validationResult)
            return
        }

        _editProcessState.value = _editProcessState.value.copy(isLoading = true)
        viewModelScope.launch {
            delay(300)
            processRepository.updateProcess(
                id = id,
                name = _editProcessState.value.name,
                step = _editProcessState.value.step.toInt()
            ).collect { result ->
                _editProcessState.value = when (result) {
                    is Resource.Success -> _editProcessState.value.copy(
                        isSuccess = true,
                        statusCode = result.code,
                        isLoading = false
                    )

                    is Resource.Error -> _editProcessState.value.copy(
                        isLoading = false,
                        errorMessage = result.msg,
                        statusCode = result.code,
                        errors = ApiUtils.extractApiFieldErrors(result.errors)
                    )

                    else -> _editProcessState.value
                }
            }
        }
    }
}