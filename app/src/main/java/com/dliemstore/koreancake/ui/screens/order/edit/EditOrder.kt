package com.dliemstore.koreancake.ui.screens.order.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dliemstore.koreancake.ui.components.BottomAppBar
import com.dliemstore.koreancake.ui.components.LoadingDialog
import com.dliemstore.koreancake.ui.navigation.graphs.ScaffoldViewState
import com.dliemstore.koreancake.ui.navigation.graphs.TopAppBarItem
import com.dliemstore.koreancake.ui.navigation.graphs.TopAppBarNavigationIcon
import com.dliemstore.koreancake.ui.screens.order.OrderForm
import com.dliemstore.koreancake.ui.screens.order.UploadOrderPictures
import com.dliemstore.koreancake.ui.viewmodel.order.EditOrderViewModel
import com.dliemstore.koreancake.util.ToastUtils

@Composable
fun EditOrder(
    navController: NavController,
    scaffoldViewState: MutableState<ScaffoldViewState>,
    viewModel: EditOrderViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val editOrderState by viewModel.editOrderState.collectAsState()
    val selectedPicturesState by viewModel.selectedPictureState.collectAsState()
    val progressesState by viewModel.progressesState.collectAsState()

    val isUpdating = editOrderState.isLoading

    LaunchedEffect(Unit) {
        scaffoldViewState.value = ScaffoldViewState(
            topAppBar = TopAppBarItem(
                title = { Text("Edit") },
                navigationIcon = TopAppBarNavigationIcon.CLOSE
            ),
            bottomAppBar = BottomAppBar.Save { viewModel.editOrder(context) }
        )
        viewModel.fetchProgresses()
    }

    LaunchedEffect(editOrderState) {
        when {
            editOrderState.isSuccess -> {
                ToastUtils.show(context, "Order berhasil diupdate!")
                navController.popBackStack()
            }

            editOrderState.errorMessage != null -> {
                ToastUtils.show(context, editOrderState.errorMessage ?: "Gagal mengubah order")
                viewModel.clearErrorMessage()
            }
        }
    }

    LaunchedEffect(progressesState) {
        if (progressesState.msg != null) {
            ToastUtils.show(context, editOrderState.errorMessage ?: "Gagal mengambil data")
        }
    }

    if (isUpdating) LoadingDialog()

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        UploadOrderPictures(
            pictures = selectedPicturesState.pictures,
            pictureError = selectedPicturesState.error,
            onAdd = viewModel::addPictures,
            onReorder = viewModel::reorderPictures,
            onDelete = viewModel::removePicture,
        )
        OrderForm(
            orderFormState = editOrderState,
            progressesState = progressesState,
            onProgressRetry = viewModel::fetchProgresses,
            onInputChange = viewModel::onInputChange,
            navController = navController
        )
    }
}
