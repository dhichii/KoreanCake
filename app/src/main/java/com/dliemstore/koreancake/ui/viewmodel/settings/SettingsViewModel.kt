package com.dliemstore.koreancake.ui.viewmodel.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dliemstore.koreancake.data.source.repository.auth.AuthRepository
import com.dliemstore.koreancake.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {
    private val _logoutState = MutableStateFlow<Resource<Unit>?>(null)
    val logoutState: StateFlow<Resource<Unit>?> = _logoutState

    fun logout() {
        _logoutState.value = Resource.Loading()
        viewModelScope.launch {
            delay(300)
            authRepository.logout().collect { _logoutState.value = it }
        }
    }
}