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

import com.android.demo.leftslideview.model.BaseCard;
import com.android.demo.leftslideview.model.BaseCard.CardType;
import com.android.demo.leftslideview.model.CardAdapter;
import com.android.demo.leftslideview.model.MessageFlag;
import com.android.demo.leftslideview.widget.DragGridView;
import com.android.demo.leftslideview.widget.DragGridView.OnChanageListener;
import com.android.demo.leftslideview.widget.SlidingLayout;

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
                case MessageFlag.HIDE_SLIDE:
                    mBtnShow.setBackgroundResource(R.drawable.btn_hide);
                    break;
                case MessageFlag.SHOW_SLIDE:
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
        // Intent intent = new Intent(this, FloatButtonService.class);
        // startService(intent);
    }

    protected void initCardList() {
        BaseCard musicCard = new BaseCard();
        musicCard.setCardType(CardType.CARDTYPE_IMAGE_ONLY);
        musicCard.setBackgroundImage(R.drawable.music_info_image);
        musicCard.setLabelIcon(R.drawable.label_icon_music);
        musicCard.setLabelContent("“Ù ¿÷");
        mCardList.add(musicCard);

        BaseCard settingCard = new BaseCard();
        settingCard.setCardType(CardType.CARDTYPE_IMAGE_ONLY);
        settingCard.setBackgroundImage(R.drawable.image_setting_active);
        settingCard.setLabelIcon(R.drawable.label_icon_setting);
        settingCard.setLabelContent("My Car");
        mCardList.add(settingCard);

        BaseCard radioCard = new BaseCard();
        radioCard.setCardType(CardType.CARDTYPE_IMAGE_ONLY);
        radioCard.setBackgroundImage(R.drawable.image_radio_info);
        radioCard.setLabelIcon(R.drawable.label_icon_radio);
        radioCard.setLabelContent("µÁ Ã®");
        mCardList.add(radioCard);

        BaseCard navigationCard = new BaseCard();
        navigationCard.setCardType(CardType.CARDTYPE_IMAGE_ONLY);
        navigationCard.setBackgroundImage(R.drawable.image_navigation_background);
        navigationCard.setLabelIcon(R.drawable.label_icon_navigation);
        navigationCard.setLabelContent("µº ∫Ω");
        mCardList.add(navigationCard);

    }

    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        switch (view.getId()) {
            case R.id.btn_show:
                if (mSlidingLayout.isLeftLayoutVisible()) {
                    mSlidingLayout.scrollToRightLayout();
                    Message msg = new Message();
                    msg.what = MessageFlag.HIDE_SLIDE;
                    mHandler.sendMessage(msg);
                } else {
                    mSlidingLayout.scrollToLeftLayout();
                    Message msg = new Message();
                    msg.what = MessageFlag.SHOW_SLIDE;
                    mHandler.sendMessage(msg);
                }
                break;
        }
    }

}
