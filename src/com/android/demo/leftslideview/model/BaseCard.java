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

    private CardType mCardType;
    private String mLabelContent;
    private String mTopContent;
    private String mBottomContent;
    private String mTopTime;
    private String mBottomTime;
    private int mTopIcon;
    private int mBottomIcon;
    private int mBackgroundImage;
    private int mLabelIcon;

    public BaseCard() {}

    public String getLabelContent() {
        return mLabelContent;
    }

    public void setLabelContent(String mLabelContent) {
        this.mLabelContent = mLabelContent;
    }

    public String getContentTop() {
        return mTopContent;
    }

    public void setContentTop(String mContentTop) {
        this.mTopContent = mContentTop;
    }

    public String getContentBottom() {
        return mBottomContent;
    }

    public void setContentBottom(String mContentBottom) {
        this.mBottomContent = mContentBottom;
    }

    public CardType getCardType() {
        return mCardType;
    }

    public void setCardType(CardType mCardType) {
        this.mCardType = mCardType;
    }

    public String getTimeTop() {
        return mTopTime;
    }

    public void setTimeTop(String mTimeTop) {
        this.mTopTime = mTimeTop;
    }

    public String getTimeBottom() {
        return mBottomTime;
    }

    public void setTimeBottom(String mTimeBottom) {
        this.mBottomTime = mTimeBottom;
    }

    public int getTopIcon() {
        return mTopIcon;
    }

    public void setTopIcon(int mTopIcon) {
        this.mTopIcon = mTopIcon;
    }

    public int getBottomIcon() {
        return mBottomIcon;
    }

    public void setBottomIcon(int mBottomIcon) {
        this.mBottomIcon = mBottomIcon;
    }

    public int getBackgroundImage() {
        return mBackgroundImage;
    }

    public void setBackgroundImage(int mBackgroundImage) {
        this.mBackgroundImage = mBackgroundImage;
    }

    public int getLabelIcon() {
        return mLabelIcon;
    }

    public void setLabelIcon(int mLabelIcon) {
        this.mLabelIcon = mLabelIcon;
    }

}
