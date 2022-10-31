package com.example.myapplication.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.pojo.MusicData
import com.example.myapplication.repository.ContactRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repository: ContactRepository,
) : ViewModel() {

    val contactsLiveData: LiveData<MusicData?>
        get() = repository.contacts

    fun callApi() {
        viewModelScope.launch {
            repository.getContacts()
        }
    }

}

