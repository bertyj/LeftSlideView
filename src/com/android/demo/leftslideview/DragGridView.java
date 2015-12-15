
package com.android.demo.leftslideview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * @description ���϶������GridView
 * @blog http://blog.csdn.net/xiaanming
 * @author xiaanming
 */
@SuppressLint("NewApi")
public class DragGridView extends GridView {
    /**
     * DragGridView��item������Ӧ��ʱ�䣬 Ĭ����1000���룬Ҳ������������
     */
    private long dragResponseMS = 500;

    /**
     * �Ƿ������ק��Ĭ�ϲ�����
     */
    private boolean isDrag = false;

    private int mDownX;
    private int mDownY;
    private int moveX;
    private int moveY;
    /**
     * ������ק��position
     */
    private int mDragPosition;

    /**
     * �տ�ʼ��ק��item��Ӧ��View
     */
    private View mStartDragItemView = null;

    /**
     * ������ק�ľ�������ֱ����һ��ImageView
     */
    private ImageView mDragImageView;

    /**
     * ����
     */
    private final Vibrator mVibrator;

    private final WindowManager mWindowManager;
    /**
     * item����Ĳ��ֲ���
     */
    private WindowManager.LayoutParams mWindowLayoutParams;

    /**
     * ������ק��item��Ӧ��Bitmap
     */
    private Bitmap mDragBitmap;

    /**
     * ���µĵ㵽����item���ϱ�Ե�ľ���
     */
    private int mPoint2ItemTop;

    /**
     * ���µĵ㵽����item�����Ե�ľ���
     */
    private int mPoint2ItemLeft;

    /**
     * DragGridView������Ļ������ƫ����
     */
    private int mOffset2Top;

    /**
     * DragGridView������Ļ��ߵ�ƫ����
     */
    private int mOffset2Left;

    /**
     * ״̬���ĸ߶�
     */
    private final int mStatusHeight;

    /**
     * DragGridView�Զ����¹����ı߽�ֵ
     */
    private int mDownScrollBorder;

    /**
     * DragGridView�Զ����Ϲ����ı߽�ֵ
     */
    private int mUpScrollBorder;

    /**
     * DragGridView�Զ��������ٶ�
     */
    private static final int speed = 20;

    /**
     * item�����仯�ص��Ľӿ�
     */
    private OnChanageListener onChanageListener;

    public DragGridView(Context context) {
        this(context, null);
    }

    public DragGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mVibrator = (Vibrator) context
                .getSystemService(Context.VIBRATOR_SERVICE);
        mWindowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        mStatusHeight = getStatusHeight(context); // ��ȡ״̬���ĸ߶�
    }

    private final Handler mHandler = new Handler();

    // ���������Ƿ�Ϊ������Runnable
    private final Runnable mLongClickRunnable = new Runnable() {
        @Override
        public void run() {
            isDrag = true; // ���ÿ�����ק
            mVibrator.vibrate(50); // ��һ��
            mStartDragItemView.setVisibility(View.INVISIBLE);// ���ظ�item

            // �������ǰ��µĵ���ʾitem����
            createDragImage(mDragBitmap, mDownX, mDownY);
        }
    };

    /**
     * ���ûص��ӿ�
     * 
     * @param onChanageListener
     */
    public void setOnChangeListener(OnChanageListener onChanageListener) {
        this.onChanageListener = onChanageListener;
    }

    /**
     * ������Ӧ��ק�ĺ�������Ĭ����1000����
     * 
     * @param dragResponseMS
     */
    public void setDragResponseMS(long dragResponseMS) {
        this.dragResponseMS = dragResponseMS;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // ʹ��Handler�ӳ�dragResponseMSִ��mLongClickRunnable
                mHandler.postDelayed(mLongClickRunnable, dragResponseMS);

                mDownX = (int) ev.getX();
                mDownY = (int) ev.getY();

                // ���ݰ��µ�X,Y�����ȡ�����item��position
                mDragPosition = pointToPosition(mDownX, mDownY);

                if (mDragPosition == AdapterView.INVALID_POSITION) {
                    return super.dispatchTouchEvent(ev);
                }

                // ����position��ȡ��item����Ӧ��View
                mStartDragItemView = getChildAt(mDragPosition
                        - getFirstVisiblePosition());

                // �����⼸�������ҿ��Բο��ҵĲ��������ͼ��������
                mPoint2ItemTop = mDownY - mStartDragItemView.getTop();
                mPoint2ItemLeft = mDownX - mStartDragItemView.getLeft();

                mOffset2Top = (int) (ev.getRawY() - mDownY);
                mOffset2Left = (int) (ev.getRawX() - mDownX);

                // ��ȡDragGridView�Զ����Ϲ�����ƫ������С�����ֵ��DragGridView���¹���
                mDownScrollBorder = getHeight() / 4;
                // ��ȡDragGridView�Զ����¹�����ƫ�������������ֵ��DragGridView���Ϲ���
                mUpScrollBorder = getHeight() * 3 / 4;

                // ����mDragItemView��ͼ����
                mStartDragItemView.setDrawingCacheEnabled(true);
                // ��ȡmDragItemView�ڻ����е�Bitmap����
                mDragBitmap = Bitmap.createBitmap(mStartDragItemView
                        .getDrawingCache());
                // ��һ���ܹؼ����ͷŻ�ͼ���棬��������ظ��ľ���
                mStartDragItemView.destroyDrawingCache();

                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();

                // ��������ڰ��µ�item�����ƶ���ֻҪ������item�ı߽����ǾͲ��Ƴ�mRunnable
                if (!isTouchInItem(mStartDragItemView, moveX, moveY)) {
                    mHandler.removeCallbacks(mLongClickRunnable);
                }
                break;
            case MotionEvent.ACTION_UP:
                mHandler.removeCallbacks(mLongClickRunnable);
                mHandler.removeCallbacks(mScrollRunnable);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * �Ƿ�����GridView��item����
     * 
     * @param itemView
     * @param x
     * @param y
     * @return
     */
    private boolean isTouchInItem(View dragView, int x, int y) {
        int leftOffset = dragView.getLeft();
        int topOffset = dragView.getTop();
        if (x < leftOffset || x > leftOffset + dragView.getWidth()) {
            return false;
        }

        if (y < topOffset || y > topOffset + dragView.getHeight()) {
            return false;
        }

        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isDrag && mDragImageView != null) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    moveX = (int) ev.getX();
                    moveY = (int) ev.getY();
                    // �϶�item
                    onDragItem(moveX, moveY);
                    break;
                case MotionEvent.ACTION_UP:
                    onStopDrag();
                    isDrag = false;
                    break;
            }
            return true;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * �����϶��ľ���
     * 
     * @param bitmap
     * @param downX
     *            ���µĵ���Ը��ؼ���X����
     * @param downY
     *            ���µĵ���Ը��ؼ���X����
     */
    private void createDragImage(Bitmap bitmap, int downX, int downY) {
        mWindowLayoutParams = new WindowManager.LayoutParams();
        mWindowLayoutParams.format = PixelFormat.TRANSLUCENT; // ͼƬ֮��������ط�͸��
        mWindowLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        mWindowLayoutParams.x = downX - mPoint2ItemLeft + mOffset2Left;
        mWindowLayoutParams.y = downY - mPoint2ItemTop + mOffset2Top
                - mStatusHeight;
        mWindowLayoutParams.alpha = 0.55f; // ͸����
        mWindowLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        mDragImageView = new ImageView(getContext());
        mDragImageView.setImageBitmap(bitmap);
        mWindowManager.addView(mDragImageView, mWindowLayoutParams);
    }

    /**
     * �ӽ��������ƶ��϶�����
     */
    private void removeDragImage() {
        if (mDragImageView != null) {
            mWindowManager.removeView(mDragImageView);
            mDragImageView = null;
        }
    }

    /**
     * �϶�item��������ʵ����item�����λ�ø��£�item���໥�����Լ�GridView�����й���
     * 
     * @param x
     * @param y
     */
    private void onDragItem(int moveX, int moveY) {
        mWindowLayoutParams.x = moveX - mPoint2ItemLeft + mOffset2Left;
        mWindowLayoutParams.y = moveY - mPoint2ItemTop + mOffset2Top
                - mStatusHeight;
        mWindowManager.updateViewLayout(mDragImageView, mWindowLayoutParams); // ���¾����λ��
        onSwapItem(moveX, moveY);

        // GridView�Զ�����
        mHandler.post(mScrollRunnable);
    }

    /**
     * ��moveY��ֵ�������Ϲ����ı߽�ֵ������GridView�Զ����Ϲ��� ��moveY��ֵС�����¹����ı߽�ֵ������GridView�Զ����¹���
     * ���򲻽��й���
     */
    private final Runnable mScrollRunnable = new Runnable() {
        @Override
        public void run() {
            int scrollY;
            if (moveY > mUpScrollBorder) {
                scrollY = speed;
                mHandler.postDelayed(mScrollRunnable, 25);
            } else if (moveY < mDownScrollBorder) {
                scrollY = -speed;
                mHandler.postDelayed(mScrollRunnable, 25);
            } else {
                scrollY = 0;
                mHandler.removeCallbacks(mScrollRunnable);
            }

            // �����ǵ���ָ����GridView���ϻ������¹�����ƫ������ʱ�򣬿���������ָû���ƶ�������DragGridView���Զ��Ĺ���
            // �������������������onSwapItem()����������item
            onSwapItem(moveX, moveY);

            smoothScrollBy(scrollY, 10);
        }
    };

    /**
     * ����item,���ҿ���item֮�����ʾ������Ч��
     * 
     * @param moveX
     * @param moveY
     */
    private void onSwapItem(int moveX, int moveY) {
        // ��ȡ������ָ�ƶ������Ǹ�item��position
        int tempPosition = pointToPosition(moveX, moveY);

        // ����tempPosition �ı��˲���tempPosition������-1,����н���
        if (tempPosition != mDragPosition
                && tempPosition != AdapterView.INVALID_POSITION) {
            if (onChanageListener != null) {
                onChanageListener.onChange(mDragPosition, tempPosition);
            }

            getChildAt(tempPosition - getFirstVisiblePosition()).setVisibility(
                    View.INVISIBLE);// �϶������µ�item,�µ�item���ص�
            getChildAt(mDragPosition - getFirstVisiblePosition())
                    .setVisibility(View.VISIBLE);// ֮ǰ��item��ʾ����

            mDragPosition = tempPosition;
        }
    }

    /**
     * ֹͣ��ק���ǽ�֮ǰ���ص�item��ʾ���������������Ƴ�
     */
    private void onStopDrag() {
        View view = getChildAt(mDragPosition - getFirstVisiblePosition());
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
        removeDragImage();
    }

    /**
     * ��ȡ״̬���ĸ߶�
     * 
     * @param context
     * @return
     */
    private static int getStatusHeight(Context context) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        ((Activity) context).getWindow().getDecorView()
                .getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass
                        .getField("status_bar_height").get(localObject)
                        .toString());
                statusHeight = context.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    /**
     * @author xiaanming
     */
    public interface OnChanageListener {

        /**
         * ��item����λ�õ�ʱ��ص��ķ���������ֻ��Ҫ�ڸ÷�����ʵ�����ݵĽ�������
         * 
         * @param form
         *            ��ʼ��position
         * @param to
         *            ��ק����position
         */
        public void onChange(int form, int to);
    }
}