package my.darkenk.okienkatest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.util.Log;
import android.view.InputEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class Okienko extends RelativeLayout {

    ActivityViewWrapper mActivityViewWrapper;
    View mHeader;
    ViewGroup mWindow;
    View mRightCorner;
    View mLeftCorner;

    /**
     *
     * @param context ctx
     * @param root parent of component
     * @param intent activity to start in Okienko
     */
    public Okienko(Context context, ViewGroup root, View resizeFrame, final Intent intent) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        mWindow = (ViewGroup)inflater.inflate(R.layout.okienko_layout, root).findViewById(R.id.okienko_root);
        mWindow.setId(View.generateViewId());
        mActivityViewWrapper = new ActivityViewWrapper(context);

        ((ViewGroup)mWindow.findViewById(R.id.activity)).addView(mActivityViewWrapper.getActivityView());

        mHeader = mWindow.findViewById(R.id.header);
        mHeader.setOnTouchListener(new TouchMoveListener() {

            @Override
            protected void onMove(float dx, float dy) {
                mWindow.bringToFront();
                mWindow.setX(mWindow.getX() + dx);
                mWindow.setY(mWindow.getY() + dy);
            }
        });
        mRightCorner = mWindow.findViewById(R.id.right_corner);
        mRightCorner.setOnTouchListener(new ResizeListener(resizeFrame, mWindow) {

            @Override
            protected void onMove(float dx, float dy) {
                ViewGroup.LayoutParams lp = mResizeFrame.getLayoutParams();
                lp.width += dx;
                lp.height += dy;
                mResizeFrame.setLayoutParams(lp);
            }

        });
        mLeftCorner = mWindow.findViewById(R.id.left_corner);
        mLeftCorner.setOnTouchListener(new ResizeListener(resizeFrame, mWindow) {

            @Override
            protected void onMove(float dx, float dy) {
                mResizeFrame.setX(mResizeFrame.getX() + dx);
                ViewGroup.LayoutParams lp = mResizeFrame.getLayoutParams();
                lp.width -= dx;
                lp.height += dy;
                mResizeFrame.setLayoutParams(lp);
            }
        });
        this.post(new Runnable() {
            @Override
            public void run() {
                mActivityViewWrapper.startActivity(intent);
            }
        });
    }

    private class ResizeListener extends TouchMoveListener {

        protected View mResizeFrame;
        protected ViewGroup mWindow;

        public ResizeListener(View resizeFrame, ViewGroup window) {
            mResizeFrame = resizeFrame;
            mWindow = window;
        }

        @Override
        protected void onStartMove() {
            mResizeFrame.bringToFront();
            ViewGroup.LayoutParams lp = mResizeFrame.getLayoutParams();
            lp.width = mWindow.getLayoutParams().width;
            lp.height = mWindow.getLayoutParams().height;
            mResizeFrame.setLayoutParams(lp);
            mResizeFrame.setX(mWindow.getX());
            mResizeFrame.setY(mWindow.getY());
            mResizeFrame.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onEndMove(float dx, float dy) {
            mWindow.setX(mResizeFrame.getX());
            ViewGroup.LayoutParams lp = mWindow.getLayoutParams();
            lp.width = mResizeFrame.getLayoutParams().width;
            lp.height = mResizeFrame.getLayoutParams().height;
            mWindow.setLayoutParams(lp);
            mResizeFrame.setVisibility(View.INVISIBLE);
        }
    }

    public boolean injectInputEvent(InputEvent e) {
        return mActivityViewWrapper.injectInputEvent(e);
    }

}
