package com.dliemstore.koreancake.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dliemstore.koreancake.ui.screens.order.OrderList
import com.dliemstore.koreancake.ui.theme.KoreanCakeTheme
import com.dliemstore.koreancake.util.getAllOrderData

@Composable
fun Home(navController: NavController) {
    val statusOptions = listOf("All", "Proses", "Selesai")
    var statusIndexState by remember {
        mutableIntStateOf(0)
    }

    val data = getAllOrderData(statusOptions[statusIndexState])
    KoreanCakeTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(12.dp)
                ) {
                    statusOptions.forEachIndexed { index, option ->
                        var borderColor = MaterialTheme.colorScheme.tertiary
                        var backgroundColor = MaterialTheme.colorScheme.background
                        var textColor = MaterialTheme.colorScheme.tertiary
                        if (statusIndexState == index) {
                            borderColor = MaterialTheme.colorScheme.primary
                            backgroundColor = MaterialTheme.colorScheme.primaryContainer
                            textColor = MaterialTheme.colorScheme.primary
                        }
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .border(
                                    1.dp,
                                    borderColor,
                                    MaterialTheme.shapes.medium
                                )
                                .clip(MaterialTheme.shapes.medium)
                                .background(backgroundColor)
                                .clickable(onClick = {
                                    statusIndexState = index
                                })
                        ) {
                            Text(
                                option,
                                fontSize = 14.sp,
                                color = textColor,
                                modifier = Modifier.padding(12.dp, 4.dp)
                            )
                        }
                    }
                }
                OrderList(navController, data)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val navController = rememberNavController()
    KoreanCakeTheme { Home(navController) }
}
