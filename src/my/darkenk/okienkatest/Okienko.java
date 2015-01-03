/**
 * Copyright (C) 2014, Dariusz Kluska <darkenk@gmail.com>
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
    public Okienko(Context context, ViewGroup root, final Intent intent) {
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
                mWindow.setX(mWindow.getX() + dx);
                mWindow.setY(mWindow.getY() + dy);
            }
        });
        mRightCorner = mWindow.findViewById(R.id.right_corner);
        mRightCorner.setOnTouchListener(new TouchMoveListener() {

            @Override
            protected void onMove(float dx, float dy) {
                ViewGroup.LayoutParams lp = mWindow.getLayoutParams();
                lp.width += dx;
                lp.height += dy;
                mWindow.setLayoutParams(lp);
            }
        });
        mLeftCorner = mWindow.findViewById(R.id.left_corner);
        mLeftCorner.setOnTouchListener(new TouchMoveListener() {

            @Override
            protected void onMove(float dx, float dy) {
                mWindow.setX(mWindow.getX() + dx);
                ViewGroup.LayoutParams lp = mWindow.getLayoutParams();
                lp.width -= dx;
                lp.height += dy;
                mWindow.setLayoutParams(lp);
            }
        });
        this.post(new Runnable() {
            @Override
            public void run() {
                mActivityViewWrapper.startActivity(intent);
            }
        });
    }

    private abstract class TouchMoveListener implements OnTouchListener {

        private float mLastX, mLastY;

        @Override
        public boolean onTouch(View v, MotionEvent e) {
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                mLastX = e.getRawX();
                mLastY = e.getRawY();
            } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
                onMove(e.getRawX() - mLastX, e.getRawY() - mLastY);
                mLastX = e.getRawX();
                mLastY = e.getRawY();
            } else if (e.getAction() == MotionEvent.ACTION_UP) {
                mLastX = mLastY = 0f;
            }
            return true;
        }

        protected abstract void onMove(float dx, float dy);
    }

}
