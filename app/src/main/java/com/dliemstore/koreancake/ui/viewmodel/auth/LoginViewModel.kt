package com.dliemstore.koreancake.ui.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dliemstore.koreancake.data.source.repository.auth.AuthRepository
import com.dliemstore.koreancake.domain.validation.auth.LoginValidator
import com.dliemstore.koreancake.ui.state.auth.LoginState
import com.dliemstore.koreancake.util.ApiUtils
import com.dliemstore.koreancake.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {
    private val _loginState = MutableStateFlow(LoginState())
    val loginState = _loginState.asStateFlow()

    fun clearErrorMessage() {
        _loginState.update { it.copy(errorMessage = null) }
    }

    fun onInputChange(field: String, value: String) {
        val state = _loginState.value
        val newErrors = state.errors.toMutableMap()

        if (state.errorMessage != null) {
            clearErrorMessage()
        }

        val fieldError = LoginValidator.validate(
            username = if (field == "username") value else state.username,
            password = if (field == "password") value else state.password
        )[field]

        // set error if exist and remove if fixed
        if (fieldError != null) {
            newErrors[field] = fieldError
        } else {
            newErrors.remove(field)
        }

        _loginState.update {
            it.copy(
                username = if (field == "username") value else it.username,
                password = if (field == "password") value else it.password,
                errors = newErrors
            )
        }
    }

    fun login() {
        val validationResult = LoginValidator.validate(
            _loginState.value.username,
            _loginState.value.password
        )
        if (validationResult.isNotEmpty()) {
            _loginState.value = _loginState.value.copy(errors = validationResult)
            return
        }

        viewModelScope.launch {
            authRepository.login(_loginState.value.username, _loginState.value.password)
                .collect { result ->
                    _loginState.value = when (result) {
                        is Resource.Loading -> _loginState.value.copy(isLoading = true)
                        is Resource.Success -> _loginState.value.copy(
                            isSuccess = true,
                            statusCode = null,
                            isLoading = false
                        )

                        is Resource.Error -> _loginState.value.copy(
                            isLoading = false,
                            errorMessage = result.msg,
                            statusCode = result.code,
                            errors = ApiUtils.extractApiFieldErrors(result.errors)
                        )
                    }
                }
        }
    }
}