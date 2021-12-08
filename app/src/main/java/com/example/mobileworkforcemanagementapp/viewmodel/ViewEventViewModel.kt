package com.example.mobileworkforcemanagementapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewEventViewModel: ViewModel() {
    private var closeSoftKeyBoardEventLiveData: MutableLiveData<Unit> = MutableLiveData()

    fun emitCloseSoftKeyboardEvent() {
        closeSoftKeyBoardEventLiveData.postValue(Unit)
    }

    fun getCloseSoftKeyboardEvents(): LiveData<Unit> {
        return closeSoftKeyBoardEventLiveData
    }
}