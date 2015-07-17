package my.darkenk.okienkatest;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

class TouchMoveListener implements OnTouchListener {

    private float mLastX, mLastY;
    private float mInitX, mInitY;

    @Override
    public boolean onTouch(View v, MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            mLastX = e.getRawX();
            mLastY = e.getRawY();
            mInitX = e.getRawX();
            mInitY = e.getRawY();
            onStartMove();
        } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
            onMove(e.getRawX() - mLastX, e.getRawY() - mLastY);
            mLastX = e.getRawX();
            mLastY = e.getRawY();
        } else if (e.getAction() == MotionEvent.ACTION_UP) {
            onEndMove(e.getRawX() - mInitX, e.getRawY() - mInitY);
            mLastX = mLastY = 0f;
        }
        return true;
    }

    protected void onMove(float dx, float dy) {}

    protected void onEndMove(float dx, float dy) {}

    protected void onStartMove() {}
}