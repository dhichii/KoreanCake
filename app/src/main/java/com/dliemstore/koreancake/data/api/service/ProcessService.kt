package com.dliemstore.koreancake.data.api.service

import com.dliemstore.koreancake.data.source.remote.request.process.UpdateProcessesStepRequest
import com.dliemstore.koreancake.data.source.remote.response.SuccessResponse
import com.dliemstore.koreancake.data.source.remote.response.process.ProcessResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface ProcessService {
    @GET("processes")
    suspend fun getAll(): Response<SuccessResponse<List<ProcessResponse>>>

    @DELETE("processes/{id}")
    suspend fun delete(@Path("id") id: String): Response<Unit>

    @PATCH("processes/steps")
    suspend fun updateProcessesStep(@Body request: List<UpdateProcessesStepRequest>): Response<Unit>
}