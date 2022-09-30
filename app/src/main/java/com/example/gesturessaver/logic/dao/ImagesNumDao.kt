package com.example.gesturessaver.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.example.gesturessaver.GesturesSaverApplication
import com.example.gesturessaver.logic.model.ImageClass

object ImagesNumDao {

    fun saveImagesNum(imageClass: Int, imageNum: Int) {
        sharedPreferences().edit {
            when(imageClass) {
                0 -> putInt("images_class_0", imageNum)
                1 -> putInt("images_class_1", imageNum)
                2 -> putInt("images_class_2", imageNum)
                3 -> putInt("images_class_3", imageNum)
                4 -> putInt("images_class_4", imageNum)
                5 -> putInt("images_class_5", imageNum)
                6 -> putInt("images_class_6", imageNum)
                7 -> putInt("images_class_7", imageNum)
            }
        }
    }

    fun getImageClass(): ImageClass {
        val images_class_0 = sharedPreferences().getInt("images_class_0", 0)
        val images_class_1 = sharedPreferences().getInt("images_class_1", 0)
        val images_class_2 = sharedPreferences().getInt("images_class_2", 0)
        val images_class_3 = sharedPreferences().getInt("images_class_3", 0)
        val images_class_4 = sharedPreferences().getInt("images_class_4", 0)
        val images_class_5 = sharedPreferences().getInt("images_class_5", 0)
        val images_class_6 = sharedPreferences().getInt("images_class_6", 0)
        val images_class_7 = sharedPreferences().getInt("images_class_7", 0)
        return ImageClass(
            images_class_0,
            images_class_1,
            images_class_2,
            images_class_3,
            images_class_4,
            images_class_5,
            images_class_6,
            images_class_7
        )
    }

    private fun sharedPreferences() = GesturesSaverApplication
        .context.getSharedPreferences("imagesNum", Context.MODE_PRIVATE)
}