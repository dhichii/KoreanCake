package com.dliemstore.koreancake.ui.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dliemstore.koreancake.data.source.repository.auth.AuthRepository
import com.dliemstore.koreancake.domain.validation.RegisterValidator
import com.dliemstore.koreancake.ui.state.RegisterState
import com.dliemstore.koreancake.util.ApiUtils
import com.dliemstore.koreancake.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {
    private val _registerState = MutableStateFlow(RegisterState())
    val registerState = _registerState.asStateFlow()

    fun clearErrorMessage() {
        _registerState.update { it.copy(errorMessage = null) }
    }

    fun onInputChange(field: String, value: String) {
        val state = _registerState.value
        val newErrors = state.errors.toMutableMap()
        val fieldError = RegisterValidator.validate(
            name = if (field == "name") value else state.name,
            email = if (field == "email") value else state.email,
            username = if (field == "username") value else state.username,
            password = if (field == "password") value else state.password,
            confirmPassword = if (field == "confirmPassword") value else state.confirmPassword
        )[field]

        // set error if exist and remove if fixed
        if (fieldError != null) {
            newErrors[field] = fieldError
        } else {
            newErrors.remove(field)
        }

        _registerState.update {
            it.copy(
                name = if (field == "name") value else it.name,
                username = if (field == "username") value else it.username,
                email = if (field == "email") value else it.email,
                password = if (field == "password") value else it.password,
                confirmPassword = if (field == "confirmPassword") value else it.confirmPassword,
                errors = newErrors
            )
        }
    }

    fun register() {
        val validationResult = RegisterValidator.validate(
            _registerState.value.name,
            _registerState.value.email,
            _registerState.value.username,
            _registerState.value.password,
            _registerState.value.confirmPassword
        )
        if (validationResult.isNotEmpty()) {
            _registerState.value = _registerState.value.copy(errors = validationResult)
            return
        }

        viewModelScope.launch {
            authRepository.register(
                _registerState.value.name,
                _registerState.value.email,
                _registerState.value.username,
                _registerState.value.password
            ).stateIn(viewModelScope)
                .collect { result ->
                    _registerState.value = when (result) {
                        is Resource.Loading -> _registerState.value.copy(isLoading = true)
                        is Resource.Success -> _registerState.value.copy(
                            isLoading = false,
                            isSuccess = true
                        )

                        is Resource.Error -> _registerState.value.copy(
                            isLoading = false,
                            errorMessage = result.msg,
                            errors = ApiUtils.extractApiFieldErrors(result.errorResponse?.errors)
                        )
                    }
                }
        }
    }
}