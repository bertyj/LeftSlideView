
package com.android.demo.leftslideview.widget;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;

import com.android.demo.leftslideview.model.MessageFlag;

public class SlidingLayout extends LinearLayout implements OnTouchListener {
    private Handler mHandler;

    /**
     * ������ʾ��������಼��ʱ����ָ������Ҫ�ﵽ���ٶȡ�
     */
    public static final int SNAP_VELOCITY = 200;

    /**
     * ��Ļ���ֵ��
     */
    private final int screenWidth;

    /**
     * ��಼�������Ի����������Ե��ֵ����಼�ֵĿ��������marginLeft�����ֵ֮�󣬲����ټ��١�
     */
    private int leftEdge;

    /**
     * ��಼�������Ի��������ұ�Ե��ֵ��Ϊ0����marginLeft����0֮�󣬲������ӡ�
     */
    private final int rightEdge = 0;

    /**
     * ��಼����ȫ��ʾʱ�������Ҳ಼�ֵĿ��ֵ��
     */
    private int leftLayoutPadding = 80;

    /**
     * ��¼��ָ����ʱ�ĺ����ꡣ
     */
    private float xDown;

    /**
     * ��¼��ָ�ƶ�ʱ�ĺ����ꡣ
     */
    private float xMove;

    /**
     * ��¼�ֻ�̧��ʱ�ĺ����ꡣ
     */
    private float xUp;

    /**
     * ��಼�ֵ�ǰ����ʾ�������ء�ֻ����ȫ��ʾ������ʱ�Ż���Ĵ�ֵ�����������д�ֵ��Ч��
     */
    private boolean isLeftLayoutVisible;

    /**
     * ��಼�ֶ���
     */
    private View leftLayout;

    /**
     * �Ҳ಼�ֶ���
     */
    private View rightLayout;

    /**
     * ���ڼ����໬�¼���View��
     */
    private View mBindView;

    /**
     * ��಼�ֵĲ�����ͨ���˲���������ȷ����಼�ֵĿ�ȣ��Լ�����leftMargin��ֵ��
     */
    private MarginLayoutParams leftLayoutParams;

    /**
     * �Ҳ಼�ֵĲ�����ͨ���˲���������ȷ���Ҳ಼�ֵĿ�ȡ�
     */
    private MarginLayoutParams rightLayoutParams;

    /**
     * ���ڼ�����ָ�������ٶȡ�
     */
    private VelocityTracker mVelocityTracker;

    /**
     * ��дSlidingLayout�Ĺ��캯�������л�ȡ����Ļ�Ŀ�ȡ�
     * 
     * @param context
     * @param attrs
     */
    public SlidingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // WindowManager wm = (WindowManager) context
        // .getSystemService(Context.WINDOW_SERVICE);
        // screenWidth = wm.getDefaultDisplay().getWidth();
        screenWidth = (int) getScreenWidth((Activity) context);
        leftLayoutPadding = screenWidth / 2;// ��಼����ȫ��ʾʱ�������Ҳ಼�ֵĿ��ֵΪ��Ļ��ȵ�һ��
    }

    /**
     * ��ȡ��Ļ���
     * 
     * @param act
     *            Activity����
     * @return ���ص���Ļ��ȵ�float���͵�ֵ
     */
    public float getScreenWidth(Activity act) {
        float width;
        // ʵ����һ����Ļ��ʾ�߶ȵ���Ķ���
        DisplayMetrics metrics = new DisplayMetrics();
        // ͨ����Ļ�Ĺ����࣬�Գߴ�������һЩ���ݲ���
        act.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
        return width;
    }

    /**
     * �󶨼����໬�¼���View�����ڰ󶨵�View���л����ſ�����ʾ��������಼�֡�
     * 
     * @param bindView
     *            ��Ҫ�󶨵�View����
     */
    public void setScrollEvent(View bindView, Handler handler) {
        mBindView = bindView;
        mBindView.setOnTouchListener(this);
        mHandler = handler;
    }

    /**
     * ����Ļ��������಼�ֽ��棬�����ٶ��趨Ϊ50.
     */
    public void scrollToLeftLayout() {
        new ScrollTask().execute(50);
    }

    /**
     * ����Ļ�������Ҳ಼�ֽ��棬�����ٶ��趨Ϊ-50.
     */
    public void scrollToRightLayout() {
        new ScrollTask().execute(-50);
    }

    /**
     * ��಼���Ƿ���ȫ��ʾ����������ȫ���أ����������д�ֵ��Ч��
     * 
     * @return ��಼����ȫ��ʾ����true����ȫ���ط���false��
     */
    public boolean isLeftLayoutVisible() {
        return isLeftLayoutVisible;
    }

    /**
     * ��onLayout�������趨��಼�ֺ��Ҳ಼�ֵĲ�����
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            // ��ȡ��಼�ֶ���
            leftLayout = getChildAt(0);
            leftLayoutParams = (MarginLayoutParams) leftLayout.getLayoutParams();
            // ������಼�ֶ���Ŀ��Ϊ��Ļ��ȼ�ȥleftLayoutPadding
            leftLayoutParams.width = screenWidth - leftLayoutPadding;
            // ��������߾�Ϊ������಼�ֵĿ��
            leftEdge = -leftLayoutParams.width;
            leftLayoutParams.leftMargin = leftEdge;
            leftLayout.setLayoutParams(leftLayoutParams);
            // ��ȡ�Ҳ಼�ֶ���
            rightLayout = getChildAt(1);
            rightLayoutParams = (MarginLayoutParams) rightLayout.getLayoutParams();
            rightLayoutParams.width = screenWidth;
            rightLayout.setLayoutParams(rightLayoutParams);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        createVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // ��ָ����ʱ����¼����ʱ�ĺ�����
                xDown = event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                // ��ָ�ƶ�ʱ���ԱȰ���ʱ�ĺ����꣬������ƶ��ľ��룬��������಼�ֵ�leftMarginֵ���Ӷ���ʾ��������಼��
                xMove = event.getRawX();
                int distanceX = (int) (xMove - xDown);
                if (isLeftLayoutVisible) {
                    leftLayoutParams.leftMargin = distanceX;
                } else {
                    leftLayoutParams.leftMargin = leftEdge + distanceX;
                }
                if (leftLayoutParams.leftMargin < leftEdge) {
                    leftLayoutParams.leftMargin = leftEdge;
                } else if (leftLayoutParams.leftMargin > rightEdge) {
                    leftLayoutParams.leftMargin = rightEdge;
                }
                leftLayout.setLayoutParams(leftLayoutParams);
                break;
            case MotionEvent.ACTION_UP:
                // ��ָ̧��ʱ�������жϵ�ǰ���Ƶ���ͼ���Ӷ������ǹ�������಼�֣����ǹ������Ҳ಼��
                xUp = event.getRawX();
                if (wantToShowLeftLayout()) {
                    if (shouldScrollToLeftLayout()) {
                        scrollToLeftLayout();
                        Message msg = new Message();
                        msg.what = MessageFlag.SHOW_SLIDE;
                        mHandler.sendMessage(msg);
                    } else {
                        scrollToRightLayout();
                        Message msg = new Message();
                        msg.what = MessageFlag.HIDE_SLIDE;
                        mHandler.sendMessage(msg);
                    }
                } else if (wantToShowRightLayout()) {
                    if (shouldScrollToContent()) {
                        scrollToRightLayout();
                        Message msg = new Message();
                        msg.what = MessageFlag.HIDE_SLIDE;
                        mHandler.sendMessage(msg);
                    } else {
                        scrollToLeftLayout();
                        Message msg = new Message();
                        msg.what = MessageFlag.SHOW_SLIDE;
                        mHandler.sendMessage(msg);
                    }
                }

                recycleVelocityTracker();
                break;
        }
        return isBindBasicLayout();
    }

    /**
     * �жϵ�ǰ���Ƶ���ͼ�ǲ�������ʾ�Ҳ಼�֡������ָ�ƶ��ľ����Ǹ������ҵ�ǰ��಼���ǿɼ��ģ�����Ϊ��ǰ��������Ҫ��ʾ�Ҳ಼�֡�
     * 
     * @return ��ǰ��������ʾ�Ҳ಼�ַ���true�����򷵻�false��
     */
    private boolean wantToShowRightLayout() {
        return xUp - xDown < 0 && isLeftLayoutVisible;
    }

    /**
     * �жϵ�ǰ���Ƶ���ͼ�ǲ�������ʾ��಼�֡������ָ�ƶ��ľ������������ҵ�ǰ��಼���ǲ��ɼ��ģ�����Ϊ��ǰ��������Ҫ��ʾ��಼�֡�
     * 
     * @return ��ǰ��������ʾ��಼�ַ���true�����򷵻�false��
     */
    private boolean wantToShowLeftLayout() {
        return xUp - xDown > 0 && !isLeftLayoutVisible;
    }

    /**
     * �ж��Ƿ�Ӧ�ù�������಼��չʾ�����������ָ�ƶ����������Ļ��1/2��������ָ�ƶ��ٶȴ���SNAP_VELOCITY��
     * ����ΪӦ�ù�������಼��չʾ������
     * 
     * @return ���Ӧ�ù�������಼��չʾ��������true�����򷵻�false��
     */
    private boolean shouldScrollToLeftLayout() {
        return xUp - xDown > screenWidth / 2
                || getScrollVelocity() > SNAP_VELOCITY;
    }

    /**
     * �ж��Ƿ�Ӧ�ù������Ҳ಼��չʾ�����������ָ�ƶ��������leftLayoutPadding������Ļ��1/2��
     * ������ָ�ƶ��ٶȴ���SNAP_VELOCITY�� ����ΪӦ�ù������Ҳ಼��չʾ������
     * 
     * @return ���Ӧ�ù������Ҳ಼��չʾ��������true�����򷵻�false��
     */
    private boolean shouldScrollToContent() {
        return xDown - xUp + leftLayoutPadding > screenWidth / 2
                || getScrollVelocity() > SNAP_VELOCITY;
    }

    /**
     * �жϰ󶨻����¼���View�ǲ���һ������layout����֧���Զ���layout��ֻ֧�����ֻ���layout,
     * AbsoluteLayout�ѱ����á�
     * 
     * @return ����󶨻����¼���View��LinearLayout,RelativeLayout,FrameLayout,
     *         TableLayout֮һ�ͷ���true�����򷵻�false��
     */
    private boolean isBindBasicLayout() {
        if (mBindView == null) {
            return false;
        }
        String viewName = mBindView.getClass().getName();
        return viewName.equals(LinearLayout.class.getName())
                || viewName.equals(RelativeLayout.class.getName())
                || viewName.equals(FrameLayout.class.getName())
                || viewName.equals(TableLayout.class.getName());
    }

    /**
     * ����VelocityTracker���󣬲��������¼����뵽VelocityTracker���С�
     * 
     * @param event
     *            �Ҳ಼�ּ����ؼ��Ļ����¼�
     */
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * ��ȡ��ָ���Ҳ಼�ֵļ���View�ϵĻ����ٶȡ�
     * 
     * @return �����ٶȣ���ÿ�����ƶ��˶�������ֵΪ��λ��
     */
    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();
        return Math.abs(velocity);
    }

    /**
     * ����VelocityTracker����
     */
    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    class ScrollTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... speed) {
            int leftMargin = leftLayoutParams.leftMargin;
            // ���ݴ�����ٶ����������棬������������߽���ұ߽�ʱ������ѭ����
            while (true) {
                leftMargin = leftMargin + speed[0];
                if (leftMargin > rightEdge) {
                    leftMargin = rightEdge;
                    break;
                }
                if (leftMargin < leftEdge) {
                    leftMargin = leftEdge;
                    break;
                }
                publishProgress(leftMargin);
                // Ϊ��Ҫ�й���Ч��������ÿ��ѭ��ʹ�߳�˯��20���룬�������۲��ܹ���������������
                sleep(20);
            }
            if (speed[0] > 0) {
                isLeftLayoutVisible = true;
            } else {
                isLeftLayoutVisible = false;
            }
            return leftMargin;
        }

        @Override
        protected void onProgressUpdate(Integer... leftMargin) {
            leftLayoutParams.leftMargin = leftMargin[0];
            leftLayout.setLayoutParams(leftLayoutParams);
        }

        @Override
        protected void onPostExecute(Integer leftMargin) {
            leftLayoutParams.leftMargin = leftMargin;
            leftLayout.setLayoutParams(leftLayoutParams);
        }
    }

    /**
     * ʹ��ǰ�߳�˯��ָ���ĺ�������
     * 
     * @param millis
     *            ָ����ǰ�߳�˯�߶�ã��Ժ���Ϊ��λ
     */
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
