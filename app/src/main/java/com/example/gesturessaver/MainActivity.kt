package com.example.gesturessaver

import android.content.Context
import android.content.SharedPreferences
import android.graphics.*
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import androidx.lifecycle.ViewModelProvider
import com.example.gesturessaver.databinding.ActivityMainBinding

import com.example.gesturessaver.ui.GestureView
import com.example.gesturessaver.ui.GesturesViewModel
import com.example.gesturessaver.ui.ImagesNumViewModel
import com.example.gesturessaver.util.ImageProcess


import org.pytorch.IValue
import org.pytorch.MemoryFormat
import org.pytorch.Module
import org.pytorch.torchvision.TensorImageUtils
import java.io.*
import java.lang.Long
import java.text.SimpleDateFormat
import java.util.*


class ActivityMain : AppCompatActivity(), TouchUpEvent {
    private lateinit var binding: ActivityMainBinding

    val imagesNumViewModel by lazy {
        ViewModelProvider(this).get(ImagesNumViewModel::class.java)
    }
    val gesturesViewModel by lazy {
        ViewModelProvider(this).get(GesturesViewModel::class.java)
    }
    private var pathInfix = ""
    private var fileName = ""
    private var currentImg = 0
    lateinit var gesturebitmap: Bitmap
    lateinit var gestureView: GestureView
    private val timeFormat = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS")
    private lateinit var curGesture: Bitmap
    var paintWidth = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val views = binding.root
        setContentView(views)

        val fileSavedPath = getFilesPath(this)

        val imageClass = imagesNumViewModel.getImageClass()

        binding.ERASERRECT.isChecked = true
        gestureView = findViewById<GestureView>(R.id.signature)
        //设置画笔颜色(可以不设置--默认画笔宽度10,画笔颜色黑,背景颜色白)
        gestureView.setPaintColor(Color.WHITE)
        gestureView.setPaintWidth(paintWidth)
        gestureView.setCanvasColor(Color.BLACK)


        gestureView.setCanvasColor(Color.BLACK)

        //清除
        binding.clear.setOnClickListener { view: View? ->
            gestureView.clear()

            gestureView.setPaintColor(Color.WHITE)
            gestureView.setPaintWidth(paintWidth)
            gestureView.setCanvasColor(Color.BLACK)
            gestureView.setBackgroundColor(Color.BLACK)
            binding.gestureName.text = "请画出手势"
        }

        //保存
        binding.save.setOnClickListener { view: View? ->
            try {
                pathInfix = when {
                    binding.ERASERRECT.isChecked -> {
                        "/class_ERASERRECT/"
                    }
                    binding.SELECTCIRCLE.isChecked -> {
                        "/class_SELECTCIRCLE/"
                    }
                    binding.INSERTSPACELINEDOWN.isChecked -> {
                        "/class_INSERTSPACELINEDOWN/"
                    }
                    binding.INSERTSPACELINEUP.isChecked -> {
                        "/class_INSERTSPACELINEUP/"
                    }
                    binding.INSERTTEXTLINEDOWN.isChecked -> {
                        "/class_INSERTTEXTLINEDOWN/"
                    }
                    binding.INSERTTEXTLINEUP.isChecked -> {
                        "/class_INSERTTEXTLINEUP/"
                    }
                    binding.KEYENTER.isChecked -> {
                        "/class_KEYENTER/"
                    }
                    binding.KEYTAB.isChecked -> {
                        "/class_KEYTAB/"
                    }
                    binding.SELECTRECT.isChecked -> {
                        "/class_SELECTRECT/"
                    }
                    binding.SELECTLINE.isChecked -> {
                        "/class_SELECTLINE/"
                    }
                    else -> ""
                }

                fileName = when {
                    binding.ERASERRECT.isChecked -> {
                        imagesNumViewModel.addImageNum(0)
                        currentImg = imagesNumViewModel.class0_num_LiveData.value!!
                        imagesNumViewModel.saveImagesNum(0, currentImg)
                        "ERASERRECT_${getCurrentTime()}.png"
                    }
                    binding.SELECTCIRCLE.isChecked -> {
                        imagesNumViewModel.addImageNum(1)
                        currentImg = imagesNumViewModel.class1_num_LiveData.value!!
                        imagesNumViewModel.saveImagesNum(1, currentImg)
                        "SELECTCIRCLE_${getCurrentTime()}.png"
                    }
                    binding.INSERTSPACELINEDOWN.isChecked -> {
                        imagesNumViewModel.addImageNum(2)
                        currentImg = imagesNumViewModel.class2_num_LiveData.value!!
                        imagesNumViewModel.saveImagesNum(2, currentImg)
                        "INSERTSPACELINEDOWN_${getCurrentTime()}.png"
                    }
                    binding.INSERTTEXTLINEDOWN.isChecked -> {
                        imagesNumViewModel.addImageNum(3)
                        currentImg = imagesNumViewModel.class3_num_LiveData.value!!
                        imagesNumViewModel.saveImagesNum(3, currentImg)
                        "INSERTTEXTLINEDOWN_${getCurrentTime()}.png"
                    }
                    binding.INSERTTEXTLINEUP.isChecked -> {
                        imagesNumViewModel.addImageNum(4)
                        currentImg = imagesNumViewModel.class4_num_LiveData.value!!
                        imagesNumViewModel.saveImagesNum(4, currentImg)
                        "INSERTTEXTLINEUP_${getCurrentTime()}.png"
                    }
                    binding.KEYENTER.isChecked -> {
                        imagesNumViewModel.addImageNum(5)
                        currentImg = imagesNumViewModel.class5_num_LiveData.value!!
                        imagesNumViewModel.saveImagesNum(5, currentImg)
                        "KEYENTER_${getCurrentTime()}.png"
                    }
                    binding.KEYTAB.isChecked -> {
                        imagesNumViewModel.addImageNum(6)
                        currentImg = imagesNumViewModel.class6_num_LiveData.value!!
                        imagesNumViewModel.saveImagesNum(6, currentImg)
                        "KEYTAB_${getCurrentTime()}.png"
                    }
                    binding.SELECTRECT.isChecked -> {
                        imagesNumViewModel.addImageNum(7)
                        currentImg = imagesNumViewModel.class7_num_LiveData.value!!
                        imagesNumViewModel.saveImagesNum(7, currentImg)
                        "SELECTRECT_${getCurrentTime()}.png"
                    }
                    else -> ""
                }

                if (ImageProcess.saveGesturebitmap(
                        fileSavedPath + pathInfix,
                        fileName,
                        gesturebitmap
                    )
                ) {
                    Log.d("saved:", getCurrentTime())
                    Log.d("paths", filesDir.path)
                    Toast.makeText(this, "保存成功_${currentImg}张", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            gestureView.clear()

            gestureView.setBackgroundColor(Color.BLACK)
            gestureView.setPaintColor(Color.WHITE)
            gestureView.setPaintWidth(paintWidth)
            gestureView.setCanvasColor(Color.BLACK)
        }

        gesturesViewModel.gesturesLiveData.observe(this, androidx.lifecycle.Observer {
            val gesture = gesturesViewModel.gesturesLiveData.value
            binding.gestureName.text = gesture
        })

        //识别笔迹
        binding.startRec.setOnClickListener {
            curGesture = gestureView.image
            gesturebitmap = ImageProcess.resetImgSize(curGesture, 224)

        }

        binding.recognizeSelected.isChecked = true

        gestureView.setTouchUpEvent(this)


    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }


    override fun onSaveorRec() {
        curGesture = gestureView.image
        gesturebitmap = ImageProcess.resetImgSize(curGesture, 224)
        if (binding.saveSelected.isChecked) {
            saveGesture()
        } else {
            predit(gesturebitmap)
        }
    }

    fun predit(bitmap: Bitmap) {
        gesturesViewModel.predict(bitmap)
    }

    fun getFilesPath(context: Context): String {
        var filePath: String
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
            || !Environment.isExternalStorageRemovable()
        ) {
            //外部存储可用
            filePath = context.getExternalFilesDir(null)!!.getPath();
        } else {
            //外部存储不可用
            filePath = context.getFilesDir().getPath();
        }
        return filePath
    }


    private fun getCurrentTime(): String {
        timeFormat.timeZone = TimeZone.getTimeZone("GMT+08")
        val currentData = Date(System.currentTimeMillis())
        val curTime = timeFormat.format(currentData)
        return curTime
    }

    private fun saveGesture() {
        binding.save.performClick()
    }

}