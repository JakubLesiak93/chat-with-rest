package com.example.jkbll93.czatwithrest;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.jkbll93.czatwithrest", appContext.getPackageName());
    }

    /**
     * Created by jkbll93 on 2017-11-29.
     */

    public static class Klient {

        private String receive;

        public  Klient ( String m){
            this.receive = m;
        }

        public String getReceive() {
            return receive;
        }
    }
}
