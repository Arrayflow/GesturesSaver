package com.example.gesturessaver

import com.example.gesturessaver.logic.dao.ImagesNumDao
import com.example.gesturessaver.logic.model.ImageClass

object Repository {

    fun saveImagesNum(imageClass: Int, imageNum: Int) = ImagesNumDao.saveImagesNum(imageClass, imageNum)

    fun getImageClass() = ImagesNumDao.getImageClass()

    fun addImagesNum(imageNum: Int) {

    }

    fun loadModel() {

    }



}