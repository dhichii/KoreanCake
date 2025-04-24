package com.dliemstore.koreancake.ui.screens.order

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.dliemstore.koreancake.ui.components.ErrorText
import com.dliemstore.koreancake.ui.components.SecondaryButton
import sh.calvin.reorderable.ReorderableCollectionItemScope
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun UploadOrderPictures(
    pictures: List<Uri>,
    pictureError: String? = null,
    onAdd: (List<Uri>) -> Unit,
    onReorder: (Int, Int) -> Unit,
    onDelete: (Uri) -> Unit
) {
    val remainingSelection = 3 - pictures.size
    val photosPickerLauncher = rememberLauncherForActivityResult(
        contract = if (remainingSelection > 1) ActivityResultContracts.PickMultipleVisualMedia(
            remainingSelection
        ) else ActivityResultContracts.PickVisualMedia(),
        onResult = {
            val newUris: List<Uri> = when (it) {
                is List<*> -> it.filterIsInstance<Uri>()
                is Uri -> listOf(it)
                else -> emptyList()
            }

            onAdd(newUris)
        }
    )

    Column {
        if (pictures.isEmpty()) {
            EmptyPicturesSelector(photosPickerLauncher)
        } else {
            UploadOrderPictureList(
                pictures = pictures,
                photosPickerLauncher = photosPickerLauncher,
                onReorder = onReorder,
                onDelete = onDelete
            )
        }
        pictureError?.let {
            ErrorText(
                text = it, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            )
        }
    }
}

@Composable
fun EmptyPicturesSelector(photosPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, out Any?>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(12.dp)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium
            )
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Pilih gambar untuk diupload",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(text = "Format: JPG, PNG", style = MaterialTheme.typography.bodySmall)
        SecondaryButton(text = "Pilih gambar", onClick = {
            photosPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        })
    }
}

@Composable
fun UploadOrderPictureList(
    pictures: List<Uri>,
    photosPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, out Any?>,
    onReorder: (Int, Int) -> Unit,
    onDelete: (Uri) -> Unit
) {
    val lazyListState = rememberLazyListState()
    val reorderState = rememberReorderableLazyListState(lazyListState) { from, to ->
        onReorder(from.index, to.index)
    }

    LazyRow(
        state = lazyListState,
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(
            pictures,
            key = { _, item -> item.toString() }) { index, item ->
            ReorderableItem(state = reorderState, key = item.toString()) {
                UploadOrderPictureItem(
                    index = index,
                    uri = item,
                    onDelete = onDelete,
                    reorderScope = this
                )
            }
        }

        if (pictures.size in 1..2) {
            item { AddOrderPictureButton(photosPickerLauncher) }
        }
    }
}

@Composable
fun UploadOrderPictureItem(
    index: Int,
    uri: Uri,
    onDelete: (Uri) -> Unit,
    reorderScope: ReorderableCollectionItemScope
) {
    Box(
        modifier = with(reorderScope) {
            Modifier
                .size(300.dp)
                .longPressDraggableHandle()
        }
    ) {
        AsyncImage(
            model = uri,
            contentDescription = "Order Picture $index",
            modifier = Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop
        )
        IconButton(
            onClick = { onDelete(uri) },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(32.dp)
                .padding(top = 8.dp, end = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Close,
                tint = MaterialTheme.colorScheme.surfaceContainerLowest,
                contentDescription = "remove picture",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(24.dp)
                    .background(
                        MaterialTheme.colorScheme.inverseSurface.copy(0.8f)
                    )
                    .padding(4.dp)
            )
        }
    }
}

@Composable
fun AddOrderPictureButton(photosPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, out Any?>) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.height(300.dp)
    ) {
        OutlinedButton(
            onClick = {
                photosPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            shape = MaterialTheme.shapes.medium,
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Add Picture",
                modifier = Modifier
                    .size(60.dp)
                    .padding(12.dp)
            )
        }
    }
}