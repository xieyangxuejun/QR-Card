package cn.foretree.view.card

/**
 * Created by silen on 20/09/2018
 */
data class QrEntity<T>(
        val data: T,
        val content: String,
        val title: String = "",
        val msg: String = ""
)