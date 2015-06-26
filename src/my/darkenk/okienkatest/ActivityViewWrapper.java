/**
 * Copyright (C) 2015, Dariusz Kluska <darkenk@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of the {organization} nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package my.darkenk.okienkatest;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.InputEvent;
import android.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ActivityViewWrapper {

    private final String TAG = "OkienkaWrapper";
    private Constructor<?> mDefaultCtor;
    private View mActivityView;
    private Method mStartActivityMethod;
    private Method mInjectInputEvent;

    public ActivityViewWrapper(Context ctx) {
        try {
            Class<?> clazz = Class.forName("android.app.ActivityView");
            mDefaultCtor = clazz.getConstructor(Context.class);
            mActivityView = (View)mDefaultCtor.newInstance(ctx);
            mStartActivityMethod = clazz.getMethod("startActivity", Intent.class);
            mInjectInputEvent = clazz.getDeclaredMethod("injectInputEvent", InputEvent.class);
            mInjectInputEvent.setAccessible(true);
        } catch (Exception e) {
            Log.e(TAG, "ActivityViewWrapper failed " + e);
        }
    }

    public void startActivity(Intent intent) {
        try {
            mStartActivityMethod.invoke(mActivityView, intent);
        } catch (Exception e) {
            Log.e(TAG, "ActivityViewWrapper failed startActivity ", e);
        }
    }

    public View getActivityView() {
        return mActivityView;
    }

    public boolean injectInputEvent(InputEvent e) {
        try {
            return (Boolean) mInjectInputEvent.invoke(mActivityView, e);
        } catch (Exception ex) {
            Log.e(TAG, "ActivitityViewWrapper failed injectInputEvent", ex);
        }
        return false;
    }
}
