package com.dliemstore.koreancake.cake

import StatusTag
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dliemstore.koreancake.R
import com.dliemstore.koreancake.ui.components.MultipleCheckBox
import com.dliemstore.koreancake.ui.theme.KoreanCakeTheme
import com.dliemstore.koreancake.util.getCakeData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailCake(navController: NavController, id: String) {
    val data = getCakeData(id)
    KoreanCakeTheme {
        Surface {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Detail") },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = {}) {
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = "Edit"
                                )
                            }
                            IconButton(onClick = {}) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Delete"
                                )
                            }
                        }
                    )
                }
            ) { contentPadding ->
                val context = LocalContext.current
                val sectionModifier = Modifier.padding(12.dp, 0.dp)
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                        .verticalScroll(rememberScrollState())
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = sectionModifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(
                                id = data.picture,
                            ),
                            contentDescription = "Cake",
                            modifier = Modifier
                                .size(200.dp)
                                .padding(0.dp, 8.dp)
                        )

                        val pickupTime = data.pickupTime.transformPickupTime()
                        Text(
                            text = pickupTime.date,
                            color = colorResource(R.color.black_500),
                            fontSize = 12.sp
                        )
                        if (pickupTime.remainingDay > 0) {
                            Text(
                                text = "(${pickupTime.remainingDay} hari lagi)",
                                color = colorResource(R.color.black_500),
                                fontSize = 12.sp,
                            )
                        }
                        Text(
                            text = "Jam ${pickupTime.time}",
                            color = colorResource(R.color.black_500),
                            fontSize = 12.sp
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable(onClick = {
                                val url = "https://api.whatsapp.com/send?phone=" + data.telp
                                context.startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(url)
                                    )
                                )
                            })
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Call, contentDescription = "telp",
                                tint = colorResource(R.color.black_700),
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(end = 2.dp)
                            )
                            Text(
                                text = "+${data.telp}",
                                color = colorResource(R.color.black_700),
                                fontSize = 12.sp,
                            )

                        }
                    }

                    Column(modifier = sectionModifier) {
                        Text(
                            text = "Informasi Detail",
                            color = colorResource(R.color.black_700),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp)
                        )
                        Row {
                            Column(modifier = Modifier.padding(end = 8.dp)) {
                                Text(
                                    text = "Status",
                                    color = colorResource(R.color.black_500),
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "Ukuran",
                                    color = colorResource(R.color.black_500),
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "Tulisan",
                                    color = colorResource(R.color.black_500),
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "Warna Tulisan",
                                    color = colorResource(R.color.black_500),
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "Harga",
                                    color = colorResource(R.color.black_500),
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "Uang Muka",
                                    color = colorResource(R.color.black_500),
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "Sisa",
                                    color = colorResource(R.color.black_500),
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "Catatan",
                                    color = colorResource(R.color.black_500),
                                    fontSize = 12.sp
                                )
                            }
                            Column {
                                StatusTag(data.status)

                                var size = "${data.size}cm"
                                if (data.layer != null) size += " ${data.layer} layer"
                                Text(
                                    text = size,
                                    color = colorResource(R.color.black_700),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = data.text,
                                    color = colorResource(R.color.black_700),
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = data.textColor,
                                    color = colorResource(R.color.black_700),
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = data.price,
                                    color = colorResource(R.color.black_700),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = data.downPayment,
                                    color = colorResource(R.color.black_700),
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = data.remainingPayment,
                                    color = colorResource(R.color.black_700),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = data.notes ?: "-",
                                    color = colorResource(R.color.black_700),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    Column(modifier = sectionModifier) {
                        Text(
                            text = "Progres",
                            color = colorResource(R.color.black_700),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth()
                        )
                        MultipleCheckBox(data.progress)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailCakePreview() {
    KoreanCakeTheme {
        val navController = rememberNavController()
        DetailCake(navController, "1")
    }
}
