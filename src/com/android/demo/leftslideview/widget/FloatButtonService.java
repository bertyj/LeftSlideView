/*
 * @author Jing Yang
 * @date 2015.12.18
 */

package com.android.demo.leftslideview.widget;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.android.demo.leftslideview.R;

public class FloatButtonService extends Service {
    private LinearLayout mFloatButtonView;
    private ImageButton mHomeButton, mApplicationButton;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowManagerParams;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mWindowManager = (WindowManager) getApplication().getSystemService(WINDOW_SERVICE);
        mWindowManagerParams = new WindowManager.LayoutParams();
        setLayoutParams();
        LayoutInflater layoutInflater = LayoutInflater.from(getApplication());
        mFloatButtonView = (LinearLayout) layoutInflater.inflate(R.layout.float_button_layout, null);
        mWindowManager.addView(mFloatButtonView, mWindowManagerParams);
        mFloatButtonView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        mFloatButtonView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                // mWindowManagerParams.x = (int) event.getRawX() -
                // mFloatButtonView.getMeasuredWidth() / 2;
                // mWindowManagerParams.y = (int) event.getRawY() -
                // mFloatButtonView.getMeasuredHeight() / 2 - 25;
                mWindowManagerParams.x = (int) event.getRawX() + mWindowManager.getDefaultDisplay().getWidth();
                mWindowManagerParams.y = (int) event.getRawY() + mWindowManager.getDefaultDisplay().getHeight();
                mWindowManager.updateViewLayout(mFloatButtonView, mWindowManagerParams);
                return false;
            }
        });
    }

    private void setLayoutParams() {
        mWindowManagerParams.type = LayoutParams.TYPE_PHONE;
        mWindowManagerParams.format = PixelFormat.RGBA_8888;
        mWindowManagerParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
        mWindowManagerParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        mWindowManagerParams.x = 0;
        // mWindowManagerParams.x = mWindowManager.getDefaultDisplay().getWidth();
        mWindowManagerParams.y = 0;
        mWindowManagerParams.width = LayoutParams.WRAP_CONTENT;
        mWindowManagerParams.height = LayoutParams.WRAP_CONTENT;
    }

    private void setFloatButton() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

}
