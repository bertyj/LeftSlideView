/*
 * @author Jing Yang
 * @date 2015.12.16
 */

package com.android.demo.leftslideview.model;

public class BaseCard {
    public enum CardType {
        CARDTYPE_IMAGE_ONLY,
        CARDTYPE_CONTENT_TOP_ONLY,
        CARDTYPE_CONTENT_TOP_AND_BOTTOM
    }

    private String mLabel;
    private String mContentTop;
    private String mContentBottom;
    private CardType mCardType;

    public BaseCard() {}

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String mLabel) {
        this.mLabel = mLabel;
    }

    public String getContentTop() {
        return mContentTop;
    }

    public void setContentTop(String mContentTop) {
        this.mContentTop = mContentTop;
    }

    public String getContentBottom() {
        return mContentBottom;
    }

    public void setContentBottom(String mContentBottom) {
        this.mContentBottom = mContentBottom;
    }

    public CardType getCardType() {
        return mCardType;
    }

    public void setCardType(CardType mCardType) {
        this.mCardType = mCardType;
    }

}
