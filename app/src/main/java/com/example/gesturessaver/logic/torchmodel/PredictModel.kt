package com.example.gesturessaver.logic.torchmodel

import android.graphics.Bitmap
import android.util.Log
import com.example.gesturessaver.GesturesSaverApplication
import com.example.gesturessaver.util.ImageProcess
import org.pytorch.IValue
import org.pytorch.MemoryFormat
import org.pytorch.Module
import org.pytorch.torchvision.TensorImageUtils

object PredictModel {

    private lateinit var module: Module

    init {
        initMoudle()
    }

    fun initMoudle() {
        try {
            module = Module.load(ImageProcess.assetFilePath(GesturesSaverApplication.context, "gesturesRec_best.pt"))
        } catch (e: Exception) {
            Log.e("Pytorch", "Failed load model", e)
        }

    }

    fun predict(gesturebitmap: Bitmap): Int {



        val t1 = System.currentTimeMillis()

        val inputTensor = TensorImageUtils.bitmapToFloat32Tensor(
            gesturebitmap, TensorImageUtils.TORCHVISION_NORM_MEAN_RGB,
            TensorImageUtils.TORCHVISION_NORM_STD_RGB, MemoryFormat.CHANNELS_LAST
        )

        val t2 = System.currentTimeMillis()

        val outputTensor = module.forward(IValue.from(inputTensor))?.toTensor()

        val t3 = System.currentTimeMillis()

        Log.d("ExpTime", "All: ${t3 - t1}ms, forward: ${t3 - t2}ms")

        val scores = outputTensor?.dataAsFloatArray
        var maxScore = -Float.MAX_VALUE
        var maxScoreIdx = -1
        for (i in scores!!.indices) {
            if (scores[i] > maxScore) {
                maxScore = scores[i]
                maxScoreIdx = i
            }
        }
        if (maxScore < 2) {
            return -1
        }
        return maxScoreIdx
    }
}