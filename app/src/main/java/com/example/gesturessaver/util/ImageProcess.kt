package com.example.gesturessaver.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import java.io.*

object ImageProcess {

    private val newBitmap by lazy { Bitmap.createBitmap(224, 224, Bitmap.Config.ARGB_8888) }

    fun resetImgSize(bm: Bitmap, scale: Int): Bitmap {
        // 获得图片的宽高.
        val width = bm.width;
        val height = bm.height;
        // 计算缩放比例.
        val k = scale.toFloat() / Math.max(width, height);
        // 取得想要缩放的matrix参数.
        val matrix = Matrix();
        matrix.postScale(k, k);
        // 得到新的图片.
        val newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);

        if (newbm.height != 224 || newbm.width != 224) {
            val canvas = Canvas(newBitmap)
            if (newbm.height < newbm.width) {
                val concatHeight = 224 - newbm.height
                if (concatHeight < 2) {
                    val concatBitmap = concatBitmap4Vertical(concatHeight)
                    val canvas = Canvas(newBitmap)
                    canvas.drawBitmap(concatBitmap, 0f, 0f, null)
                    canvas.drawBitmap(newbm, 0f, concatBitmap.height.toFloat(), null)
                } else if (concatHeight % 2 != 0) {
                    val concatBitmapTop = concatBitmap4Vertical(concatHeight / 2)
                    val concatBitmapBottom = concatBitmap4Vertical(concatHeight - concatHeight / 2)
                    val canvas = Canvas(newBitmap)
                    canvas.drawBitmap(concatBitmapTop, 0f, 0f, null)
                    canvas.drawBitmap(newbm, 0f, concatBitmapTop.height.toFloat(), null)
                    canvas.drawBitmap(
                        concatBitmapBottom,
                        0f,
                        (concatBitmapTop.height + newbm.height).toFloat(),
                        null
                    )
                } else {
                    val concatBitmap = concatBitmap4Vertical(concatHeight / 2)
                    canvas.drawBitmap(concatBitmap, 0f, 0f, null)
                    canvas.drawBitmap(newbm, 0f, concatBitmap.height.toFloat(), null)
                    canvas.drawBitmap(
                        concatBitmap,
                        0f,
                        (concatBitmap.height + newbm.height).toFloat(),
                        null
                    )
                }

            } else {
                val concatWidth = 224 - newbm.width
                if (concatWidth < 2) {
                    val concatBitmap = concatBitmap4Horizontal(concatWidth)
                    canvas.drawBitmap(concatBitmap, 0f, 0f, null)
                    canvas.drawBitmap(newbm, concatBitmap.width.toFloat(), 0f, null)
                } else if (concatWidth % 2 != 0) {
                    val concatBitmapLeft = concatBitmap4Horizontal(concatWidth / 2)
                    val concatBitmapRight = concatBitmap4Horizontal(concatWidth - concatWidth / 2)
                    canvas.drawBitmap(concatBitmapLeft, 0f, 0f, null)
                    canvas.drawBitmap(newbm, concatBitmapLeft.width.toFloat(), 0f, null)
                    canvas.drawBitmap(
                        concatBitmapRight,
                        (concatBitmapLeft.width + newbm.width).toFloat(),
                        0f,
                        null
                    )
                } else {
                    val concatBitmap = concatBitmap4Horizontal(concatWidth / 2)
                    canvas.drawBitmap(concatBitmap, 0f, 0f, null)
                    canvas.drawBitmap(newbm, concatBitmap.width.toFloat(), 0f, null)
                    canvas.drawBitmap(
                        concatBitmap,
                        (concatBitmap.width + newbm.width).toFloat(),
                        0f,
                        null
                    )
                }
            }
            canvas.save()
            canvas.restore()
            return newBitmap
        }

        return newbm;
    }

    fun concatBitmap4Vertical(concatHeight: Int): Bitmap {
        val concatBitmap = Bitmap.createBitmap(224, concatHeight, Bitmap.Config.ARGB_8888)
        concatBitmap.eraseColor(Color.BLACK)
        return concatBitmap
    }

    fun concatBitmap4Horizontal(concatWidth: Int): Bitmap {
        val concatBitmap = Bitmap.createBitmap(concatWidth, 224, Bitmap.Config.ARGB_8888)
        concatBitmap.eraseColor(Color.BLACK)
        return concatBitmap
    }


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

    fun saveGesturebitmap(path: String, fileName: String, gesturesbitmap: Bitmap): Boolean {

        val bos = ByteArrayOutputStream()
        gesturesbitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val buffer = bos.toByteArray()
        if (buffer != null) {
            val filePath = File(path)
            if (!filePath.exists()) {
                filePath.mkdir()
            }
            val file = File(path + fileName)
            val outputStream: OutputStream = FileOutputStream(file)
            outputStream.write(buffer)
            outputStream.close()
            bos.close()
        }
        return true
    }

}