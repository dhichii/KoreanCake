package com.dliemstore.koreancake.ui.screens.add

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
import com.dliemstore.koreancake.ui.viewmodel.order.AddOrderViewModel
import com.dliemstore.koreancake.util.ToastUtils

@Composable
fun Add(
    navController: NavController,
    scaffoldViewState: MutableState<ScaffoldViewState>,
    viewModel: AddOrderViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val addOrderState by viewModel.addOrderState.collectAsState()
    val selectedPicturesState by viewModel.selectedPictureState.collectAsState()
    val progressesState by viewModel.progressesState.collectAsState()

    val isCreating = addOrderState.isLoading

    LaunchedEffect(Unit) {
        scaffoldViewState.value = ScaffoldViewState(
            topAppBar = TopAppBarItem(
                title = { Text("Tambah Order") },
                navigationIcon = TopAppBarNavigationIcon.CLOSE
            ),
            bottomAppBar = BottomAppBar.Save { viewModel.addOrder(context) }
        )
        viewModel.fetchProgresses()
    }

    LaunchedEffect(addOrderState) {
        when {
            addOrderState.isSuccess -> {
                ToastUtils.show(context, "Order berhasil ditambahkan!")
                navController.popBackStack()
            }

            addOrderState.errorMessage != null -> {
                ToastUtils.show(context, addOrderState.errorMessage ?: "Gagal menambahkan order")
                viewModel.clearErrorMessage()
            }
        }
    }

    LaunchedEffect(progressesState) {
        if (progressesState.msg != null) {
            ToastUtils.show(context, addOrderState.errorMessage ?: "Gagal mengambil data")
        }
    }

    if (isCreating) LoadingDialog()

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
            orderFormState = addOrderState,
            progressesState = progressesState,
            onProgressRetry = viewModel::fetchProgresses,
            onInputChange = viewModel::onInputChange,
            navController = navController
        )
    }
}
