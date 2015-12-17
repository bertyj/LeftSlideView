/*
 * @author Jing Yang
 * @date 2015.12.16
 */

package com.android.demo.leftslideview.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.demo.leftslideview.R;

import java.util.List;

public class CardAdapter extends BaseAdapter {
    private final LayoutInflater mLayoutInflater;
    private final List<BaseCard> mCardList;

    public CardAdapter(Context mContext, List<BaseCard> mCardList) {
        super();
        this.mCardList = mCardList;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mCardList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mCardList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        CardViewTag cardViewTag;
        BaseCard card = (BaseCard) getItem(position);
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.imageonly_card_layout, null);
            ImageView cardImageView = (ImageView) convertView.findViewById(R.id.card_image);
            ImageView labelIconImageView = (ImageView) convertView.findViewById(R.id.label_icon);
            TextView labelContentTextView = (TextView) convertView.findViewById(R.id.label_content);
            cardViewTag = new CardViewTag(cardImageView, labelIconImageView, labelContentTextView);
            convertView.setTag(cardViewTag);
        } else {
            cardViewTag = (CardViewTag) convertView.getTag();
        }
        cardViewTag.mCardImageView.setBackgroundResource(card.getBackgroundImage());
        cardViewTag.mLabelContentTextView.setText(card.getLabelContent());
        cardViewTag.mLabelIconImageView.setBackgroundResource(card.getLabelIcon());
        return convertView;
    }

    class CardViewTag {
        private final ImageView mCardImageView, mLabelIconImageView;
        private final TextView mLabelContentTextView;

        public CardViewTag(ImageView mCardImageView, ImageView mLabelIconImageView, TextView mLabelContentTextView) {
            this.mCardImageView = mCardImageView;
            this.mLabelIconImageView = mLabelIconImageView;
            this.mLabelContentTextView = mLabelContentTextView;
        }

    }

}
