package cn.foretree.samples

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import cn.foretree.view.card.AbsBeView
import cn.foretree.view.card.QrEntity
import kotlinx.android.synthetic.main.layout_test.view.*

/**
 * Created by silen on 20/09/2018
 */
class TestViewH(context: Context) : AbsBeView<String>(context, QrEntity("我是二维码", "https://google.com", "标题", "扫描二维码")) {

    override fun onBindBodyView(view: View, data: String) {
        view.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        view.tv_text.text = data
        view.setBackgroundColor(Color.RED)
    }

    override fun bindBackground(view: ImageView) {
        view.setImageResource(R.drawable.icon_bg)
    }

    override fun bindQrTitle(view: TextView) {
        //view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40f)
    }

    override fun bindQrImage(view: ImageView) {
        super.bindQrImage(view)
    }

    override fun bindQrLayout(view: View) {
        super.bindQrLayout(view)
        view.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
    }

    //绑定二维码msg
    override fun bindQrMsg(view: TextView) {
        super.bindQrMsg(view)
    }

    override fun getBodyLayoutId(): Int = R.layout.layout_test

    override fun adjustParams(linearLayout: LinearLayout): LinearLayout {
        return super.adjustParams(linearLayout).apply {
            orientation = LinearLayout.HORIZONTAL
            background = null
        }
    }
}