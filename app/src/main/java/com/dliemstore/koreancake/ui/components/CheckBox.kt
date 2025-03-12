package com.dliemstore.koreancake.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dliemstore.koreancake.util.Progress

@Composable
fun CustomCheckBox(
    isChecked: Boolean,
    label: String,
    onClicked: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .selectable(
                selected = isChecked,
                onClick = { onClicked(!isChecked) },
                role = Role.Checkbox
            )
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = null,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = label,
            fontSize = 14.sp
        )
    }
}

@Composable
fun MultipleCheckBox(data: List<Progress>) {
    var checkState by remember {
        mutableStateOf(listOf<Int>())
    }

    Column {
        data.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .selectable(
                        selected = checkState.contains(item.id),
                        onClick = {
                            checkState = if (checkState.contains(item.id)) {
                                checkState - item.id
                            } else {
                                checkState + item.id
                            }
                        },
                        role = Role.Checkbox
                    )
            ) {
                Checkbox(
                    checked = checkState.contains(item.id),
                    onCheckedChange = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = item.name,
                    fontSize = 14.sp
                )
            }
        }
    }
}