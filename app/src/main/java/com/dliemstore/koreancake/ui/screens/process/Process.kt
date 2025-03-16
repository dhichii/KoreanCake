package com.dliemstore.koreancake.ui.screens.process

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Reorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dliemstore.koreancake.R
import com.dliemstore.koreancake.data.source.remote.response.process.ProcessResponse
import com.dliemstore.koreancake.ui.components.BottomAppBar
import com.dliemstore.koreancake.ui.components.DeleteDialog
import com.dliemstore.koreancake.ui.components.EmptyState
import com.dliemstore.koreancake.ui.components.ErrorState
import com.dliemstore.koreancake.ui.components.LoadingDialog
import com.dliemstore.koreancake.ui.components.shimmerEffect
import com.dliemstore.koreancake.ui.navigation.graphs.ProcessNavigationItem
import com.dliemstore.koreancake.ui.navigation.graphs.ScaffoldViewState
import com.dliemstore.koreancake.ui.navigation.graphs.TopAppBarItem
import com.dliemstore.koreancake.ui.navigation.graphs.TopAppBarNavigationIcon
import com.dliemstore.koreancake.ui.viewmodel.process.ProcessViewModel
import com.dliemstore.koreancake.util.Resource
import com.dliemstore.koreancake.util.ToastUtils
import sh.calvin.reorderable.ReorderableCollectionItemScope
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Process(
    navController: NavController,
    scaffoldViewState: MutableState<ScaffoldViewState>,
    viewModel: ProcessViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lazyListState = rememberLazyListState()
    val pullToRefreshState = rememberPullToRefreshState()

    val processesState by viewModel.processesState.collectAsState()
    val deleteProcessState by viewModel.deleteProcessState.collectAsState()
    val reorderProcessState by viewModel.reorderProcessesState.collectAsState()

    val isRefreshing = remember { mutableStateOf(false) }
    val isShowDeleteDialog = remember { mutableStateOf(false) }
    val isDeleting = deleteProcessState is Resource.Loading
    val selectedProcessId = remember { mutableStateOf<String?>(null) }

    val isCanReorder = remember { derivedStateOf { (processesState.data?.size ?: 0) > 1 } }
    val isReorderEnabled = remember { mutableStateOf(false) }
    val isReordering = reorderProcessState is Resource.Loading

    LaunchedEffect(isReorderEnabled.value) {
        setupScaffold(
            scaffoldViewState = scaffoldViewState,
            isCanReorder = isCanReorder,
            isReorderEnabled = isReorderEnabled,
            onAdd = { navController.navigate(ProcessNavigationItem.Add.route) },
            onCancel = {
                isReorderEnabled.value = false
                viewModel.cancelReorderProcess()
            },
            onReorder = {
                viewModel.saveReorderedProcess()
            }
        )
    }

    LaunchedEffect(Unit) { viewModel.fetchProcesses() }

    HandleStates(
        context = context,
        processesState = processesState,
        deleteProcessState = deleteProcessState,
        reorderProcessState = reorderProcessState,
        isRefreshing = isRefreshing,
        isReorderEnabled = isReorderEnabled
    )

    PullToRefreshBox(
        modifier = Modifier.fillMaxSize(),
        isRefreshing = isRefreshing.value,
        state = pullToRefreshState,
        onRefresh = {
            isRefreshing.value = true
            viewModel.fetchProcesses()
        }) {
        when (processesState) {
            is Resource.Loading -> ProcessLoadingState()
            is Resource.Success -> processesState.data?.takeIf { it.isNotEmpty() }
                ?.let { processes ->
                    ProcessList(
                        processes = processes,
                        navController = navController,
                        lazyListState = lazyListState,
                        isReorderEnabled = isReorderEnabled.value,
                        selectedProcessId = selectedProcessId,
                        isShowDeleteDialog = isShowDeleteDialog,
                        onReordering = viewModel::reorderProcess
                    )
                }
                ?: EmptyState(
                    icon = R.drawable.assignment_add_rounded_24,
                    title = "Belum Ada Proses",
                    body = "Coba tambahkan beberapa proses!"
                )

            is Resource.Error -> ErrorState(processesState.msg) { viewModel.fetchProcesses() }
        }
    }

    if (isShowDeleteDialog.value) {
        DeleteDialog(
            onDismiss = { isShowDeleteDialog.value = false },
            onConfirmation = {
                isShowDeleteDialog.value = false
                selectedProcessId.value?.let { viewModel.deleteProcess(it) }
            }
        )
    }

    if (isDeleting || isReordering) LoadingDialog()
}

private fun setupScaffold(
    scaffoldViewState: MutableState<ScaffoldViewState>,
    isCanReorder: State<Boolean>,
    isReorderEnabled: MutableState<Boolean>,
    onAdd: () -> Unit,
    onCancel: () -> Unit,
    onReorder: () -> Unit
) {
    scaffoldViewState.value = ScaffoldViewState(
        topAppBar = if (!isReorderEnabled.value) TopAppBarItem(
            title = { Text("Proses Pembuatan") },
            actions = {
                IconButton(onClick = onAdd) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Add"
                    )
                }

                if (isCanReorder.value) {
                    IconButton(onClick = { isReorderEnabled.value = true }) {
                        Icon(
                            imageVector = Icons.Rounded.Reorder,
                            contentDescription = "Reorder"
                        )
                    }
                }
            }
        ) else TopAppBarItem(
            title = { Text("Urutkan") },
            navigationIcon = TopAppBarNavigationIcon.Custom(
                icon = Icons.Rounded.Close,
                contentDescription = "Close",
                onClick = onCancel
            )
        ),
        bottomAppBar =
        if (!isReorderEnabled.value) {
            BottomAppBar.Navigation
        } else {
            BottomAppBar.Save(onClick = onReorder)
        }
    )
}

@Composable
private fun HandleStates(
    context: Context,
    processesState: Resource<List<ProcessResponse>>,
    deleteProcessState: Resource<Unit>?,
    reorderProcessState: Resource<Unit>?,
    isRefreshing: MutableState<Boolean>,
    isReorderEnabled: MutableState<Boolean>,
) {
    LaunchedEffect(processesState) {
        isRefreshing.value = false
        if (processesState is Resource.Error) {
            ToastUtils.show(context, processesState.msg ?: "Gagal mengambil data")
        }
    }

    LaunchedEffect(deleteProcessState) {
        when (deleteProcessState) {
            is Resource.Success -> {
                ToastUtils.show(context, "Proses berhasil dihapus")
            }

            is Resource.Error -> {
                ToastUtils.show(context, deleteProcessState.msg ?: "Gagal menghapus proses")
            }

            else -> {}
        }
    }

    LaunchedEffect(reorderProcessState) {
        when (reorderProcessState) {
            is Resource.Success -> {
                isReorderEnabled.value = false
                ToastUtils.show(context, "Proses berhasil diurutkan")
            }

            is Resource.Error -> {
                ToastUtils.show(context, reorderProcessState.msg ?: "Gagal mengurutkan proses")
            }

            else -> {}
        }
    }
}

@Composable
fun ProcessLoadingState() {
    Column(Modifier.fillMaxSize()) {
        repeat(15) { ProcessShimmerItem() }
    }
}

@Composable
fun ProcessShimmerItem() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp, 4.dp)
    ) {
        Box(
            Modifier
                .padding(12.dp, 16.dp)
                .fillMaxWidth(0.6f)
                .height(16.dp)
                .shimmerEffect()
        )
    }
}

@Composable
fun ProcessList(
    processes: List<ProcessResponse>,
    navController: NavController,
    lazyListState: LazyListState,
    isReorderEnabled: Boolean,
    selectedProcessId: MutableState<String?>,
    isShowDeleteDialog: MutableState<Boolean>,
    onReordering: (Int, Int) -> Unit
) {
    val reorderableLazyColumnState = rememberReorderableLazyListState(lazyListState) { from, to ->
        onReordering(from.index, to.index)
    }

    LazyColumn(
        state = lazyListState,
        modifier = Modifier.fillMaxSize()
    ) {
        items(processes, key = { it.id }) { item ->
            ReorderableItem(reorderableLazyColumnState, key = item.id) {
                ProcessListItem(
                    item = item,
                    isReorderEnabled = isReorderEnabled,
                    onEdit = { navController.navigate("${ProcessNavigationItem.Edit.route}/${item.id}") },
                    onDelete = {
                        selectedProcessId.value = item.id
                        isShowDeleteDialog.value = true
                    },
                    reorderScope = this
                )
            }
        }
    }
}

@Composable
fun ProcessListItem(
    item: ProcessResponse,
    isReorderEnabled: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    reorderScope: ReorderableCollectionItemScope,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .padding(12.dp, 4.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .padding(12.dp, 8.dp)
            ) {
                Text("${item.step}.")
                Spacer(Modifier.width(8.dp))
                Text(item.name)
            }

            if (!isReorderEnabled) {
                ActionButtons(onEdit, onDelete)
            } else {
                DragHandle(reorderScope)
            }
        }
    }
}

@Composable
private fun ActionButtons(onEdit: () -> Unit, onDelete: () -> Unit) {
    Row {
        IconButton(onClick = onEdit) {
            Icon(imageVector = Icons.Rounded.Edit, contentDescription = "Edit")
        }
        IconButton(onClick = onDelete) {
            Icon(imageVector = Icons.Rounded.Delete, contentDescription = "Delete")
        }
    }
}

@Composable
private fun DragHandle(reorderScope: ReorderableCollectionItemScope) {
    IconButton(
        onClick = {},
        modifier = with(reorderScope) { Modifier.draggableHandle() }
    ) {
        Icon(imageVector = Icons.Rounded.Reorder, contentDescription = "Reorder")
    }
}
