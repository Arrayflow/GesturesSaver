package com.example.gesturessaver

import android.content.Context
import android.content.SharedPreferences
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import com.example.gesturessaver.databinding.ActivityMainBinding


import org.pytorch.IValue
import org.pytorch.MemoryFormat
import org.pytorch.Module
import org.pytorch.torchvision.TensorImageUtils
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.SimpleFormatter

class ActivityMain : AppCompatActivity(), ListenTouchEvent, TouchUpEvent, TouchUpEventLeft {
    private lateinit var binding: ActivityMainBinding
    lateinit var sp: SharedPreferences
    lateinit var viewModel: MainViewModel
    private var postfix = ""
    private var currentImg = 0
    lateinit var Gesturebitmap: Bitmap
    lateinit var gestureView: GestureView
    private val timeFormat = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS")
    private val newBitmap by lazy { Bitmap.createBitmap(224, 224, Bitmap.Config.ARGB_8888) }

    var module: Module? = null
    var gestureName: String = ""
    private lateinit var curGesture: Bitmap
    var paintWidth = 3
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val views = binding.root
        setContentView(views)
        sp = getPreferences(Context.MODE_PRIVATE)
        val class_1 = sp.getInt("class_1", 1)
        val class_2 = sp.getInt("class_2", 1)
        val class_3 = sp.getInt("class_3", 1)
        val class_4 = sp.getInt("class_4", 1)
        val class_5 = sp.getInt("class_5", 1)
        val class_6 = sp.getInt("class_6", 1)
        val class_7 = sp.getInt("class_7", 1)
        val class_8 = sp.getInt("class_8", 1)
        val class_9 = sp.getInt("class_9", 1)
        val class_10 = sp.getInt("class_10", 1)


        viewModel = ViewModelProvider(
            this, MainViewModelFactory(
                class_1, class_2, class_3, class_4, class_5,
                class_6, class_7, class_8, class_9, class_10
            )
        ).get(MainViewModel::class.java)

//        resetNum() //??????ViewModel

        binding.ERASERRECT.isChecked = true
        gestureView = findViewById<GestureView>(R.id.signature)
        //??????????????????(???????????????--??????????????????10,???????????????,???????????????)
        gestureView.setPaintColor(Color.WHITE)
        gestureView.setPaintWidth(paintWidth)
        gestureView.setCanvasColor(Color.BLACK)

        binding.signatureLeft.setPaintColor(Color.WHITE)
        binding.signatureLeft.setPaintWidth(paintWidth)
        gestureView.setCanvasColor(Color.BLACK)

        //??????
        binding.clear.setOnClickListener { view: View? ->
            gestureView.clear()
            //??????????????????(???????????????--??????????????????10,???????????????,???????????????)
            gestureView.setPaintColor(Color.WHITE)
            gestureView.setPaintWidth(paintWidth)
            gestureView.setCanvasColor(Color.BLACK)
            gestureView.setBackgroundColor(Color.BLACK)
            binding.gestureName.text = "???????????????"
        }


        //??????
        binding.save.setOnClickListener { view: View? ->
            try {
                postfix = when {
                    binding.ERASERRECT.isChecked -> {
                        currentImg = viewModel.class_1++
                        "/class_ERASERRECT/ERASERRECT_${getCurrentTime()}.png"
                    }
                    binding.SELECTCIRCLE.isChecked -> {
                        currentImg = viewModel.class_2++
                        "/class_SELECTCIRCLE/SELECTCIRCLE_${getCurrentTime()}.png"
                    }
                    binding.INSERTSPACELINEDOWN.isChecked -> {
                        currentImg = viewModel.class_3++
                        "/class_INSERTSPACELINEDOWN/INSERTSPACELINEDOWN_${getCurrentTime()}.png"
                    }
                    binding.INSERTSPACELINEUP.isChecked -> {
                        currentImg = viewModel.class_4++
                        "/class_INSERTSPACELINEUP/INSERTSPACELINEUP_${getCurrentTime()}.png"
                    }
                    binding.INSERTTEXTLINEDOWN.isChecked -> {
                        currentImg = viewModel.class_5++
                        "/class_INSERTTEXTLINEDOWN/INSERTTEXTLINEDOWN_${getCurrentTime()}.png"
                    }
                    binding.INSERTTEXTLINEUP.isChecked -> {
                        currentImg = viewModel.class_6++
                        "/class_INSERTTEXTLINEUP/INSERTTEXTLINEUP_${getCurrentTime()}.png"
                    }
                    binding.KEYENTER.isChecked -> {
                        currentImg = viewModel.class_7++
                        "/class_KEYENTER/KEYENTER_${getCurrentTime()}.png"
                    }
                    binding.KEYTAB.isChecked -> {
                        currentImg = viewModel.class_8++
                        "/class_KEYTAB/KEYTAB_${getCurrentTime()}.png"
                    }
                    binding.SELECTRECT.isChecked -> {
                        currentImg = viewModel.class_9++
                        "/class_SELECTRECT/SELECTRECT_${getCurrentTime()}.png"
                    }
                    binding.SELECTLINE.isChecked -> {
                        currentImg = viewModel.class_10++
                        "/class_SELECTLINE/SELECTLINE_${getCurrentTime()}.png"
                    }
                    else -> ""
                }
                if (saveGesturebitmap(filesDir.path + postfix, Gesturebitmap)) {
                    Log.d("saved:", getCurrentTime())
                    Toast.makeText(this, "????????????_${currentImg}???", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            gestureView.clear()
            //??????????????????(???????????????--??????????????????10,???????????????,???????????????)
            gestureView.setBackgroundColor(Color.BLACK)
            gestureView.setPaintColor(Color.WHITE)
            gestureView.setPaintWidth(paintWidth)
            gestureView.setCanvasColor(Color.BLACK)
        }
//        initMoudle()

        //????????????
        binding.startRec.setOnClickListener {
            curGesture = gestureView.image
            Gesturebitmap = resetImgSize(curGesture, 224)
            predict()
        }

//        signatureView.setListenTouchEvent(object : ListenTouchEvent {
//            override fun onTouchView(motionEvent: MotionEvent) {
//                super.onTouchView(motionEvent)
//                if (motionEvent.action == MotionEvent.ACTION_UP) {
//
//                }
//            }
//        })
        gestureView.setTouchUpEvent(this)
//        signatureView.setTouchUpEvent(object : TouchUpEvent {
//            override fun onSuccess() {
//
//            }
//
//        })
        binding.signatureLeft.setTouchUpEvent(this)

    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

    override fun onPause() {
        super.onPause()
        sp.edit {
            putInt("class_1", viewModel.class_1)
            putInt("class_2", viewModel.class_2)
            putInt("class_3", viewModel.class_3)
            putInt("class_4", viewModel.class_4)
            putInt("class_5", viewModel.class_5)
            putInt("class_6", viewModel.class_6)
            putInt("class_7", viewModel.class_7)
            putInt("class_8", viewModel.class_8)
            putInt("class_9", viewModel.class_9)
            putInt("class_10", viewModel.class_10)
        }
    }

    fun resetNum() {
        viewModel.class_1 = 1
        viewModel.class_2 = 1
        viewModel.class_3 = 1
        viewModel.class_4 = 1
        viewModel.class_5 = 1
        viewModel.class_6 = 1
        viewModel.class_7 = 1
        viewModel.class_8 = 1
        viewModel.class_9 = 1
        viewModel.class_10 = 1

    }

    fun resetImgSize(bm: Bitmap, scale: Int): Bitmap {
        // ?????????????????????.
        val width = bm.width;
        val height = bm.height;
        // ??????????????????.
        val k = scale.toFloat() / Math.max(width, height);
        // ?????????????????????matrix??????.
        val matrix = Matrix();
        matrix.postScale(k, k);
        // ??????????????????.
        val newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);

        if (newbm.height < newbm.width) {
            val concatBitmap = concatBitmapVertical(newbm)
            val canvas = Canvas(newBitmap)
            canvas.drawBitmap(concatBitmap, 0f, 0f, null)
            canvas.drawBitmap(newbm, 0f, concatBitmap.height.toFloat(), null)
            canvas.drawBitmap(
                concatBitmap,
                0f,
                (concatBitmap.height + newbm.height).toFloat(),
                null
            )
            canvas.save()
            canvas.restore()
        } else {
            val concatBitmap = concatBitmapHorizontal(newbm)
            val canvas = Canvas(newBitmap)
            canvas.drawBitmap(concatBitmap, 0f, 0f, null)
            canvas.drawBitmap(newbm, concatBitmap.width.toFloat(), 0f, null)
            canvas.drawBitmap(concatBitmap, (concatBitmap.width + newbm.width).toFloat(), 0f, null)
            canvas.save()
            canvas.restore()
        }
        return newBitmap;
    }

    fun concatBitmapVertical(bmSrc: Bitmap): Bitmap {
        val concatHeight = (224 - bmSrc.height) / 2
        val concatBitmap = Bitmap.createBitmap(224, concatHeight, Bitmap.Config.ARGB_8888)
        concatBitmap.eraseColor(Color.BLACK)
        return concatBitmap
    }

    fun concatBitmapHorizontal(bmSrc: Bitmap): Bitmap {
        val concatWidth = (224 - bmSrc.width) / 2
        val concatBitmap = Bitmap.createBitmap(concatWidth, 224, Bitmap.Config.ARGB_8888)
        concatBitmap.eraseColor(Color.BLACK)
        return concatBitmap
    }

    fun initMoudle() {
        try {
//            bitmap = BitmapFactory.decodeStream(assets.open("KEYTAB_5.png"))
//            bitmap = setImgSize(bitmap, 224)
            module = Module.load(assetFilePath(this, "gesturesRec_9_7.pt"))
        } catch (e: Exception) {
            Log.e("Pytorch", "Error Pytorch", e)
            finish()
        }

    }


    fun predict() {

        val inputTensor = TensorImageUtils.bitmapToFloat32Tensor(
            Gesturebitmap, TensorImageUtils.TORCHVISION_NORM_MEAN_RGB,
            TensorImageUtils.TORCHVISION_NORM_STD_RGB, MemoryFormat.CHANNELS_LAST
        )
        val outputTensor = module?.forward(IValue.from(inputTensor))?.toTensor()
        val scores = outputTensor?.dataAsFloatArray
        var maxScore = -Float.MAX_VALUE
        var maxScoreIdx = -1
        for (i in scores!!.indices) {
            if (scores[i] > maxScore) {
                maxScore = scores[i]
                maxScoreIdx = i
            }
        }
        gestureName = Gestures.gestures[maxScoreIdx]
        binding.gestureName.text = gestureName

    }

    companion object {
        @Throws(IOException::class)
        fun assetFilePath(context: Context, assetName: String?): String? {
            val file = File(context.filesDir, assetName)
            if (file.exists() && file.length() > 0) {
                return file.absolutePath
            }
            context.assets.open(assetName!!).use { `is` ->
                FileOutputStream(file).use { os ->
                    val buffer = ByteArray(4 * 1024)
                    var read: Int
                    while (`is`.read(buffer).also { read = it } != -1) {
                        os.write(buffer, 0, read)
                    }
                    os.flush()
                }
                return file.absolutePath
            }
        }
    }

    override fun onSave() {
        curGesture = gestureView.image
        Gesturebitmap = resetImgSize(curGesture, 224)
        binding.save.performClick()
    }

    override fun onSaveLeft() {
        curGesture = binding.signatureLeft.image
        Gesturebitmap = resetImgSize(curGesture, 224)

    }

    private fun saveGesturebitmap(path: String, gesturesbitmap: Bitmap): Boolean {

        val bos = ByteArrayOutputStream()
        gesturesbitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val buffer = bos.toByteArray()
        if (buffer != null) {
            val file = File(path)
//            if (!file.exists()) {
//                file.mkdir()
//            }
            val outputStream: OutputStream = FileOutputStream(file)
            outputStream.write(buffer)
            outputStream.close()
            bos.close()
        }
        return true
    }

    private fun getCurrentTime(): String {
        timeFormat.timeZone = TimeZone.getTimeZone("GMT+08")
        val currentData = Date(System.currentTimeMillis())
        val curTime = timeFormat.format(currentData)
        return curTime
    }
}