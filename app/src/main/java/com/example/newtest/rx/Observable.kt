package com.example.newtest.rx

interface ObservableSource<T> {
    //订阅
    fun subscribe(observer: Observer<T>)
}

abstract class Observable<T> : ObservableSource<T> {
    companion object {
        fun <T> create(source: ObservableOnSubscribe<T>): Observable<T> {
            return ObservableCreate(source)
        }
    }

    fun <U> map(mapper: Function<T, U>): Observable<U> {
        //注意这里传入的this对象，就是上游的Observable对象
        // 传给下游，利用装饰者模式，mapper进行数据转换
        return ObservableMap(this, mapper)
    }

    override fun subscribe(observer: Observer<T>) {
        subscribeActual(observer);
    }
    //抽象订阅方法，这里会传入观察者的对象，交给数据发送者
    protected abstract fun subscribeActual(observer: Observer<T>)
}

interface Emitter<T> {
    fun onNext(t: T)
    fun onError(t:Throwable)
    fun onComplete()
}

interface ObservableOnSubscribe<T> {
    //被订阅者在此实现方法里发送数据
    fun subscribe(emitter: Emitter<T>)
}

interface Function<T, U> {
    //外部实现数据的转换逻辑
    fun apply(t: T): U
}

class ObservableCreate<T>(var observableOnSubscribe: ObservableOnSubscribe<T>) : Observable<T>() {

    //被观察者订阅观察者时，具体实现数据发送
    override fun subscribeActual(observer: Observer<T>) {
        val emitterCreate = EmitterCreate(observer)
        observableOnSubscribe.subscribe(emitterCreate)
        //触发了onSubscribe
        observer.onSubscribe()
    }
    //构造方法传入了观察者
    class EmitterCreate<T>(private val observer: Observer<T>) : Emitter<T> {

        override fun onNext(t: T) {
            //观察者接收到被观察者通过Emitter发送的数据
            observer.onNext(t)
        }

        override fun onError(t: Throwable) {
            observer.onError(t)
        }

        override fun onComplete() {
            observer.onComplete()
        }
    }
}

//被观察者
class ObservableMap<T, U>(observableSource: ObservableSource<T>, var mapper: Function<T, U>) :
    AbstractObservableWithUpStream<T, U>(observableSource) {

    override fun subscribeActual(observer: Observer<U>) {
        //调用持有的被观察者的subscribe，同时创建了装饰后的观察者对象
        observableSource.subscribe(MapObserver(observer, mapper))
    }
    //观察者
    class MapObserver<T, U>(observer: Observer<U>, var mapper: Function<T, U>) : BasicFuseabObserver<T, U>(observer) {

        override fun onNext(t: T) {
            //执行装饰逻辑及原被装饰者逻辑
            val apply = mapper.apply(t)
            oberver.onNext(apply)
        }
    }
}

//注意这里持有了一个ObservableSource被观察者对象
abstract class AbstractObservableWithUpStream<T, U>(var observableSource: ObservableSource<T>) :
    Observable<U>() {

}