package cn.foretree.view.card

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

/**
 * Created by silen on 20/09/2018
 */
object RxJava2Obserable {

    fun <T> io2main(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> create(action: () -> T): Observable<T> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(action.invoke())
            } catch (e: Exception) {
                emitter.onError(e)
            }

            emitter.onComplete()
        }
    }

    /**
     * 创建一个异步任务
     * @param function
     * @return
     */
    fun <T> async(action: () -> T): Observable<T> {
        return create(action).compose(io2main())
    }
}