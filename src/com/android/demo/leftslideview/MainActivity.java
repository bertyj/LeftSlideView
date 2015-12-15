
package com.android.demo.leftslideview;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

public class MainActivity extends Activity implements OnClickListener {

    private SlidingLayout mSlidingLayout;
    private ImageButton mBtnShow;
    private ListView mDummyListView;
    private ArrayAdapter<String> mAdapter;
    private final String[] mDummyItems = {
            "Content Item 1",
            "Content Item 2",
            "Content Item 3",
            "Content Item 4",
            "Content Item 5"
    };
    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case Flag.HIDE_SLIDE:
                    mBtnShow.setBackgroundResource(R.drawable.btn_hide);
                    break;
                case Flag.SHOW_SLIDE:
                    mBtnShow.setBackgroundResource(R.drawable.btn_show);
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main_activity_layout);
        mSlidingLayout = (SlidingLayout) findViewById(R.id.left_slide_view);
        mBtnShow = (ImageButton) findViewById(R.id.btn_show);
        mDummyListView = (ListView) findViewById(R.id.listview);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mDummyItems);
        mDummyListView.setAdapter(mAdapter);
        mSlidingLayout.setScrollEvent(mBtnShow, mHandler);
        mBtnShow.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.btn_show:
                if (mSlidingLayout.isLeftLayoutVisible()) {
                    mSlidingLayout.scrollToRightLayout();
                    Message msg = new Message();
                    msg.what = Flag.HIDE_SLIDE;
                    mHandler.sendMessage(msg);
                } else {
                    mSlidingLayout.scrollToLeftLayout();
                    Message msg = new Message();
                    msg.what = Flag.SHOW_SLIDE;
                    mHandler.sendMessage(msg);
                }
                break;
        }
    }

}

class Flag {
    public static final int SHOW_SLIDE = 0;
    public static final int HIDE_SLIDE = 1;
}
