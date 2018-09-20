package cn.foretree.view.card

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.LinearLayoutCompat
import io.reactivex.Observable

/**
 *
 * Created by silen on 20/09/2018
 */
abstract class AbsBeView<T>(context: Context, val entity: QrEntity<T>) : RelativeLayout(context) {
    private val mMinWidth = resources.getDimensionPixelOffset(R.dimen.dp360)

    fun generate(): Observable<Bitmap> {
        return RxJava2Obserable.async {
            addView(ImageView(context).apply {
                layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT)
                scaleType = ImageView.ScaleType.FIT_CENTER
                //adjustViewBounds = true
                minimumWidth = mMinWidth
                bindBackground(this)
            })
            addView(buildQrLayout(View.inflate(context, getBodyLayoutId(), null)
                    .apply {
                        layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                        onBindBodyView(this, entity.data)
                    },
                    getQrLayoutView().apply {
                        findViewById<ImageView>(R.id.iv_qr_image).run {
                            if (this != null) {
                                setImageBitmap(BarcodeEncoder().encodeBitmap(context, entity.content, getLogo()))
                                bindQrImage(this)
                            }
                        }
                        findViewById<TextView>(R.id.tv_qr_title).run {
                            if (this != null) {
                                text = entity.title
                                setVisible(this, text)
                                bindQrTitle(this)
                            }
                        }
                        findViewById<TextView>(R.id.tv_qr_msg).run {
                            if (this != null) {
                                text = entity.msg
                                setVisible(this, text)
                                bindQrMsg(this)
                            }
                        }
                    }.apply {
                        layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                        bindQrLayout(this)
                    }
            ))
            ViewUtil.getBitmapFromView(this).apply {
                onSuccess(this)
            }
        }
    }

    private fun setVisible(view: View, value: CharSequence?) {
        view.visibility = if (value.isNullOrEmpty()) GONE else VISIBLE
    }

    open fun buildQrLayout(bodyView: View, qrView: View): LinearLayout = LinearLayout(context).apply {
        adjustParams(this)
        addView(bodyView)
        addView(qrView)
    }

    open fun adjustParams(linearLayout: LinearLayout) = linearLayout.apply {
        layoutParams = LinearLayoutCompat.LayoutParams(mMinWidth, LinearLayoutCompat.LayoutParams.WRAP_CONTENT)
        weightSum = 2f
        orientation = LinearLayout.VERTICAL
        background = ColorDrawable(Color.WHITE)
    }

    open fun getLogo(): Bitmap? = getLogoResId().run {
        if (this == 0) {
            BitmapFactory.decodeResource(resources, 0)
        } else {
            null
        }
    }

    open fun getLogoResId(): Int = 0

    @LayoutRes
    abstract fun getBodyLayoutId(): Int

    @LayoutRes
    fun getQrLayoutId(): Int = R.layout.layout_simple_bottom_qr

    abstract fun onBindBodyView(view: View, data: T)

    open fun getQrLayoutView(): View = View.inflate(context, getQrLayoutId(), null)

    open fun onSuccess(bitmap: Bitmap) {
        //generate success
        Log.d("==>", "generate success")
    }

    open fun bindQrImage(view: ImageView) {}
    open fun bindQrTitle(view: TextView) {}
    open fun bindQrMsg(view: TextView) {}
    open fun bindQrLayout(view: View) {}
    open fun bindBackground(view: ImageView) {}
}