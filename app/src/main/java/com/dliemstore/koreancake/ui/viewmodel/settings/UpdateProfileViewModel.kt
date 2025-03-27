package com.dliemstore.koreancake.ui.viewmodel.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dliemstore.koreancake.data.source.repository.user.UserRepository
import com.dliemstore.koreancake.domain.validation.settings.UpdateProfileValidator
import com.dliemstore.koreancake.ui.state.settings.UpdateProfileState
import com.dliemstore.koreancake.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateProfileViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    private val _updateProfileState = MutableStateFlow(UpdateProfileState())
    val updateProfileState = _updateProfileState.asStateFlow()

    fun clearErrorMessage() {
        _updateProfileState.update { it.copy(errorMessage = null) }
    }

    fun onInputChange(value: String) {
        val state = _updateProfileState.value

        if (state.errorMessage != null) {
            clearErrorMessage()
        }

        val newError = UpdateProfileValidator.validate(value)

        _updateProfileState.update {
            it.copy(
                name = value,
                error = newError
            )
        }
    }

    fun updateProfile() {
        val validationResult = UpdateProfileValidator.validate(_updateProfileState.value.name)
        if (validationResult != null) {
            _updateProfileState.value = _updateProfileState.value.copy(error = validationResult)
            return
        }

        viewModelScope.launch {
            _updateProfileState.value = _updateProfileState.value.copy(isLoading = true)
            delay(300)
            userRepository.updateProfile(_updateProfileState.value.name).collect { result ->
                _updateProfileState.value = when (result) {
                    is Resource.Success -> _updateProfileState.value.copy(
                        isSuccess = true,
                        statusCode = result.code,
                        isLoading = false
                    )

                    is Resource.Error -> _updateProfileState.value.copy(
                        isLoading = false,
                        errorMessage = result.msg,
                        statusCode = result.code,
                        error = result.errors?.get(0)?.message
                    )

                    else -> _updateProfileState.value
                }
            }
        }
    }
}