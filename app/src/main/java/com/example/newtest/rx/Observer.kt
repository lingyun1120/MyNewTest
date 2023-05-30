package com.example.newtest.rx

//观察者
interface Observer<T> {
    //订阅
    fun onSubscribe()
    //事件发送
    fun onNext(t: T)
    //错误
    fun onError(t:Throwable)
    //事件完成
    fun onComplete()
}

//同样持有了一个Observer观察者
open class BasicFuseabObserver<T, U>(var oberver: Observer<U>) : Observer<T> {
    override fun onNext(t: T) {}
    override fun onSubscribe() {}
    override fun onComplete() {}
    override fun onError(t: Throwable) {}
}