package com.dliemstore.koreancake.ui.viewmodel.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dliemstore.koreancake.data.source.repository.user.UserRepository
import com.dliemstore.koreancake.domain.validation.settings.ChangePasswordValidator
import com.dliemstore.koreancake.ui.state.settings.ChangePasswordState
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
class ChangePasswordViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    private val _changePasswordState = MutableStateFlow(ChangePasswordState())
    val changePasswordState = _changePasswordState.asStateFlow()

    fun clearErrorMessage() {
        _changePasswordState.update { it.copy(errorMessage = null) }
    }

    fun onInputChange(field: String, value: String) {
        val state = _changePasswordState.value
        val newErrors = state.errors.toMutableMap()

        if (state.errorMessage != null) {
            clearErrorMessage()
        }

        val fieldError = ChangePasswordValidator.validate(
            oldPassword = if (field == "oldPassword") value else state.oldPassword,
            newPassword = if (field == "newPassword") value else state.newPassword,
            confirmPassword = if (field == "confirmPassword") value else state.confirmPassword
        )[field]

        // set error if exist and remove if fixed
        if (fieldError != null) {
            newErrors[field] = fieldError
        } else {
            newErrors.remove(field)
        }

        _changePasswordState.update {
            it.copy(
                oldPassword = if (field == "oldPassword") value else it.oldPassword,
                newPassword = if (field == "newPassword") value else it.newPassword,
                confirmPassword = if (field == "confirmPassword") value else it.confirmPassword,
                errors = newErrors
            )
        }
    }

    fun changePassword() {
        val validationResult = ChangePasswordValidator.validate(
            _changePasswordState.value.oldPassword,
            _changePasswordState.value.newPassword,
            _changePasswordState.value.confirmPassword
        )
        if (validationResult.isNotEmpty()) {
            _changePasswordState.value = _changePasswordState.value.copy(errors = validationResult)
            return
        }

        _changePasswordState.value = _changePasswordState.value.copy(isLoading = true)
        viewModelScope.launch {
            delay(300)
            userRepository.updatePassword(
                oldPassword = _changePasswordState.value.oldPassword,
                newPassword = _changePasswordState.value.newPassword
            ).collect { result ->
                _changePasswordState.value = when (result) {
                    is Resource.Success -> _changePasswordState.value.copy(
                        isSuccess = true,
                        statusCode = result.code,
                        isLoading = false
                    )

                    is Resource.Error -> _changePasswordState.value.copy(
                        isLoading = false,
                        errorMessage = result.msg,
                        statusCode = result.code,
                        errors = ApiUtils.extractApiFieldErrors(result.errors)
                    )

                    else -> _changePasswordState.value
                }
            }
        }
    }
}