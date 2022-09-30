package com.example.gesturessaver.ui

import androidx.lifecycle.*
import com.example.gesturessaver.Repository
import com.example.gesturessaver.logic.model.ImageClass

class ImagesNumViewModel : ViewModel() {

    private val class0_add_LiveData = MutableLiveData<Int>()
    private val class1_add_LiveData = MutableLiveData<Int>()
    private val class2_add_LiveData = MutableLiveData<Int>()
    private val class3_add_LiveData = MutableLiveData<Int>()
    private val class4_add_LiveData = MutableLiveData<Int>()
    private val class5_add_LiveData = MutableLiveData<Int>()
    private val class6_add_LiveData = MutableLiveData<Int>()
    private val class7_add_LiveData = MutableLiveData<Int>()


    val class0_num_LiveData: LiveData<Int>
        get() = class0_add_LiveData
    val class1_num_LiveData: LiveData<Int>
        get() = class1_add_LiveData
    val class2_num_LiveData: LiveData<Int>
        get() = class2_add_LiveData
    val class3_num_LiveData: LiveData<Int>
        get() = class3_add_LiveData
    val class4_num_LiveData: LiveData<Int>
        get() = class4_add_LiveData
    val class5_num_LiveData: LiveData<Int>
        get() = class5_add_LiveData
    val class6_num_LiveData: LiveData<Int>
        get() = class6_add_LiveData
    val class7_num_LiveData: LiveData<Int>
        get() = class7_add_LiveData


    fun saveImagesNum(image_class: Int, image_num: Int) = Repository.saveImagesNum(image_class, image_num)

    fun getImageClass(): ImageClass {
        val imageClass = Repository.getImageClass()
        class0_add_LiveData.value = imageClass.class_0
        class1_add_LiveData.value = imageClass.class_1
        class2_add_LiveData.value = imageClass.class_2
        class3_add_LiveData.value = imageClass.class_3
        class4_add_LiveData.value = imageClass.class_4
        class5_add_LiveData.value = imageClass.class_5
        class6_add_LiveData.value = imageClass.class_6
        class7_add_LiveData.value = imageClass.class_7
        return imageClass
    }

    fun addImageNum(image_class: Int) {
        when(image_class) {
            0 -> class0_add_LiveData.value = class0_add_LiveData.value?.plus(1)
            1 -> class1_add_LiveData.value = class1_add_LiveData.value?.plus(1)
            2 -> class2_add_LiveData.value = class2_add_LiveData.value?.plus(1)
            3 -> class3_add_LiveData.value = class3_add_LiveData.value?.plus(1)
            4 -> class4_add_LiveData.value = class4_add_LiveData.value?.plus(1)
            5 -> class5_add_LiveData.value = class5_add_LiveData.value?.plus(1)
            6 -> class6_add_LiveData.value = class6_add_LiveData.value?.plus(1)
            7 -> class7_add_LiveData.value = class7_add_LiveData.value?.plus(1)
        }
    }

}