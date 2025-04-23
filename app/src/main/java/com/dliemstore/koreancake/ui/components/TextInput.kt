package com.dliemstore.koreancake.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.dliemstore.koreancake.R
import com.dliemstore.koreancake.util.CurrencyVisualTransformation

@Composable
fun TextInput(
    value: String,
    onInputChanged: (String) -> Unit,
    label: String,
    placeholder: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Unspecified,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    errorMessage: String? = null,
    readOnly: Boolean = false,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    var labelFontSize by remember {
        mutableStateOf(14.sp)
    }

    Column(modifier = modifier) {
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
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            isError = errorMessage != null,
            readOnly = readOnly,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    labelFontSize = if (it.isFocused || value != "") TextUnit.Unspecified else 14.sp
                }
        )
        errorMessage?.let {
            ErrorText(text = it)
        }
    }
}

@Composable
fun TelpInput(
    value: String,
    onInputChanged: (String) -> Unit,
    imeAction: ImeAction = ImeAction.Unspecified,
    errorMessage: String? = null,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    var leadingIconState by remember {
        mutableStateOf(false)
    }

    Column(modifier) {
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
            imeAction = imeAction,
            errorMessage = errorMessage,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { leadingIconState = it.isFocused || value != "" }
        )
        Text(
            text = "Contoh: 8123456789",
            color = colorResource(R.color.black_500),
            fontSize = 12.sp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun PasswordInput(
    value: String,
    label: String = "Password",
    onInputChanged: (String) -> Unit,
    errorMessage: String? = null,
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
            val img =
                if (isPasswordVisible) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff
            val description = if (isPasswordVisible) "Hide password" else "Show password"
            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                Icon(imageVector = img, description)
            }
        },
        label = label,
        keyboardType = KeyboardType.Password,
        errorMessage = errorMessage,
        modifier = modifier
    )
}

@Composable
fun CurrencyInput(
    value: String,
    label: String,
    imeAction: ImeAction = ImeAction.Unspecified,
    modifier: Modifier = Modifier.fillMaxWidth(),
    errorMessage: String? = null,
    onInputChanged: (String) -> Unit
) {
    TextInput(
        value = value,
        onInputChanged = { onInputChanged(it.filter { char -> char.isDigit() }) },
        label = label,
        keyboardType = KeyboardType.Number,
        imeAction = imeAction,
        visualTransformation = CurrencyVisualTransformation(),
        errorMessage = errorMessage,
        modifier = modifier
    )
}
