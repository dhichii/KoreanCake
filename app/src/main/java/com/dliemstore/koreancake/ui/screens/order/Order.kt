package com.dliemstore.koreancake.ui.screens.order

import StatusTag
import android.graphics.drawable.ColorDrawable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dliemstore.koreancake.R
import com.dliemstore.koreancake.data.source.remote.response.order.OrdersResponse
import com.dliemstore.koreancake.ui.components.PrimaryButton
import com.dliemstore.koreancake.ui.components.SecondaryButton
import com.dliemstore.koreancake.ui.components.shimmerEffect
import com.dliemstore.koreancake.ui.navigation.graphs.OrderNavigationItem
import com.dliemstore.koreancake.ui.viewmodel.order.OrdersViewModel
import com.dliemstore.koreancake.util.ToastUtils
import com.dliemstore.koreancake.util.formatCurrency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderList(
    navController: NavController,
    status: String,
    viewModel: OrdersViewModel = hiltViewModel()
) {
    val orders = remember(status) { viewModel.fetchOrders(status) }.collectAsLazyPagingItems()
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val isRefreshing = remember { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(orders.loadState.refresh) {
        when (val refreshState = orders.loadState.refresh) {
            is LoadState.Loading -> isRefreshing.value = orders.itemCount > 0
            is LoadState.NotLoading -> {
                isRefreshing.value = false
                if (orders.itemCount > 0) listState.scrollToItem(0)
            }

            is LoadState.Error -> {
                isRefreshing.value = false
                ToastUtils.show(context, refreshState.error.message ?: "Gagal mengambil data")
            }

            else -> {}
        }
    }

    LaunchedEffect(orders.loadState.append) {
        when (val appendState = orders.loadState.append) {
            is LoadState.Loading, is LoadState.Error -> {
                val lastIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                if (lastIndex >= orders.itemCount - 1) {
                    listState.animateScrollToItem(orders.itemCount - 1)
                }

                if (appendState is LoadState.Error) {
                    ToastUtils.show(context, appendState.error.message ?: "Gagal mengambil data")
                }
            }

            else -> {}
        }
    }

    PullToRefreshBox(
        modifier = Modifier.fillMaxSize(),
        isRefreshing = isRefreshing.value,
        state = pullToRefreshState,
        onRefresh = {
            isRefreshing.value = true
            orders.refresh()
        }) {
        when (orders.loadState.refresh) {
            is LoadState.Loading -> if (orders.itemCount == 0) OrdersLoadingState()
            is LoadState.NotLoading -> {
                if (orders.itemCount == 0) OrdersEmptyState()
                else OrderListContent(orders, navController, listState)
            }

            is LoadState.Error -> OrdersErrorState { orders.retry() }
        }
    }
}

@Composable
fun OrdersLoadingState() {
    Column(modifier = Modifier.fillMaxSize()) {
        repeat(6) {
            OrderShimmerItem()
        }
    }
}

@Composable
fun OrderShimmerItem() {
    Row(modifier = Modifier.padding(12.dp, 8.dp)) {
        Box(
            Modifier
                .size(120.dp)
                .padding(end = 8.dp)
                .shimmerEffect()
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(1f, 0.2f, 0.5f, 0.4f, 0.5f).forEach { widthFactor ->
                Box(
                    Modifier
                        .fillMaxWidth(widthFactor)
                        .height(15.dp)
                        .shimmerEffect()
                )
            }
        }
    }
}

@Composable
fun OrdersEmptyState() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Icon(
            painter = painterResource(R.drawable.orders_filled_24),
            contentDescription = "Empty State",
            tint = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.size(120.dp)
        )
        Text(text = "Belum Ada Pesanan", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Coba tambahkan beberapa pesanan!", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun OrdersErrorState(onRetry: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Gagal memuat lebih banyak",
            fontSize = 14.sp,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(12.dp))
        SecondaryButton("Refresh", onClick = onRetry)
    }
}

@Composable
fun OrderListContent(
    orders: LazyPagingItems<OrdersResponse>,
    navController: NavController,
    listState: LazyListState
) {
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp, 0.dp)
    ) {
        items(orders.itemCount) { index ->
            orders[index]?.let {
                Order(
                    it,
                    onItemClick = { navController.navigate("${OrderNavigationItem.Detail.route}/${it.id}") })
            }
        }

        orders.apply {
            when (loadState.append) {
                is LoadState.Loading -> item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is LoadState.Error -> item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Gagal memuat lebih banyak",
                            fontSize = 14.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(12.dp))
                        PrimaryButton("Refresh", onClick = { orders.retry() })
                    }
                }

                else -> {}
            }
        }
    }
}

@Composable
fun Order(data: OrdersResponse, onItemClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .padding(bottom = 8.dp)
            .clickable {
                onItemClick()
            }
    ) {
        Row(modifier = Modifier.padding(12.dp, 8.dp)) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(data.pictures[0].url)
                    .placeholder(
                        ColorDrawable(
                            MaterialTheme.colorScheme.surfaceVariant.copy(
                                alpha = 0.6f
                            ).toArgb()
                        )
                    )
                    .crossfade(true)
                    .build(),
                contentDescription = "Order Picture",
                modifier = Modifier
                    .size(120.dp)
                    .padding(end = 8.dp)
                    .clip(MaterialTheme.shapes.medium)
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
                        text = data.price.formatCurrency(),
                        color = colorResource(R.color.black_700),
                        fontSize = 12.sp,
                    )
                    Text(
                        text = "(sisa ${data.remainingPayment.formatCurrency()})",
                        color = colorResource(R.color.black_500),
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}