package com.ahmed.vodafonerepos.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmed.vodafonerepos.data.models.LoadingModel
import com.ahmed.vodafonerepos.data.models.ProgressTypes
import com.ahmed.vodafonerepos.data.models.Status
import com.ahmed.vodafonerepos.utils.alternate
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel(
    private var mSavedStateHandle: SavedStateHandle,
    private vararg var mUseCases: IBaseUseCase
) : ViewModel() {

    var loadingObservable = MutableSharedFlow<LoadingModel>()
    var errorViewObservable = MutableSharedFlow<Boolean>()
    var showToastObservable = MutableSharedFlow<String>()

    private var _loadingModel: LoadingModel? = null

    val loadingModel: LoadingModel?
        get() = _loadingModel

    protected fun showProgress(
        shouldShow: Boolean,
        progressType: ProgressTypes = ProgressTypes.MAIN_PROGRESS
    ) {
        viewModelScope.launch {
            _loadingModel = LoadingModel(shouldShow, progressType)
            loadingObservable.emit(_loadingModel!!)
        }
    }

    protected fun shouldShowError(shouldShow: Boolean) {
        viewModelScope.launch {
            errorViewObservable.emit(shouldShow)
        }
    }

    protected fun showToastMessage(message: Any?, vararg args: Any?) {
        viewModelScope.launch {

            if (message is String)
                showToastObservable.emit(message)
            else
                showToastObservable.emit(message.toString().alternate())
        }
    }

    private fun <T> getStateLiveData(key: String, initialValue: T? = null): MutableLiveData<T?>? {
        if (!mSavedStateHandle.contains(key)) return null

        val liveData = mSavedStateHandle.getLiveData(key, initialValue)
        mSavedStateHandle.remove<T>(key)
        return liveData
    }



    fun <T> showErrorTitle(status: Status<T>) {
        val errorTitle = status.error
        showToastMessage(errorTitle)
    }

}
