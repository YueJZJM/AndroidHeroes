package com.example.systemwidget;

import android.content.Context;
import android.support.v4.view.ViewGroupCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Scroller;

public class MyScrollView extends ViewGroup {
    // 屏幕高度
    private int mScreenHeight;
    private Scroller mScroller;
    private int mLastY;
    // 手指触碰起始点
    private int mStart = 0;
    // 手指触碰终点
    private int mEnd;

    public MyScrollView(Context context) {
        this(context, null);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MyScrollView(Context context, AttributeSet attrs,
                        int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        // 获取窗口管理器
        WindowManager wm = (WindowManager) context.getSystemService(
                Context.WINDOW_SERVICE);
        // 新建一个DisplayMetrics
        DisplayMetrics dm = new DisplayMetrics();
        // 将度量标准设置为窗口管理器的默认显示
        wm.getDefaultDisplay().getMetrics(dm);
        // 获取屏幕的宽度并赋值给mScreenHeight
        mScreenHeight = dm.heightPixels;
        // 实例化一个滚动类Scroller
        mScroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            // 遍历查找所有的子view
            View childView = getChildAt(i);
            // 测量所有子view的宽、高
            measureChild(childView,widthMeasureSpec,heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        // 设置ViewGroup的高度
        MarginLayoutParams mlp = (MarginLayoutParams) getLayoutParams();
        mlp.height = mScreenHeight * childCount;
        setLayoutParams(mlp);
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                child.layout(l,i * mScreenHeight,r,(i + 1)  * mScreenHeight);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 获得最左上角的坐标相对于 view 刚显示出来原点的位置
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录本次左上角的坐标，为下次动作做准备
                mLastY = y;
                // 获取初始的y轴坐标
                mStart = getScrollY();

               // Log.d("MyScrollView", "mStart1:" + mStart);
                break;
            case MotionEvent.ACTION_MOVE:
                // 判断是否完成动作
                if (!mScroller.isFinished()) {
                    // 如果没有动作的话就立刻停止动画
                    mScroller.abortAnimation();
                }
                // 当前手指位置坐标减去没有动作时的坐标
                int dy = mLastY - y;// 滑动距离dy
                // 如果检测到坐标小于0，说明当前坐标是处于第一页，用户试图向下滑动
                if (getScaleY() < 0) {
                    // 直接将滑动距离置为0
                    dy = 0;
                }
                // 如果超过了父view的高度减去子view的高度的话（处于最后一页），并且用户试图向上滑动
                if (getScrollY() > getHeight() - mScreenHeight) {
                    // 直接将滑动距离置为0
                    dy = 0;
                }
                // 辅助滑动，执行滑动
                scrollBy(0,dy);
                // 记录左上角坐标，为下次动作做准备
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                int dScrollY = checkAlignment();
                // 判断滑动的方向
                if (dScrollY > 0) {
                    // 判断滑动距离是否符合翻页条件（滑动距离为屏幕的三分之一）
                    if (dScrollY < mScreenHeight / 3) {
                        // 不符合就回到初始位置
                        mScroller.startScroll(0, getScrollY(), 0, -dScrollY);
                    } else {
                        // 符合就向下滑动一整页
                        // 向下辅助滑动的距离需要将子view的高度减去用户已经滑动过的距离
                        mScroller.startScroll(0, getScrollY(), 0, mScreenHeight - dScrollY);
                    }
                } else {
                    // 如果滑动的距离小于0，即手指向上滑动(-dScrollY相当于滑动距离,因为dScrollY小于0)
                    if (-dScrollY < mScreenHeight / 3) {
                        // 滑动距离，计算要注意滑动距离为负数
                        mScroller.startScroll(0, getScrollY(), 0, -dScrollY);
                    } else {
                        mScroller.startScroll(0,getScrollY(),0,-mScreenHeight - dScrollY);
                    }
                }
                break;
        }
        // 刷新界面，区别于invalidate()
        postInvalidate();
        return true;
    }

    private int checkAlignment() {
        int mEnd = getScrollY();
      //  int a= getscrolly
       // int mEnd = eventY;
        boolean isUp = ((mEnd - mStart) > 0 )? true:false;
        Log.d("MyScrollView", "mEnd:" + mEnd);
        Log.d("MyScrollView", "mStart:" + mStart);
        int lastPrev = mEnd % mScreenHeight;
        int lastNext = mScreenHeight - lastPrev;
        Log.d("MyScrollView", "isUp:" + String.valueOf(isUp));
        if (isUp) {
            //向上的
            return lastPrev;
        }else {
            return -lastNext;
        }
    }

    //重写computeScroll（）方法，实现模拟滑动，系统在绘制view的时候，会在draw（）方法中调用该方法
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            postInvalidate();
        }
    }
}
