package com.example.newtest.rx

import android.os.Handler
import android.os.Looper
import android.os.Message
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * @Author XTP
 * @CreateTime 2023/3/20
 * @Description:
 */
abstract class Scheduler {

    abstract fun createWorker(): Worker

    fun scheduleDirect(task: Runnable) {
        val worker = createWorker()
        worker.schedule(task)
    }

    interface Worker {
        fun schedule(runnable: Runnable)
    }
}

class HandlerScheduler(var handler: Handler) : Scheduler() {

    override fun createWorker(): Worker {
        return HandlerWorker(handler)
    }

    class HandlerWorker(var handler: Handler) : Worker {

        override fun schedule(runnable: Runnable) {
            val message = Message.obtain(handler, runnable)
            message.obj = this
            handler.sendMessage(message)
        }

    }
}
class NewThreadScheduler : Scheduler() {

    override fun createWorker(): Worker {
        return NewThreadWorker()
    }

    class NewThreadWorker : Worker {
        var executorService: ExecutorService? = null

        init {
            executorService = Executors.newScheduledThreadPool(2)
        }

        override fun schedule(runnable: Runnable) {
            executorService?.execute(runnable)
        }
    }
}

class Schedulers {
    companion object {
        private val MAIN_THREAD = HandlerScheduler(Handler(Looper.getMainLooper()))
        private val NEW_THREAD = NewThreadScheduler()

        fun mainThread(): Scheduler {
            return MAIN_THREAD
        }

        fun newThread(): Scheduler {
            return NEW_THREAD
        }
    }
}

class ObservableSubscribeOn<T>(
    observableSource: ObservableSource<T>,
    private val scheduler: Scheduler
) : AbstractObservableWithUpStream<T, T>(observableSource) {

    override fun subscribeActual(observer: Observer<T>) {
        //将订阅逻辑抽离到一个Runnable里
        scheduler.scheduleDirect(SubscribeTask(observableSource, SubscribeOnObserver(observer)))
    }

    class SubscribeOnObserver<T>(downstream: Observer<T>) : BasicFuseabObserver<T, T>(downstream)

    class SubscribeTask<T>(
        private val observableSource: ObservableSource<T>,
        private val subscribeOnObserver: SubscribeOnObserver<T>
    ) : Runnable {
        //真正执行订阅逻辑的Runnable，运行线程决定了订阅线程
        override fun run() {
            observableSource.subscribe(subscribeOnObserver)
        }
    }
}

class ObservableObserveOn<T>(
    observableSource: ObservableSource<T>,
    private val scheduler: Scheduler
) : AbstractObservableWithUpStream<T, T>(observableSource) {

    override fun subscribeActual(observer: Observer<T>) {
        val worker = scheduler.createWorker()
        observableSource.subscribe(ObserveOnObserver(observer, worker))
    }

    class ObserveOnObserver<T>(observer: Observer<T>, var worker: Scheduler.Worker) :
        BasicFuseabObserver<T, T>(observer), Runnable {

        @Volatile
        var done = false
        private var queue: ArrayDeque<T>? = null

        @Volatile
        var error: Throwable? = null

        @Volatile
        var over = false

        init {
            queue = ArrayDeque()
        }

        override fun onSubscribe() {
            oberver.onSubscribe()
            schedule()
        }

        override fun onNext(t: T) {
            if (done) {
                return
            }
            queue?.add(t)
            schedule()
        }

        override fun onComplete() {
            if (done) {
                return
            }
            done = true
            schedule()
        }

        override fun onError(t: Throwable) {
            if (done) {
                return
            }
            done = true
            error = t
            schedule()
        }

        override fun run() {
            drainNormal()
        }
        //执行了线程的切换
        private fun schedule() {
            worker.schedule(this)
        }
        //观察者的数据观察
        private fun drainNormal() {
            var arrayDeque = queue
            var a = oberver

            while (true) {
                var d = done
                var t = arrayDeque?.removeAt(0)
                val empty = t == null
                if (checkTerminated(d, empty, a)) {
                    return
                }
                if (t == null) {
                    break
                }
                a.onNext(t)
            }
        }

        /**
         * 判断是否终止
         */
        private fun checkTerminated(d: Boolean, empty: Boolean, a: Observer<T>): Boolean {
            if (over) {
                queue?.clear()
                return true
            }
            if (d) {
                var e = error
                if (e is Throwable) {
                    over = true
                    a.onError(e)
                    return true
                } else if (empty) {
                    over = true
                    a.onComplete()
                    return true
                }
            }
            return false
        }
    }
}