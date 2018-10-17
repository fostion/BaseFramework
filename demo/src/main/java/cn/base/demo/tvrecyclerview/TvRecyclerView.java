package cn.base.demo.tvrecyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import static android.view.KeyEvent.KEYCODE_DPAD_RIGHT;

/**
 * TvRecyclerView
 */

public class TvRecyclerView extends RecyclerView {
    private int mSelectedPosition;
    private View mSelectView;

    public TvRecyclerView(Context context) {
        this(context, null);
    }

    public TvRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TvRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setChildrenDrawingOrderEnabled(true);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        //处理绘制顺序，让焦点item放大后可以盖在其他item上面
        int focusIndex = indexOfChild(mSelectView);
        if (focusIndex < 0) {
            return i;
        }
        if (i < focusIndex) {
            return i;
        } else if (i < childCount - 1) {
            return focusIndex + childCount - 1 - i;
        } else {
            return focusIndex;
        }
    }

    @Override
    protected void onFocusChanged(boolean gainFocus,int direction, @Nullable Rect previouslyFocusedRect) {
        if (gainFocus) {
            mSelectView = getFocusedChild();
            mSelectedPosition = getChildAdapterPosition(mSelectView);
        }

        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        View nexFocusView = null;
        View selectView = getFocusedChild();
        int dx = 0;
        int dy = 0;
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            dx = -selectView.getWidth();
            dy = 0;
            nexFocusView = FocusFinder.getInstance().findNextFocus(this, selectView, View.FOCUS_LEFT);
        } else if (keyCode == KEYCODE_DPAD_RIGHT) {
            dx = selectView.getWidth();
            dy = 0;
            nexFocusView = FocusFinder.getInstance().findNextFocus(this, selectView, View.FOCUS_RIGHT);
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            dx = 0;
            dy = -selectView.getHeight();
            nexFocusView = FocusFinder.getInstance().findNextFocus(this, selectView, View.FOCUS_UP);
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            dx = 0;
            dy = selectView.getHeight();
            nexFocusView = FocusFinder.getInstance().findNextFocus(this, selectView, View.FOCUS_DOWN);
        }

        //避免执行两次
        if (event.getAction() == KeyEvent.ACTION_UP) {
            return false;
        }

        if (nexFocusView == null) {
            super.smoothScrollBy(dx, dy);
        } else {
            Log.i("test_view", "执行动画逻辑");
            mSelectView = nexFocusView;
            mSelectedPosition = getChildAdapterPosition(mSelectView);
            stopAnimation(selectView);
            startAnimation(nexFocusView);
            invalidate();
        }
        return super.dispatchKeyEvent(event);
    }


    private void startAnimation(View view) {
        if (view == null) {
            return;
        }
        view.animate().scaleX(1.06f).scaleY(1.06f).setDuration(200).start();
    }

    private void stopAnimation(View view) {
        if (view == null) {
            return;
        }
        view.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start();
    }
}
