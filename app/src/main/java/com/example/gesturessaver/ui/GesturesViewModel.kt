package com.example.gesturessaver.ui

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.gesturessaver.logic.model.GesturesResult
import com.example.gesturessaver.logic.torchmodel.PredictModel

class GesturesViewModel : ViewModel() {

    private val predictLiveData = MutableLiveData<String>()

    val gesturesLiveData: LiveData<String>
        get() = predictLiveData

    fun predict(gesturebitmap: Bitmap) {
        val maxScoreIndex = PredictModel.predict(gesturebitmap)
        predictLiveData.value = GesturesResult.gestures[maxScoreIndex]
    }
}