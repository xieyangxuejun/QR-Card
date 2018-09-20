package cn.foretree.view.card

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.annotation.NonNull
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

/**
 * 生成二维码
 * Created by silen on 20/09/2018
 */
class BarcodeEncoder {
    companion object {
        @JvmStatic
        private val WHITE = -0x1
        @JvmStatic
        private val BLACK = -0x1000000
    }

    fun createBitmap(matrix: BitMatrix, logo: Bitmap?): Bitmap {
        val width = matrix.width
        val height = matrix.height

        val hx = width / 2
        val hy = height / 2

        val half = if (logo == null) 0 else logo.height / 2

        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                if (x > hx - half && x < hx + half && y > hy - half && y < hy + half) {
                    if (logo == null) continue
                    pixels[offset + x] = logo.getPixel(x - hx + half, y - hy + half)
                } else {
                    pixels[offset + x] = if (matrix.get(x, y)) BLACK else WHITE
                }
            }
        }

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    }

    @Throws(WriterException::class)
    fun encode(contents: String, format: BarcodeFormat, width: Int, height: Int): BitMatrix {
        try {
            return MultiFormatWriter().encode(contents, format, width, height)
        } catch (e: WriterException) {
            throw e
        } catch (e: Exception) {
            // ZXing sometimes throws an IllegalArgumentException
            throw WriterException(e)
        }

    }

    @Throws(WriterException::class)
    fun encode(contents: String, format: BarcodeFormat, width: Int, height: Int, hints: Map<EncodeHintType, *>): BitMatrix {
        try {
            val writer = MultiFormatWriter()
            return writer.encode(contents, format, width, height, hints)
        } catch (e: WriterException) {
            throw e
        } catch (e: Exception) {
            throw WriterException(e)
        }

    }

    @Throws(WriterException::class)
    fun encodeBitmap(context: Context, contents: String, logo: Bitmap?): Bitmap {
        val value = context.resources.getDimensionPixelOffset(R.dimen.dp300)
        return encodeBitmap(contents, BarcodeFormat.QR_CODE, value, value, getEncodeHintTypeMap(), logo)
    }

    @Throws(WriterException::class)
    fun encodeBitmap(contents: String, format: BarcodeFormat,
                     value: Int, logo: Bitmap?): Bitmap {
        return encodeBitmap(contents, format, value, value, logo)
    }

    @Throws(WriterException::class)
    fun encodeBitmap(contents: String, format: BarcodeFormat,
                     width: Int, height: Int, logo: Bitmap?): Bitmap {
        return encodeBitmap(contents, format, width, height, null, logo)
    }

    /**
     * 中间的logo暂时是3.44/1
     *
     * @param contents
     * @param format
     * @param width
     * @param height
     * @param hints
     * @param logo
     * @return
     * @throws WriterException
     */
    @Throws(WriterException::class)
    fun encodeBitmap(contents: String, format: BarcodeFormat,
                     width: Int, height: Int, hints: Map<EncodeHintType, *>?, logo: Bitmap?): Bitmap {
        val encode: BitMatrix
        if (hints == null) {
            encode = encode(contents, format, width, height)
        } else {
            encode = encode(contents, format, width, height, hints)
        }
        if (logo != null) {
            val m = getScaleMatrix(logo.width, logo.width, (height / 3.44).toInt())
            return createBitmap(encode, Bitmap.createBitmap(logo, 0, 0, logo.width,
                    logo.height, m, false))
        }
        return createBitmap(encode, null)
    }

    private fun getEncodeHintTypeMap(): Map<EncodeHintType, *> {
        return getEncodeHintTypeMap(2)
    }

    fun getEncodeHintTypeMap(margin: Int): Map<EncodeHintType, *> {
        val hst = mutableMapOf<EncodeHintType, Any>()
        // 设置字符编码
        hst.put(EncodeHintType.CHARACTER_SET, "UTF-8")
        // 设置二维码容错率
        hst.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H)
        //设置白边
        hst.put(EncodeHintType.MARGIN, margin)
        return hst
    }

    @NonNull
    private fun getScaleMatrix(dstWidth: Int, dstHeight: Int, goalValue: Int): Matrix {
        val m = Matrix()
        val sx = 1f * goalValue / dstWidth
        val sy = 1f * goalValue / dstHeight
        m.setScale(sx, sy)
        return m
    }
}