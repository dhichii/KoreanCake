package com.dliemstore.koreancake.ui.screens.process

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dliemstore.koreancake.ui.components.BottomAppBar
import com.dliemstore.koreancake.ui.components.DeleteDialog
import com.dliemstore.koreancake.ui.navigation.graphs.ProcessNavigationItem
import com.dliemstore.koreancake.ui.navigation.graphs.ScaffoldViewState
import com.dliemstore.koreancake.ui.navigation.graphs.TopAppBarItem
import com.dliemstore.koreancake.ui.navigation.graphs.TopAppBarNavigationIcon
import com.dliemstore.koreancake.util.ProcessData
import com.dliemstore.koreancake.util.getAllProcesses
import sh.calvin.reorderable.ReorderableCollectionItemScope
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun Process(
    navController: NavController,
    scaffoldViewState: MutableState<ScaffoldViewState>
) {
    var data by remember { mutableStateOf(getAllProcesses()) }
    val lazyListState = rememberLazyListState()
    var isReorderEnabled by remember { mutableStateOf(false) }
    val reorderableLazyColumnState = rememberReorderableLazyListState(lazyListState) { from, to ->
        data = data.toMutableList().apply {
            add(to.index, removeAt(from.index))
            forEachIndexed { index, item ->
                set(index, item.copy(step = index + 1))
            }
        }
    }

    var isShowDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(isReorderEnabled) {
        scaffoldViewState.value = ScaffoldViewState(
            topAppBar = if (!isReorderEnabled) TopAppBarItem(
                title = { Text("Proses Pembuatan") },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(ProcessNavigationItem.Add.route)
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "Add"
                        )
                    }
                    IconButton(onClick = { isReorderEnabled = true }) {
                        Icon(
                            imageVector = Icons.Rounded.Reorder,
                            contentDescription = "Reorder"
                        )
                    }
                }
            ) else TopAppBarItem(
                title = { Text("Urutkan") },
                navigationIcon = TopAppBarNavigationIcon.Custom(
                    icon = Icons.Rounded.Close,
                    contentDescription = "Close",
                    onClick = {
                        isReorderEnabled = false
                    })
            ),
            bottomAppBar =
            if (!isReorderEnabled) {
                BottomAppBar.Navigation
            } else {
                BottomAppBar.Save(onClick = {
                    isReorderEnabled = false
                })

            }
        )
    }

    LazyColumn(
        state = lazyListState,
        modifier = Modifier.fillMaxSize()
    ) {
        items(data, key = { it.id }) { item ->
            ReorderableItem(reorderableLazyColumnState, key = item.id) {
                ProcessListItem(
                    item = item,
                    isReorderEnabled = isReorderEnabled,
                    onEdit = { navController.navigate("${ProcessNavigationItem.Edit.route}/${item.id}") },
                    onDelete = {
                        isShowDeleteDialog = true
                    },
                    reorderScope = this
                )
            }
        }
    }

    if (isShowDeleteDialog) {
        DeleteDialog(
            isLoading = false,
            onDismiss = { isShowDeleteDialog = false },
            onConfirmation = {
                isShowDeleteDialog = false
            }
        )
    }
}

@Composable
fun ProcessListItem(
    item: ProcessData,
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
            Row(modifier = Modifier.padding(12.dp, 8.dp)) {
                Text("${item.step}.")
                Spacer(Modifier.width(8.dp))
                Text(item.name)
            }

            Row {
                if (!isReorderEnabled) {
                    IconButton(onClick = onEdit) {
                        Icon(imageVector = Icons.Rounded.Edit, contentDescription = "Edit")
                    }
                    Spacer(Modifier.width(4.dp))
                    IconButton(onClick = onDelete) {
                        Icon(imageVector = Icons.Rounded.Delete, contentDescription = "Delete")
                    }
                } else {
                    IconButton(
                        onClick = {},
                        modifier = with(reorderScope) { Modifier.draggableHandle() }) {
                        Icon(imageVector = Icons.Rounded.Reorder, contentDescription = "Reorder")
                    }
                }
            }
        }
    }
}
