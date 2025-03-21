package com.dliemstore.koreancake.ui.viewmodel.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dliemstore.koreancake.data.source.repository.user.UserRepository
import com.dliemstore.koreancake.domain.validation.settings.SettingsFormValidator
import com.dliemstore.koreancake.ui.navigation.graphs.SettingType
import com.dliemstore.koreancake.ui.state.settings.SettingsFormState
import com.dliemstore.koreancake.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsFormViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {
    private val _settingsFormState = MutableStateFlow(SettingsFormState())
    val settingsFormState = _settingsFormState.asStateFlow()

    fun clearErrorMessage() {
        _settingsFormState.update { it.copy(errorMessage = null) }
    }

    fun onInputChange(settingType: SettingType, value: String) {
        val state = _settingsFormState.value

        if (state.errorMessage != null) {
            clearErrorMessage()
        }

        val newError = SettingsFormValidator.validate(settingType, value)

        _settingsFormState.update {
            it.copy(
                input = value,
                error = newError
            )
        }
    }

    fun updateSetting(settingType: SettingType) {
        viewModelScope.launch {
            _settingsFormState.value = _settingsFormState.value.copy(isLoading = true)
            delay(300)
            val response = when (settingType) {
                SettingType.Profile -> userRepository.updateProfile(_settingsFormState.value.input)
                SettingType.Email -> userRepository.updateEmail(_settingsFormState.value.input)
                SettingType.Username -> userRepository.updateUsername(_settingsFormState.value.input)
            }

            response.collect { result ->
                _settingsFormState.value = when (result) {
                    is Resource.Success -> _settingsFormState.value.copy(
                        isSuccess = true,
                        statusCode = result.code,
                        isLoading = false
                    )

                    is Resource.Error -> _settingsFormState.value.copy(
                        isLoading = false,
                        errorMessage = result.msg,
                        statusCode = result.code,
                        error = result.errors?.get(0)?.message
                    )

                    else -> _settingsFormState.value
                }
            }
        }
    }
}