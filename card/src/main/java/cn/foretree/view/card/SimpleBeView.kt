package cn.foretree.view.card

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.RelativeLayout

/**
 * 简单的二维码图,位置在图上的8个方向
 * Created by silen on 2018/9/21 23:12
 * Copyright (c) 2018 in FORETREE
 */
class SimpleBeView : RelativeLayout {
    private var mAlign: Int = 5 //默认左上
    private var mOrientation: Int = 0//horizontal
    private var mQrWidth: Int = 0
    private var mQrHeight: Int = 0
    private var mContent: String = ""//二维码文字

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.SimpleBeView)
        mAlign = array.getInt(R.styleable.SimpleBeView_qr_align, 5)
        mOrientation = array.getInt(R.styleable.SimpleBeView_qr_orientation, 0)
        mContent = array.getString(R.styleable.SimpleBeView_qr_content) ?: ""
        mQrHeight = array.getDimensionPixelSize(R.styleable.SimpleBeView_qr_height, 0)
        mQrWidth = array.getDimensionPixelSize(R.styleable.SimpleBeView_qr_width, 0)
        setQrContent(context, mContent)
        array.recycle()
    }

    private fun setQrContent(context: Context, content: String?) {
        if (mContent.isEmpty()) return
        addView(ImageView(context).apply {
            layoutParams = getQrLayoutParams()
            setImageBitmap(BarcodeEncoder().encodeBitmap(context, content!!, null))
        })
    }

    private fun getQrLayoutParams(): RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(mQrWidth, mQrHeight).apply {
        when(mOrientation) {
            RelativeLayout.CENTER_HORIZONTAL -> addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE)
            RelativeLayout.CENTER_VERTICAL -> addRule(RelativeLayout.CENTER_VERTICAL,RelativeLayout.TRUE)
            RelativeLayout.CENTER_IN_PARENT -> {
                addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE)
                return@apply
            }
        }
        when(mAlign) {
            5 -> {
                addRule(RelativeLayout.ALIGN_PARENT_START)
                addRule(RelativeLayout.ALIGN_PARENT_TOP)
            }
            9 -> {
                addRule(RelativeLayout.ALIGN_PARENT_START)
                addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            }
            6 -> {
                addRule(RelativeLayout.ALIGN_PARENT_END)
                addRule(RelativeLayout.ALIGN_PARENT_TOP)
            }
            10 -> {
                addRule(RelativeLayout.ALIGN_PARENT_END)
                addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            }
        }

    }
}