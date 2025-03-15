package com.dliemstore.koreancake.ui.viewmodel.process

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dliemstore.koreancake.data.source.remote.request.process.UpdateProcessesStepRequest
import com.dliemstore.koreancake.data.source.remote.response.process.ProcessResponse
import com.dliemstore.koreancake.data.source.repository.process.ProcessRepository
import com.dliemstore.koreancake.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProcessViewModel @Inject constructor(private val processRepository: ProcessRepository) :
    ViewModel() {
    private val _processesState =
        MutableStateFlow<Resource<List<ProcessResponse>>>(Resource.Loading())
    val processesState: StateFlow<Resource<List<ProcessResponse>>> = _processesState

    private var originalList: List<ProcessResponse>? = null

    private val _deleteProcessState = MutableStateFlow<Resource<Unit>>(Resource.Loading())
    val deleteProcessState: StateFlow<Resource<Unit>> = _deleteProcessState

    private val _reorderProcessesState = MutableStateFlow<Resource<Unit>>(Resource.Loading())
    val reorderProcessesState: StateFlow<Resource<Unit>> = _reorderProcessesState

    fun fetchProcesses() {
        viewModelScope.launch {
            delay(300)
            processRepository.getProcesses().collect { result ->
                if (result is Resource.Success) originalList = result.data
                _processesState.value = result
            }
        }
    }

    fun deleteProcess(id: String) {
        viewModelScope.launch {
            delay(300)
            processRepository.delete(id).collect { result ->
                _deleteProcessState.value = result
                if (result is Resource.Success) {
                    _processesState.value = Resource.Success(
                        _processesState.value.data.orEmpty().filter { it.id != id },
                        result.code
                    )
                }
            }
        }
    }

    fun reorderProcess(from: Int, to: Int) {
        val currentState = processesState.value
        if (currentState is Resource.Success && (currentState.data?.size ?: 0) > 1) {
            val updatedList = currentState.data?.toMutableList()?.apply {
                add(to, removeAt(from))
                forEachIndexed { index, item ->
                    set(index, item.copy(step = index + 1))
                }
            }
            _processesState.value = Resource.Success(updatedList, currentState.code)
        }
    }

    fun cancelReorderProcess() {
        val currentState = processesState.value
        if (currentState is Resource.Success) {
            _processesState.value = Resource.Success(originalList, currentState.code)
        }
    }

    fun saveReorderedProcess() {
        _processesState.value.data?.let { list ->
            viewModelScope.launch {
                delay(300)
                processRepository.updateProcessesStep(list.map {
                    UpdateProcessesStepRequest(it.id, it.step)
                }).collect { _reorderProcessesState.value = it }
            }
        }
    }
}