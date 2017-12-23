package com.example.cgz.bloodsoulprojects.webcomments;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Px;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.Scroller;

public class CustomScrollView extends FrameLayout {

    private static final String TAG = "CustomScrollView";

    private View mHeadView;
    private View mBottomView;
    private int mMinY = 0;
    private int mMaxY = 0;
    private float mDownX;
    private float mDownY;
    private float mLastY;
    private int mCurrentY;
    private int mTouchSlop;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    private Scroller mScroller;
    private DIRECTION mDirection;
    private VelocityTracker mVelocityTracker;
    private int mLastScrollerY;
    private boolean needCheckUpdown;
    private boolean updown;

    public CustomScrollView(Context context) {
        super(context);
        init(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mScroller = new Scroller(context);
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    private enum DIRECTION {
        UP,// 向上划
        DOWN// 向下划
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMaxY = mHeadView.getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mHeadView.layout(left, top, right, bottom);
        int webViewHeight = mHeadView.getHeight();
        mBottomView.layout(left, top + webViewHeight, right, bottom + webViewHeight);
    }

    @Override
    protected void onFinishInflate() {
        Log.i(TAG, "onFinishInflate");
        super.onFinishInflate();
        mHeadView = getChildAt(0);
        mBottomView = getChildAt(1);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float currentX = event.getX();
        float currentY = event.getY();
        float deltaY;
        int shiftX = (int) Math.abs(currentX - mDownX);
        int shiftY = (int) Math.abs(currentY - mDownY);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                needCheckUpdown = true;
                updown = true;
                mDownX = currentX;
                mLastY = mDownY = currentY;
                initOrResetVelocityTracker(event);
                mScroller.forceFinished(true);
                break;
            case MotionEvent.ACTION_MOVE:
                initVelocityTrackerIfNotExists(event);
                deltaY = mLastY - currentY;
                if (needCheckUpdown) {
                    if (shiftX > mTouchSlop && shiftX > shiftY) {
                        needCheckUpdown = false;
                        updown = false;
                    } else if (shiftY > mTouchSlop && shiftY > shiftX) {
                        needCheckUpdown = false;
                        updown = true;
                    }
                }
                // y 滑动
                if (updown && shiftY > shiftX && shiftY > mTouchSlop) {
                    if (isStickedTop() && !isWebViewBottom()) {
                        // nothing
                    } else if (isStickedBottom() && !isRecyclerViewTop()) {
                        // nothing
                    } else {
                        scrollBy(0, (int) (deltaY + 0.5));
                    }
                }
                mLastY = currentY;
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "dispatchTouchEvent --> IRECTION.UP");
                Log.i(TAG, "dispatchTouchEvent --> IRECTION.UP  isStickedTop: " + isStickedTop() + ",  isWebViewBottom: " + isWebViewBottom());
                Log.i(TAG, "dispatchTouchEvent --> IRECTION.UP  isStickedBottom: " + isStickedBottom() + ",  isRecyclerViewTop: " + isRecyclerViewTop());
                if (updown && shiftY > shiftX && shiftY > mTouchSlop) {
                    int yVelocity = getScrollVelocityY();
                    boolean allowChild = false;
                    if (Math.abs(yVelocity) > mMinimumVelocity) {
                        mDirection = yVelocity > 0 ? DIRECTION.UP : DIRECTION.DOWN;
                        Log.i(TAG, "dispatchTouchEvent --> mDirection: " + mDirection);
                        if ((mDirection == DIRECTION.UP && isStickedBottom()) || (!isStickedBottom() && getScrollY() == 0 && mDirection == DIRECTION.DOWN)) {
                            Log.i(TAG, "dispatchTouchEvent dislowChild true");
                            allowChild = true;
                        } else if ((mDirection == DIRECTION.DOWN && isStickedTop()) || (!isStickedTop() && getScrollY() == -mMaxY && mDirection == DIRECTION.UP)) {
                            Log.i(TAG, "dispatchTouchEvent dislowChild true");
                            allowChild = true;
                        } else {
                            Log.i(TAG, "dispatchTouchEvent dislowChild false");
                            mScroller.fling(0, getScrollY(), 0, yVelocity, 0, 0, -Integer.MAX_VALUE, Integer.MAX_VALUE);
                            mScroller.computeScrollOffset();
                            mLastScrollerY = getScrollY();
                            invalidate();
                        }

                        if (!allowChild && !isStickedBottom() && !isStickedTop()) {
                            int action = event.getAction();
                            event.setAction(MotionEvent.ACTION_CANCEL);
                            boolean dispathResult = super.dispatchTouchEvent(event);
                            event.setAction(action);
                            return dispathResult;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                stopVelocityTracker();
        }
        super.dispatchTouchEvent(event);
        return true;
    }

    @Override
    public void scrollBy(int x, int y) {
        int scrollY = getScrollY();
        int toY = scrollY + y;
        if (toY >= mMaxY) {
            toY = mMaxY;
        } else if (toY <= mMinY) {
            toY = mMinY;
        }
        y = toY - scrollY;
        super.scrollBy(x, y);
    }

    @Override
    public void scrollTo(@Px int x, @Px int y) {
        if (y >= mMaxY) {
            y = mMaxY;
        } else if (y <= mMinY) {
            y = mMinY;
        }
        mCurrentY = y;
        super.scrollTo(x, y);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            int currY = mScroller.getCurrY();
            if (mDirection == DIRECTION.UP) {
                Log.i(TAG, "computeScroll --> IRECTION.UP");
                Log.i(TAG, "computeScroll --> IRECTION.UP  isStickedTop: " + isStickedTop() + ",  isWebViewBottom: " + isWebViewBottom());
                Log.i(TAG, "computeScroll --> IRECTION.UP  isStickedBottom: " + isStickedBottom() + ",  isRecyclerViewTop: " + isRecyclerViewTop());

                // 手势向上划
                if (isStickedBottom()) {
                    int distance = mScroller.getFinalY() - currY;
                    int duration = mScroller.getDuration() - mScroller.timePassed();
                    smoothScrollBy(mBottomView, getScrollerVelocity(distance, duration));
                    mScroller.forceFinished(true);
                    return;
                }

                if (isWebViewBottom()) {
                    int deltaY = (currY - mLastScrollerY);
                    int toY = getScrollY() + deltaY;
                    scrollTo(0, toY);
                    if (mCurrentY >= mMaxY) {
                        Log.i(TAG, "computeScroll --> mCurrentY >= mMaxY");
                        return;
                    }
                }
                invalidate();
            } else {
                Log.i(TAG, "computeScroll --> IRECTION.DOWN");
                Log.i(TAG, "computeScroll --> IRECTION.DOWN  isStickedTop: " + isStickedTop() + ",  isWebViewBottom: " + isWebViewBottom());
                Log.i(TAG, "computeScroll --> IRECTION.DOWN  isStickedBottom: " + isStickedBottom() + ",  isRecyclerViewTop: " + isRecyclerViewTop());

                if (isStickedTop()) {
                    int distance = mScroller.getFinalY() - currY;
                    int duration = mScroller.getDuration() - mScroller.timePassed();
                    Log.i(TAG, "computeScroll --> isStickedTop " + distance + ", " + duration + ", " + getScrollerVelocity(distance, duration));
                    smoothScrollBy(mHeadView, -getScrollerVelocity(distance, duration));
                    mScroller.forceFinished(true);
                    invalidate();
                    return;
                }

                // 手势向下划
                if (isRecyclerViewTop()) {
                    int deltaY = (currY - mLastScrollerY);
                    int toY = getScrollY() + deltaY;
                    scrollTo(0, toY);
                    if (mCurrentY <= mMinY) {
                        Log.i(TAG, "computeScroll --> mCurrentY <= mMinY");
                        return;
                    }
                }
                invalidate();
            }
            mLastScrollerY = currY;
            Log.i(TAG, "computeScroll --> mLastScrollerY : " + mLastScrollerY);
        }
    }

    private int getScrollerVelocity(int distance, int duration) {
        if (mScroller == null) {
            return 0;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return (int) mScroller.getCurrVelocity();
        } else {
            return distance / duration;
        }
    }

    public boolean isStickedTop() {
        return mCurrentY == 0;
    }

    public boolean isStickedBottom() {
        return mCurrentY == mMaxY;
    }

    private boolean isWebViewBottom(){
        WebView webView = (WebView) mHeadView;
        float webcontent = webView.getContentHeight() * webView.getScale();// webview的高度
        float webnow = webView.getHeight() + webView.getScrollY();// 当前webview的高度
        return (webcontent - webnow) == 0;
    }

    private boolean isRecyclerViewTop() {
        RecyclerView recyclerView = (RecyclerView) mBottomView;
        if (recyclerView != null) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                int firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                View childAt = recyclerView.getChildAt(0);
                if (childAt == null || (firstVisibleItemPosition == 0 &&
                        layoutManager.getDecoratedTop(childAt) == 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void initOrResetVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }
        mVelocityTracker.addMovement(event);
    }

    private void initVelocityTrackerIfNotExists(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    private void stopVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private int getScrollVelocityY() {
        if (mVelocityTracker != null) {
            // 设置单位, 1000表示1s内移动的像素值
            mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
            return -(int) mVelocityTracker.getYVelocity();
        } else {
            return 0;
        }
    }

    public void smoothScrollBy(View view, int velocityY) {
        if (view instanceof RecyclerView) {
            ((RecyclerView) view).fling(0, velocityY);
        } else if (view instanceof WebView) {
            ((WebView) view).flingScroll(0, velocityY);
        }
    }

}
