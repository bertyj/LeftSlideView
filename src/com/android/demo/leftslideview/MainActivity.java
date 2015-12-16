/*
 * @author Jing Yang
 * @date 2015.12.15
 */

package com.android.demo.leftslideview;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;

import com.android.demo.leftslideview.DragGridView.OnChanageListener;
import com.android.demo.leftslideview.model.BaseCard;
import com.android.demo.leftslideview.model.BaseCard.CardType;
import com.android.demo.leftslideview.model.CardAdapter;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends Activity implements OnClickListener {

    private SlidingLayout mSlidingLayout;
    private ImageButton mBtnShow;
    private DragGridView mDragGridView;
    private final ArrayList<BaseCard> mCardList = new ArrayList<BaseCard>();
    private CardAdapter mCardAdapter;

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
        setContentView(R.layout.main_activity_layout);
        mSlidingLayout = (SlidingLayout) findViewById(R.id.left_slide_view);
        mBtnShow = (ImageButton) findViewById(R.id.btn_show);
        mSlidingLayout.setScrollEvent(mBtnShow, mHandler);
        mBtnShow.setOnClickListener(this);
        mDragGridView = (DragGridView) findViewById(R.id.gridview_right_card);
        initCardList();
        mCardAdapter = new CardAdapter(this, mCardList);
        mDragGridView.setAdapter(mCardAdapter);
        mDragGridView.setOnChangeListener(new OnChanageListener() {

            @Override
            public void onChange(int from, int to) {
                BaseCard temp = mCardList.get(from);
                if (from < to) {
                    for (int i = from; i < to; i++) {
                        Collections.swap(mCardList, i, i + 1);
                    }
                } else if (from > to) {
                    for (int i = from; i > to; i--) {
                        Collections.swap(mCardList, i, i - 1);
                    }
                }
                mCardList.set(to, temp);
                mCardAdapter.notifyDataSetChanged();
            }
        });
    }

    protected void initCardList() {
        for (int i = 0; i < 6; i++) {
            BaseCard card = new BaseCard();
            card.setCardType(CardType.CARDTYPE_IMAGE_ONLY);
            card.setLabel("Talking To The Moon");
            mCardList.add(card);
        }
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
