package com.dliemstore.koreancake.ui.screens.settings

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dliemstore.koreancake.ui.components.LoadingDialog
import com.dliemstore.koreancake.ui.components.PrimaryButton
import com.dliemstore.koreancake.ui.components.TextInput
import com.dliemstore.koreancake.ui.navigation.graphs.ScaffoldViewState
import com.dliemstore.koreancake.ui.navigation.graphs.TopAppBarItem
import com.dliemstore.koreancake.ui.navigation.graphs.TopAppBarNavigationIcon
import com.dliemstore.koreancake.ui.viewmodel.settings.UpdateProfileViewModel

@Composable
fun UpdateProfile(
    navController: NavController,
    scaffoldViewState: MutableState<ScaffoldViewState>,
    viewModel: UpdateProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val updateProfileState by viewModel.updateProfileState.collectAsState()
    val isPrimaryButtonEnabled =
        updateProfileState.name.isNotBlank() &&
                updateProfileState.error == null
    val isShowLoadingDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        scaffoldViewState.value = ScaffoldViewState(
            TopAppBarItem(
                title = { Text("Profile") },
                navigationIcon = TopAppBarNavigationIcon.CLOSE
            )
        )
    }

    LaunchedEffect(updateProfileState) {
        when {
            updateProfileState.isSuccess -> {
                Toast.makeText(context, "Update berhasil!", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }

            updateProfileState.errorMessage != null -> {
                Toast.makeText(context, updateProfileState.errorMessage, Toast.LENGTH_SHORT).show()
                viewModel.clearErrorMessage()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        TextInput(
            value = updateProfileState.name,
            onInputChanged = { viewModel.onInputChange(it) },
            label = "Nama",
            errorMessage = updateProfileState.error
        )

        Spacer(modifier = Modifier.height(16.dp))
        PrimaryButton(
            text = "Simpan",
            enabled = isPrimaryButtonEnabled,
            isLoading = updateProfileState.isLoading,
            onClick = { viewModel.updateProfile() },
            modifier = Modifier.fillMaxWidth()
        )
    }

    if (isShowLoadingDialog.value) {
        LoadingDialog()
    }
}
