package com.example.myapplication.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.ConnectException

object SafeApiCall {
    suspend fun <T> safeApiCall(block: suspend () -> T): NetworkResult<T> {
        return runCatching {
            withContext(Dispatchers.IO) {
                block()
            }
        }.fold({
            NetworkResult.Success(it)
        }, {
            when (it) {
                is HttpException, is ConnectException -> NetworkResult.Error(it, "Network error")
                else -> NetworkResult.Error(it, it.message)
                // else -> throw it
            }
        })
    }
}