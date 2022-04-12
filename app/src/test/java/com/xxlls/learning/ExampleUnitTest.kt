package com.xxlls.learning

import android.os.Handler
import android.os.Looper
import android.os.Message
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    fun handlerTest() {

    }

    class LooperThread : Thread() {
        var mHandler: Handler? = null

        override fun run() {
            Looper.prepare()
            Looper.myLooper()?.setMessageLogging {

            }
            mHandler = object : Handler() {
                //【见 3.1】
                override fun handleMessage(msg: Message) {

                }
            }
            Looper.loop()
        }

    }


}