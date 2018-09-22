package cn.foretree.view.card

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.widget.ImageView

/**
 * Created by silen on 2018/9/22 9:03
 * Copyright (c) 2018 in FORETREE
 */
class QrImageView: ImageView {
    private var mContent: String = ""
    private var mColor: Int = Color.BLACK
    private var mLogo: Bitmap? = null
    private var mBackground: Bitmap? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        context.obtainStyledAttributes(attrs, R.styleable.QrImageView).apply {
            mContent = getString(R.styleable.QrImageView_qr_text)?:""
            mColor = getColor(R.styleable.QrImageView_qr_color, Color.BLACK)
            mLogo = BitmapFactory.decodeResource(resources,getResourceId(R.styleable.QrImageView_qr_logo,
                    0))
            mBackground = BitmapFactory.decodeResource(resources, getResourceId(R.styleable.QrImageView_qr_background,
                    0))
            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec))
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawQrBitmap(canvas)
    }

    private fun drawQrBitmap(canvas: Canvas?) {
        val bitmap = BarcodeEncoder().encodeBitmap(context, mContent, mLogo)
        val matrix = Matrix()
        matrix.setScale(1f*measuredWidth/bitmap.width, 1f*measuredHeight/bitmap.height)
        canvas?.drawBitmap(bitmap,matrix, Paint(Paint.ANTI_ALIAS_FLAG))
    }
}