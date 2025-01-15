package com.laorencel.uilibrary.util.kt

import android.util.LruCache
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer


/**
 * LiveDataBus 事件通知
 * 利用LiveData，不仅可以实现消息总线功能，而且对于订阅者，他们不需要关心何时取消订阅，极大减少了因为忘记取消订阅造成的内存泄漏风险。
 *
 * 最大允许100个订阅者
 *
 * 订阅消息：
 * LiveDataBus.with("key_test", String::class.java, true)
 *             .observe(this) {
 *                 println("LiveDataBus key_test $it")
 *             }
 *
 * 发送消息：
 * LiveDataBus.with("key_test", String::class.java, true).value = "test_value"
 */

object LiveDataBus {
    //    private val bus: MutableMap<String, BusMutableLiveData<Any>> by lazy { HashMap() }
    private val MAX_BUS_COUNT = 100 // 最大允许的 bus 对象数量
    private val bus: LruCache<String, BusMutableLiveData<Any>> by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        LruCache(
            MAX_BUS_COUNT
        )
    }

    //暴露一个线程安全的，给外界注册，订阅者关系
    @Synchronized
    fun <T> with(key: String, type: Class<T>, isStick: Boolean = false): BusMutableLiveData<T> {
        var liveData = bus.get(key)
        if (liveData == null) {
            liveData = BusMutableLiveData(isStick)
            bus.put(key, liveData)
        }
//        if (!bus.containsKey(key)){
//            bus[key] = BusMutableLiveData(isStick)
//        }
        return bus[key] as BusMutableLiveData<T>
    }

    class BusMutableLiveData<T> private constructor() : MutableLiveData<T>() {
        var isStick = false //开启，关闭 粘性

        //次构造
        constructor(isStick: Boolean) : this() {
            this.isStick = isStick
        }

        //hook
        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            super.observe(owner, observer)
            if (!isStick) {
                hook(observer = observer)
            } else {
            }

        }

        private fun <T> hook(observer: Observer<in T>) {
            // 获取mLastVersion
            val liveDataClass = LiveData::class.java

            // getDeclaredField是可以获取一个类的所有字段.
            // getField只能获取类的public 字段.
            val mObserversField = liveDataClass.getDeclaredField("mObservers")
            mObserversField.isAccessible = true //private 修饰也可以访问
            val mObserversObject = mObserversField.get(this)

            // 获取 private SafeIterableMap<Observer<? super T>, ObserverWrapper> mObservers = new SafeIterableMap<>();
            val mObserversClass = mObserversObject.javaClass

            // 获取 mObservers 的 get方法   protected Entry<K, V> get(K k){}
            val get = mObserversClass.getDeclaredMethod("get", Any::class.java)
            get.isAccessible = true

            // 执行get方法
            val invoke = get.invoke(mObserversObject, observer)

            // 获取 entry 中 的 value
            var observerWrapper: Any? = null
            if (invoke != null && invoke is Map.Entry<*, *>) {
                observerWrapper = invoke.value
            }

            if (observerWrapper == null) {
                throw NullPointerException("observerWrapper is null")
            }

            // 得到observerWrapper的类对象
            val superclass = observerWrapper.javaClass.superclass
            val mLastVersionField = superclass.getDeclaredField("mLastVersion")
            mLastVersionField.isAccessible = true

            // 得到mVersion
            val mVersionField = liveDataClass.getDeclaredField("mVersion")
            mVersionField.isAccessible = true

            // 版本对齐
            val versionValue = mVersionField.get(this)
            mLastVersionField.set(observerWrapper, versionValue)
        }

    }

}