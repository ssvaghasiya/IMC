package com.example.myapplication.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.pojo.MusicData
import com.example.myapplication.retrofit.APIInterface
import com.example.myapplication.utils.NetworkResult
import com.example.myapplication.utils.SafeApiCall
import retrofit2.Response
import javax.inject.Inject

class ContactRepository @Inject constructor(private val APIInterface: APIInterface) {

    private val _contacts = MutableLiveData<MusicData?>()
    val contacts: LiveData<MusicData?>
        get() = _contacts

    suspend fun getContacts() {
        val result: NetworkResult<Response<MusicData>> = SafeApiCall.safeApiCall {
            APIInterface.getContacts()
        }
        when (result) {
            is NetworkResult.Success -> {
                if (result.data.isSuccessful && result.data.body() != null) {
                    _contacts.postValue(result.data.body())
                }
            }
            is NetworkResult.Error -> { //Handle error
                Log.e("Error", result.message.toString())
                _contacts.postValue(null)
            }
        }

    }

}