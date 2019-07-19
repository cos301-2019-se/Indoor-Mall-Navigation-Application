package com.example.navigator;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

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
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        Log.i("TESTING", "Instrumentation: " + InstrumentationRegistry.getInstrumentation());
        Log.i("TESTING", "Context: " + InstrumentationRegistry.getContext());
        Log.i("TESTING", "TargetContext: " + InstrumentationRegistry.getTargetContext());
        Log.i("TESTING", "Arguments: " + InstrumentationRegistry.getArguments());
        assertEquals("com.example.navigator", appContext.getPackageName());
    }
}