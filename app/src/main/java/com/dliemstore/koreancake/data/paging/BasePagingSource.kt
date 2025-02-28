package com.dliemstore.koreancake.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dliemstore.koreancake.data.source.remote.response.PaginationSuccessResponse
import com.dliemstore.koreancake.util.ApiUtils
import retrofit2.Response
import java.io.IOException

class BasePagingSource<T : Any>(private val apiCall: suspend (Int) -> Response<PaginationSuccessResponse<T>>) :
    PagingSource<Int, T>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page = params.key ?: 1
        return try {
            val response = apiCall(page)
            if (response.isSuccessful) {
                val data = response.body()?.data ?: emptyList()
                LoadResult.Page(
                    data = data,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (data.isEmpty() || response.body()?.totalPage == page) null else page + 1
                )
            } else {
                val errorResponse = runCatching { ApiUtils.parseError(response) }.getOrNull()
                val errorMessage = when (response.code()) {
                    500 -> "Terjadi kesalahan pada server."
                    else -> errorResponse?.message ?: "Gagal mengambil data. Silakan coba lagi."
                }

                LoadResult.Error(Exception(errorMessage))
            }
        } catch (e: IOException) {
            LoadResult.Error(Throwable("Tidak ada koneksi internet."))
        } catch (e: Exception) {
            LoadResult.Error(Throwable("Terjadi kesalahan tidak terduga."))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
                ?: state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
        }
    }
}