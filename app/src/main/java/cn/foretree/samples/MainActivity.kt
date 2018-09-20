package cn.foretree.samples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun clickGenerate(view: View) {
        TestViewH(this).run {
            generate().subscribe {
                iv_image.setImageBitmap(it)
            }.isDisposed
        }
    }

    fun clickGenerate2(view: View) {
        TestViewV(this).run {
            generate().subscribe {
                iv_image2.setImageBitmap(it)
            }.isDisposed
        }
    }
}
