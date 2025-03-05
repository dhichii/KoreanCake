package com.dliemstore.koreancake.data.source.remote.response.order


data class OrderDetailResponse(
	val id: String,
	val price: Double,
	val downPayment: Double,
	val remainingPayment: Double,
	val status: String,
	val pickupTime: Long,
	val layer: Int?,
	val size: Int,
	val text: String,
	val textColor: String,
	val isUseTopper: Boolean,
	val telp: String,
	val notes: String?,
	val pictures: List<PicturesItem>,
	val progresses: List<ProgressesItem>
)

data class ProgressesItem(
	val id: String,
	val name: String,
	val step: Int,
	val isFinish: Boolean
)
