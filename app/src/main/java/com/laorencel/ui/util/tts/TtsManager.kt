package com.laorencel.ui.util.tts

import android.os.Build
import android.speech.tts.TextToSpeech
import com.laorencel.ui.UiApplication
import com.laorencel.uilibrary.util.kt.log.logD
import java.util.LinkedList
import java.util.Locale
import java.util.Queue

object TtsManager {

    private var tts: TextToSpeech? = null
    private var hasInit = false
    private var canContinue = false
    private val messageQueue: Queue<String> = LinkedList()


    private fun init() {
        tts = TextToSpeech(
            UiApplication.instance()
        ) { status ->
            logD("TtsManager init status:$status")
            if (status == TextToSpeech.SUCCESS) {
                tts!!.setPitch(1.0f) //方法用来控制音调
                tts!!.setSpeechRate(1.0f) //用来控制语速

                //判断是否支持下面两种语言
                //                    int result1 = tts.setLanguage(Locale.US);
                val setLanguageResult = tts!!.setLanguage(Locale.SIMPLIFIED_CHINESE)

                //                    boolean a = (result1 == TextToSpeech.LANG_MISSING_DATA || result1 == TextToSpeech.LANG_NOT_SUPPORTED);
                //                    boolean b = (result2 == TextToSpeech.LANG_MISSING_DATA || result2 == TextToSpeech.LANG_NOT_SUPPORTED);

                //                    Log.i("zhh_tts", "US支持否？--》" + a + "\nzh-CN支持否》--》" + b);
                logD("TtsManager init status:$status setLanguageResult:$setLanguageResult")
                hasInit = true

                start()
            }
        }
    }

    /**
     * 加入一条新的语音文本并播放
     *
     * @param message
     */
    fun queue(message: String?) {
        //offer 向后添加元素
        messageQueue.offer(message)
        start()
    }

    /**
     * 清空之前的语言并播放新的语音
     *
     * @param message 语音文本
     */
    fun start(message: String?) {
        stop(true)
        queue(message)
    }

    private fun start() {
        //初始化未完成时，调用语音会报错警告，可以在初始化完成后start一次
        if (!hasInit) {
            init()
            return
        }
        val message = messageQueue.poll() ?: return
        canContinue = true
        //输入中文，若不支持的设备则不会读出来
        //第二个参数queueMode用于指定发音队列模式，两种模式选择。
        //（1）TextToSpeech.QUEUE_FLUSH：该模式下在有新任务时候会清除当前语音任务，执行新的语音任务
        //（2）TextToSpeech.QUEUE_ADD：该模式下会把新的语音任务放到语音任务之后，等前面的语音任务执行完了才会执行新的语音任务。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts!!.speak(message, TextToSpeech.QUEUE_ADD, null, null)
        } else {
            tts!!.speak(message, TextToSpeech.QUEUE_ADD, null)
        }
        if (canContinue) {
            start()
        }
    }

    /**
     * 停止播放
     */
    fun stop() {
        canContinue = false
        if (null != tts && tts!!.isSpeaking) {
            tts!!.stop()
        }
    }

    /**
     * 停止播放并清空之前的语音文本
     *
     * @param clear 是否清空
     */
    fun stop(clear: Boolean) {
        stop()
        if (clear && null != messageQueue) {
            messageQueue.clear()
        }
    }
}