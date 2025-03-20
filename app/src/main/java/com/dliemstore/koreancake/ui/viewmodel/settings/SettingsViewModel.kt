package com.dliemstore.koreancake.ui.viewmodel.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dliemstore.koreancake.data.source.remote.response.user.UserProfileResponse
import com.dliemstore.koreancake.data.source.repository.auth.AuthRepository
import com.dliemstore.koreancake.data.source.repository.user.UserRepository
import com.dliemstore.koreancake.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _userProfileState =
        MutableStateFlow<Resource<UserProfileResponse>>(Resource.Loading())
    val userProfileState: StateFlow<Resource<UserProfileResponse>> = _userProfileState

    private val _logoutState = MutableStateFlow<Resource<Unit>?>(null)
    val logoutState: StateFlow<Resource<Unit>?> = _logoutState

    fun fetchUserProfile() {
        viewModelScope.launch {
            delay(300)
            userRepository.getProfile().collect { _userProfileState.value = it }
        }
    }

    fun logout() {
        _logoutState.value = Resource.Loading()
        viewModelScope.launch {
            delay(300)
            authRepository.logout().collect { _logoutState.value = it }
        }
    }
}