package com.dliemstore.koreancake.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun TextInput(
    value: String,
    onInputChanged: (String) -> Unit,
    label: String,
    placeholder: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    var labelFontSize by remember {
        mutableStateOf(14.sp)
    }

    OutlinedTextField(
        value = value,
        label = { Text(label, fontSize = labelFontSize) },
        placeholder = if (placeholder != null) {
            { Text(placeholder, fontSize = 14.sp) }
        } else {
            null
        },
        onValueChange = onInputChanged,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        modifier = modifier.onFocusChanged {
            labelFontSize = if (it.isFocused || value != "") TextUnit.Unspecified else 14.sp
        }
    )
}

@Composable
fun TelpInput(
    value: String,
    onInputChanged: (String) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    var leadingIconState by remember {
        mutableStateOf(false)
    }

    TextInput(
        value = if (value == "") value else value.substring(2),
        onInputChanged = {
            onInputChanged(if (it != "") "62$it" else "")
        },
        leadingIcon = if (leadingIconState) {
            { Text("+62") }
        } else null,
        label = "Telp",
        keyboardType = KeyboardType.Phone,
        modifier = modifier.onFocusChanged { leadingIconState = it.isFocused || value != "" }
    )
}

@Composable
fun PasswordInput(
    value: String,
    label: String = "Password",
    onInputChanged: (String) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    TextInput(
        value = value,
        onInputChanged = {
            onInputChanged(it)
        },
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val img = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            val description = if (isPasswordVisible) "Hide password" else "Show password"
            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                Icon(imageVector = img, description)
            }
        },
        label = label,
        keyboardType = KeyboardType.Password,
        modifier = modifier
    )
}
