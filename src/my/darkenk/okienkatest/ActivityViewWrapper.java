package my.darkenk.okienkatest;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.InputEvent;
import android.view.View;

import java.lang.reflect.Constructor;
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
