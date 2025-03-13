package com.dliemstore.koreancake.ui.screens.order

import StatusTag
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.dliemstore.koreancake.R
import com.dliemstore.koreancake.data.source.remote.response.order.OrderDetailResponse
import com.dliemstore.koreancake.data.source.remote.response.order.PicturesItem
import com.dliemstore.koreancake.data.source.remote.response.order.ProgressesItem
import com.dliemstore.koreancake.ui.components.CustomCheckBox
import com.dliemstore.koreancake.ui.components.ErrorState
import com.dliemstore.koreancake.ui.components.shimmerEffect
import com.dliemstore.koreancake.ui.navigation.graphs.OrderNavigationItem
import com.dliemstore.koreancake.ui.navigation.graphs.ScaffoldViewState
import com.dliemstore.koreancake.ui.navigation.graphs.TopAppBarItem
import com.dliemstore.koreancake.ui.navigation.graphs.TopAppBarNavigationIcon
import com.dliemstore.koreancake.ui.viewmodel.order.OrderDetailViewModel
import com.dliemstore.koreancake.util.Resource
import com.dliemstore.koreancake.util.ToastUtils
import com.dliemstore.koreancake.util.formatCurrency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetail(
    id: String,
    navController: NavController,
    scaffoldViewState: MutableState<ScaffoldViewState>,
    viewModel: OrderDetailViewModel = hiltViewModel()
) {
    val orderState by viewModel.orderDetailState.collectAsState()
    val progressState by viewModel.updateProgressState.collectAsState()
    val context = LocalContext.current
    val isRefreshing = remember { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(Unit) {
        setupScaffold(
            scaffoldViewState = scaffoldViewState,
            onEdit = { navController.navigate("${OrderNavigationItem.Edit.route}/$id") },
            onDelete = {}
        )
        viewModel.fetchOrderDetail(id)
    }

    LaunchedEffect(orderState) {
        isRefreshing.value = false
        if (orderState is Resource.Error) {
            ToastUtils.show(context, orderState.msg ?: "Gagal mengambil data")
        }

    }

    LaunchedEffect(progressState) {
        if (progressState is Resource.Error) {
            ToastUtils.show(context, progressState.msg ?: "Gagal mengubah data")
        }
    }

    when (orderState) {
        is Resource.Loading -> OrderDetailShimmer()
        is Resource.Success -> {
            PullToRefreshBox(
                modifier = Modifier.fillMaxSize(),
                isRefreshing = isRefreshing.value,
                state = pullToRefreshState,
                onRefresh = {
                    isRefreshing.value = true
                    viewModel.fetchOrderDetail(id)
                }) {
                OrderDetailContent(id, orderState.data!!, context, viewModel)
            }
        }

        is Resource.Error -> ErrorState(orderState.msg) { viewModel.fetchOrderDetail(id) }
    }
}

private fun setupScaffold(
    scaffoldViewState: MutableState<ScaffoldViewState>,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    scaffoldViewState.value = ScaffoldViewState(
        topAppBar = TopAppBarItem(
            title = { Text("Detail") },
            navigationIcon = TopAppBarNavigationIcon.BACK,
            actions = {
                IconButton(onClick = onEdit) {
                    Icon(imageVector = Icons.Rounded.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = onDelete) {
                    Icon(imageVector = Icons.Rounded.Delete, contentDescription = "Delete")
                }
            }
        )
    )
}

@Composable
fun OrderDetailShimmer() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        // image placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .shimmerEffect()
                .clip(RectangleShape)
        )

        // detail info placeholder
        OrderDetailShimmerSection {
            repeat(10) { index ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth(if (index in setOf(0, 2, 5, 8, 9)) 0.6f else 0.8f)
                        .height(15.dp)
                        .shimmerEffect()
                )
            }
        }

        // contact placeholder
        OrderDetailShimmerSection {
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(20.dp)
                        .shimmerEffect()
                )
                Spacer(Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .shimmerEffect()
                )
            }
        }
    }
}

@Composable
fun OrderDetailShimmerSection(content: @Composable () -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(12.dp, 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .height(20.dp)
                .shimmerEffect()
        )
        content()
    }
}

@Composable
fun OrderDetailContent(
    id: String,
    data: OrderDetailResponse,
    context: Context,
    viewModel: OrderDetailViewModel
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        OrderDetailImagePage(data.pictures, context)
        OrderDetailInfo(data)
        OrderDetailContact(data.telp, context)
        OrderDetailProgresses(id, data.progresses, viewModel::updateProgress)
    }
}

@Composable
fun OrderDetailImagePage(pictures: List<PicturesItem>, context: Context) {
    val pagerState = rememberPagerState { pictures.size }

    Box(Modifier.fillMaxWidth()) {
        HorizontalPager(state = pagerState, beyondViewportPageCount = 3) { page ->
            val painter = rememberAsyncImagePainter(
                ImageRequest.Builder(context).data(pictures[page].url).crossfade(true).build()
            )
            val state by painter.state.collectAsState()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                when (state) {
                    is AsyncImagePainter.State.Error -> Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { painter.restart() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Tidak dapat memuat. Klik untuk coba lagi.",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(12.dp)
                        )
                    }

                    is AsyncImagePainter.State.Success -> Image(
                        painter = painter, contentDescription = "Order Picture $page",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    else -> {}
                }
            }
        }

        // page indicator
        Text(
            text = "${pagerState.currentPage + 1}/${pagerState.pageCount}",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 12.dp, end = 12.dp)
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.inverseSurface.copy(0.8f))
                .padding(8.dp, 2.dp)
        )
    }
}

@Composable
fun OrderDetailInfo(data: OrderDetailResponse) {
    OrderDetailContentSection(title = "Informasi Detail") {
        OrderDetailInfoRow("Status") { StatusTag(data.status) }

        val pickupTime = data.pickupTime.transformPickupTime()
        var pickupDate = pickupTime.date
        if (pickupTime.remainingDay > 0) pickupDate += " (${pickupTime.remainingDay} hari lagi)"
        OrderDetailInfoRow("Tanggal", pickupDate)
        OrderDetailInfoRow("Jam", pickupTime.time)

        val size = "${data.size}cm ${data.layer.let { "$it layer" }}"
        OrderDetailInfoRow("Ukuran", size)
        OrderDetailInfoRow("Tulisan", data.text)
        OrderDetailInfoRow("Warna Tulisan", data.textColor)
        OrderDetailInfoRow("Harga", data.price.formatCurrency())
        OrderDetailInfoRow("Uang Muka", data.downPayment.formatCurrency())
        OrderDetailInfoRow("Sisa", data.remainingPayment.formatCurrency())
        OrderDetailInfoRow("Catatan", data.notes ?: "-")
    }
}

@Composable
fun OrderDetailInfoRow(label: String, value: String) {
    Row {
        Text(
            text = label,
            color = colorResource(R.color.black_500),
            fontSize = 12.sp,
            modifier = Modifier.fillMaxWidth(0.3f)
        )
        Text(
            text = value,
            color = colorResource(R.color.black_700),
            fontSize = 12.sp,
            modifier = Modifier.fillMaxWidth(0.7f)
        )
    }
}

@Composable
fun OrderDetailInfoRow(label: String, content: @Composable () -> Unit) {
    Row {
        Text(
            text = label,
            color = colorResource(R.color.black_500),
            fontSize = 12.sp,
            modifier = Modifier.fillMaxWidth(0.3f)
        )
        content()
    }
}

@Composable
fun OrderDetailContact(contact: String, context: Context) {
    val clipboardManager = LocalClipboardManager.current

    OrderDetailContentSection(title = "Kontak", titleBottomPadding = 0.dp) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
        ) {
            Text(
                text = "+$contact",
                color = colorResource(R.color.black_700),
                fontSize = 14.sp
            )
            Row {
                IconButton(onClick = {
                    clipboardManager.setText(AnnotatedString("+$contact"))
                    ToastUtils.show(context, "Disalin ke clipboard")
                }) {
                    Icon(
                        imageVector = Icons.Rounded.ContentCopy,
                        contentDescription = "Copy",
                        tint = colorResource(R.color.black_500),
                    )
                }

                IconButton(onClick = {
                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://api.whatsapp.com/send?phone=$contact")
                        )
                    )
                }) {
                    Icon(
                        painter = painterResource(R.drawable.whatsapp),
                        contentDescription = "Whatsapp",
                        tint = Color(0xff25d366),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun OrderDetailProgresses(
    orderId: String,
    data: List<ProgressesItem>,
    onProgressChanged: (String, String, Boolean) -> Unit
) {
    OrderDetailContentSection(title = "Progres") {
        Column(Modifier.padding(bottom = 12.dp)) {
            data.forEach { progress ->
                CustomCheckBox(
                    isChecked = progress.isFinish,
                    label = progress.name,
                    onClicked = { onProgressChanged(orderId, progress.id, it) }
                )
            }
        }
    }
}

@Composable
fun OrderDetailContentSection(
    title: String,
    titleBottomPadding: Dp = 4.dp,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .padding(12.dp, 8.dp)
    ) {
        Text(
            text = title,
            color = colorResource(R.color.black_700),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = titleBottomPadding)
        )
        content()
    }
}
