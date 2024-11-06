package com.dliemstore.koreancake.ui.screens.cake

import StatusTag
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dliemstore.koreancake.R
import com.dliemstore.koreancake.ui.NavigationItem
import com.dliemstore.koreancake.util.CakeData

@Composable
fun Cake(data: CakeData, onItemClick: (CakeData) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = Modifier
            .padding(bottom = 8.dp)
            .clickable {
                onItemClick(data)
            }
    ) {
        Row(modifier = Modifier.padding(12.dp, 8.dp)) {
            Image(
                painter = painterResource(
                    id = data.picture,
                ),
                contentDescription = "Cake",
                modifier = Modifier
                    .size(120.dp)
                    .padding(end = 8.dp)
            )
            Column {
                var head = "${data.size}cm"
                if (data.layer != null) head += " ${data.layer} layer"
                head += ", ${data.text}"
                Text(
                    text = head,
                    color = colorResource(R.color.black_700),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
                StatusTag(data.status)

                val pickupTime = data.pickupTime.transformPickupTime()
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = pickupTime.date,
                        color = colorResource(R.color.black_500),
                        fontSize = 12.sp
                    )
                    if (pickupTime.remainingDay > 0) {
                        Text(
                            text = "(${pickupTime.remainingDay} hari lagi)",
                            color = colorResource(R.color.black_500),
                            fontSize = 10.sp,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                }
                Text(
                    text = "Jam ${pickupTime.time}",
                    color = colorResource(R.color.black_500),
                    fontSize = 12.sp
                )
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = data.price,
                        color = colorResource(R.color.black_700),
                        fontSize = 12.sp,
                    )
                    Text(
                        text = "(sisa ${data.remainingPayment})",
                        color = colorResource(R.color.black_500),
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CakeList(navController: NavController, data: List<CakeData>) {
    LazyColumn(modifier = Modifier.padding(8.dp, 0.dp)) {
        items(data) { item ->
            Cake(item, onItemClick = {
                navController.navigate(NavigationItem.Cake.route + "/${item.id}")
            })
        }
    }
}