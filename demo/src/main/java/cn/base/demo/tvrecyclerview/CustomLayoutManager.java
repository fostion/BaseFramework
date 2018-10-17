package cn.base.demo.tvrecyclerview;

import android.graphics.Rect;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public abstract class CustomLayoutManager extends RecyclerView.LayoutManager {

    private static final int HORIZONTAL = OrientationHelper.HORIZONTAL;
    private static final int VERTICAL = OrientationHelper.VERTICAL;
    private int mVerticalOffset, mHorizontalOffset;//竖直横向偏移量，每次换行，要根据offset判断
    private int mOrientation = VERTICAL;
    // re-used variable to acquire decor insets from RecyclerView
    private final Rect mDecorInsets = new Rect();
    private int mSrcItemWidth = 160, mSrcItemHeight = 144;//原始item宽度和高度
    private int mTotalSize, mBaseItemCount;
    private final SparseArray<Rect> mItemsRect;

    public CustomLayoutManager() {
        mBaseItemCount = 4;
        mItemsRect = new SparseArray<>();
    }

    public void setOrientation(int orientation) {
        this.mOrientation = orientation;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    //界面更新都会调用
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        if (getItemCount() == 0) {
            //没有数据
            detachAndScrapAttachedViews(recycler);
        }

        if (getChildCount() == 0 && state.isPreLayout()) {
            //绘制之前
            return;
        }
        //onLayoutChildren方法在RecyclerView 初始化时，执行两次
        if (mOrientation == VERTICAL) {
            mSrcItemWidth = getDisplayRect().width() / 4;
        } else {
            mSrcItemHeight = getDisplayRect().height() / 4;
        }
        detachAndScrapAttachedViews(recycler);
        mVerticalOffset = 0;
        fill(recycler, state);
    }

    //item view填充，根据配置数据来判断
    private void fill(RecyclerView.Recycler recycler, RecyclerView.State state) {
        int itemsCount = state.getItemCount();
        Rect displayRect = getDisplayRect(); //获取绘制区域
        //首屏绘制
        for (int i = 0; i < itemsCount; i++) {
            Log.i("test_view", "itemCount:" + i);
            View child = recycler.getViewForPosition(i);
            Rect itemRect = calculateViewSizeByPosition(child, i);
            if (!Rect.intersects(displayRect, itemRect)) {
                recycler.recycleView(child);
                break;
            }
            addView(child);
            //caculate width includes margin
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
            layoutDecorated(child,
                    itemRect.left + lp.leftMargin,
                    itemRect.top + lp.topMargin,
                    itemRect.right - lp.rightMargin,
                    itemRect.bottom - lp.bottomMargin);
            if (mOrientation == HORIZONTAL) {
                mTotalSize = itemRect.right;
            } else {
                mTotalSize = itemRect.bottom;
            }

            Rect frame = mItemsRect.get(i);
            if (frame == null) {
                frame = new Rect();
            }
            frame.set(itemRect);
            mItemsRect.put(i, frame);
        }
    }

    //处理竖向滑动
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (dy == 0 || getChildCount() == 0) {
            return 0;
        }

        //实际滑动距离
        int realOffset = dy;
        int maxScrollSpace = mTotalSize - getVerticalSpace();

        if (mVerticalOffset + dy < 0) {
            if (Math.abs(dy) > mVerticalOffset) {
                realOffset = -mVerticalOffset;
            } else {
                realOffset -= mVerticalOffset;
            }
        } else if (mItemsRect.size() >= getItemCount() &&
                mVerticalOffset + dy > maxScrollSpace) {
            realOffset = maxScrollSpace - mVerticalOffset;
        }
        mVerticalOffset += realOffset;
        //对item进行滑动操作
        offsetChildrenVertical(-realOffset);
        if (mVerticalOffset != 0) {
            //填充新的item
            recycleAndFillItems(recycler, state, dy);
        }
        return dy;
    }

    //横向滑动
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (dx == 0 || getChildCount() == 0) {
            return 0;
        }
        int realOffset = dx;
        int maxScrollSpace = mTotalSize - getHorizontalSpace();
        if (mHorizontalOffset + dx < 0) {
            if (Math.abs(dx) > mHorizontalOffset) {
                realOffset = -mHorizontalOffset;
            } else {
                realOffset -= mHorizontalOffset;
            }
        } else if (mItemsRect.size() >= getItemCount() &&
                mHorizontalOffset + dx > maxScrollSpace) {
            realOffset = maxScrollSpace - mHorizontalOffset;
        }
        mHorizontalOffset += realOffset;
        offsetChildrenHorizontal(-realOffset);
        if (mHorizontalOffset != 0) {
            recycleAndFillItems(recycler, state, dx);
        }
        return realOffset;
    }

    private void recycleAndFillItems(RecyclerView.Recycler recycler, RecyclerView.State state, int dt) {
        if (state.isPreLayout()) {
            return;
        }
        //回收失效的item
        recycleByScrollState(recycler, dt);

        if (dt > 0) {
            //往下滑动
            int beginPos = findLastViewLayoutPosition() + 1;
            fillRequireItems(recycler, beginPos);
        } else {
            //选到完全展示的view开始移动
            int endPos = findFirstCompleteVisibleItemPosition();
            Log.i("test_view", "往上滑动  endPos:"+endPos);
            for (int i = endPos; i >= 0; i--) {
                Rect frame = mItemsRect.get(i);
                if (Rect.intersects(getDisplayRect(), frame)) {
                    //滑动到当前展示item
                    if (findViewByPosition(i) != null) {
                        continue;
                    }
                    Log.i("test_view", "position:"+i);

                    View scrap = recycler.getViewForPosition(i);
                    addView(scrap, 0);
                    measureChild(scrap, getItemWidth(i), getItemHeight(i));
                    RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) scrap.getLayoutParams();
                    if (mOrientation == HORIZONTAL) {
                        layoutDecorated(scrap,
                                frame.left + lp.leftMargin - mHorizontalOffset,
                                frame.top + lp.topMargin,
                                frame.right - lp.rightMargin - mHorizontalOffset,
                                frame.bottom - lp.bottomMargin);
                    } else {
                        layoutDecorated(scrap,
                                frame.left + lp.leftMargin,
                                frame.top + lp.topMargin - mVerticalOffset,
                                frame.right - lp.rightMargin,
                                frame.bottom - lp.bottomMargin - mVerticalOffset);
                    }
                }
            }
        }

    }

    private void fillRequireItems(RecyclerView.Recycler recycler, int beginPos) {
        int itemCount = getItemCount();
        Rect displayRect = getDisplayRect();
        int rectCount;
        for (int i = beginPos; i < itemCount; i++) {
            rectCount = mItemsRect.size();
            Rect frame = mItemsRect.get(i);
            if (i < rectCount && frame != null) {
                //已经显示的item，只需要修改位置
                if (Rect.intersects(displayRect, frame)) {
                    View scrap = recycler.getViewForPosition(i);
                    addView(scrap);
                    measureChild(scrap, getItemWidth(i), getItemHeight(i));
                    RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) scrap.getLayoutParams();
                    if (mOrientation == HORIZONTAL) {
                        layoutDecorated(scrap,
                                frame.left + lp.leftMargin - mHorizontalOffset,
                                frame.top + lp.topMargin,
                                frame.right - lp.rightMargin - mHorizontalOffset,
                                frame.bottom - lp.bottomMargin);
                    } else {
                        layoutDecorated(scrap,
                                frame.left + lp.leftMargin,
                                frame.top + lp.topMargin - mVerticalOffset,
                                frame.right - lp.rightMargin,
                                frame.bottom - lp.bottomMargin - mVerticalOffset);
                    }
                }
            } else if (rectCount < itemCount) {
                //没有绘制的item
                View child = recycler.getViewForPosition(i);
                Rect itemRect = calculateViewSizeByPosition(child, i);
                if (!Rect.intersects(displayRect, itemRect)) {
                    recycler.recycleView(child);
                    return;
                }
                addView(child);
                RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
                if (mOrientation == HORIZONTAL) {
                    layoutDecorated(child,
                            itemRect.left + lp.leftMargin - mHorizontalOffset,
                            itemRect.top + lp.topMargin,
                            itemRect.right - lp.rightMargin - mHorizontalOffset,
                            itemRect.bottom - lp.bottomMargin);
                    mTotalSize = itemRect.right;
                } else {
                    layoutDecorated(child,
                            itemRect.left + lp.leftMargin,
                            itemRect.top + lp.topMargin - mVerticalOffset,
                            itemRect.right - lp.rightMargin,
                            itemRect.bottom - lp.bottomMargin - mVerticalOffset);
                    mTotalSize = itemRect.bottom;
                }

                if (frame == null) {
                    frame = new Rect();
                }
                frame.set(itemRect);
                mItemsRect.put(i, frame);
            }
        }
    }

    //获取完全展示的item
    int findFirstCompleteVisibleItemPosition() {
        int childCount = getChildCount();
        if (childCount <= 0) {
            return -1;
        }
        View child = null;
        for (int i=0; i<childCount; i++) {
            View view = getChildAt(i);
            int startPos = getDecoratedStart(view);
            if (startPos > 0) {
                child = view;
                break;
            }
        }
        return child == null ? -1 : getPosition(child);
    }

    /**
     * Returns the adapter position of the first visible view. This position does not include
     * adapter changes that were dispatched after the last layout pass.
     *
     * @return the first visible item position or -1
     */
    int findFirstVisibleItemPosition() {
        final View child = findOneVisibleChild(0, getChildCount(), false, true);
        return child == null ? -1 : getPosition(child);
    }

    private View findOneVisibleChild(int fromIndex, int toIndex, boolean completelyVisible,
                                     boolean acceptPartiallyVisible) {
        final int start = getPaddingLeft();
        final int end = getHeight() - getPaddingBottom();
        final int next = toIndex > fromIndex ? 1 : -1;
        View partiallyVisible = null;
        for (int i = fromIndex; i != toIndex; i += next) {
            final View child = getChildAt(i);
            final int childStart = getDecoratedStart(child);
            final int childEnd = getDecoratedEnd(child);
            if (childStart < end && childEnd > start) {
                if (completelyVisible) {
                    if (childStart >= start && childEnd <= end) {
                        return child;
                    } else if (acceptPartiallyVisible && partiallyVisible == null) {
                        partiallyVisible = child;
                    }
                } else {
                    return child;
                }
            }
        }
        return partiallyVisible;
    }

    private int getDecoratedStart(View view) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                view.getLayoutParams();
        return getDecoratedTop(view) - params.topMargin;
    }

    private int getDecoratedEnd(View view) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                view.getLayoutParams();
        return getDecoratedBottom(view) + params.bottomMargin;
    }

    private int findLastViewLayoutPosition() {
        int lastPos = getChildCount();
        if (lastPos > 0) {
            lastPos = getPosition(getChildAt(lastPos - 1));
        }
        return lastPos;
    }

    private boolean recycleByScrollState(RecyclerView.Recycler recycler, int dt) {
        int childCount = getChildCount();
        ArrayList<Integer> recycleIndexList = new ArrayList<>();
        Rect displayRect = getDisplayRect();
        if (dt >= 0) {
            //往下滑动
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                int pos = getPosition(child);
                Rect frame = mItemsRect.get(pos);
                //若是没有在显示范围内，则认为需要回收
                if (!Rect.intersects(displayRect, frame)) {
                    recycleIndexList.add(i);
                }
            }

        } else {
            //往上滑动
            for (int i = childCount - 1; i >= 0; i--) {
                View child = getChildAt(i);
                int pos = getPosition(child);
                Rect frame = mItemsRect.get(pos);
                //若是没有在显示范围内，则认为需要回收
                if (!Rect.intersects(displayRect, frame)) {
                    recycleIndexList.add(i);
                }
            }
        }

        if (recycleIndexList.size() > 0) {
            recycleChildren(recycler, dt, recycleIndexList);
            return true;
        }
        return false;
    }


    /**
     * Recycles children between given index.
     *
     * @param dt               direction
     * @param recycleIndexList save need recycle index
     */
    private void recycleChildren(RecyclerView.Recycler recycler, int dt,
                                 ArrayList<Integer> recycleIndexList) {
        int size = recycleIndexList.size();
        if (dt < 0) {
            for (int i = 0; i < size; i++) {
                int pos = recycleIndexList.get(i);
                removeAndRecycleViewAt(pos, recycler);
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                int pos = recycleIndexList.get(i);
                removeAndRecycleViewAt(pos, recycler);
            }
        }
    }

    private Rect getDisplayRect() {
        Rect displayFrame;
        if (mOrientation == HORIZONTAL) {
            displayFrame = new Rect(mHorizontalOffset - getPaddingLeft(), 0,
                    mHorizontalOffset + getHorizontalSpace() + getPaddingRight(), getVerticalSpace());
        } else {
            displayFrame = new Rect(0, mVerticalOffset - getPaddingTop(),
                    getHorizontalSpace(), mVerticalOffset + getVerticalSpace() + getPaddingBottom());
        }
        return displayFrame;
    }

    private int getVerticalSpace() {
        int space = getHeight() - getPaddingTop() - getPaddingBottom();
        return space <= 0 ? getMinimumHeight() : space;
    }

    private int getHorizontalSpace() {
        int space = getWidth() - getPaddingLeft() - getPaddingRight();
        return space <= 0 ? getMinimumWidth() : space;
    }

    //根据位置，配置显示
    private Rect calculateViewSizeByPosition(View child, int position) {
        if (position >= getItemCount()) {
            throw new IllegalArgumentException("position outside of itemCount position is "
                    + position + " itemCount is " + getItemCount());
        }
        int leftOffset = 0;//左间距
        int topOffset = 0;//右间距
        Rect childFrame = new Rect();

        //计算间隔
        calculateItemDecorationsForChild(child, mDecorInsets);
        measureChild(child, getItemWidth(position), getItemHeight(position));

        int childHorizontalSpace = getDecoratedMeasurementHorizontal(child);
        int childVerticalSpace = getDecoratedMeasurementVertical(child);
        int itemStartPos = getItemStartPos(position);
        //todo 计算位置
        int lastPos;
        int topPos;
        if (mOrientation == HORIZONTAL) {
            lastPos = itemStartPos / mBaseItemCount;
            topPos = itemStartPos % mBaseItemCount;
        } else {
            lastPos = itemStartPos % mBaseItemCount;
            topPos = itemStartPos / mBaseItemCount;
        }

        if (lastPos == 0) {
            leftOffset = -mDecorInsets.left;
        } else {
            leftOffset = (mSrcItemWidth + getChildHorizontalPadding(child)) * lastPos
                    - mDecorInsets.left;
        }

        if (topPos == 0) {
            topOffset = -mDecorInsets.top;
        } else {
            topOffset = (mSrcItemHeight + getChildVerticalPadding(child)) * topPos
                    - mDecorInsets.top;
        }


        childFrame.left = leftOffset;
        childFrame.right = leftOffset + childHorizontalSpace;
        childFrame.top = topOffset;
        childFrame.bottom = topOffset + childVerticalSpace;
        return childFrame;
    }

    private int getChildHorizontalPadding(View child) {
        final RecyclerView.MarginLayoutParams params = (RecyclerView.LayoutParams)
                child.getLayoutParams();
        return getDecoratedMeasuredWidth(child) + params.leftMargin
                + params.rightMargin - child.getMeasuredWidth();
    }

    private int getChildVerticalPadding(View child) {
        final RecyclerView.MarginLayoutParams params = (RecyclerView.LayoutParams)
                child.getLayoutParams();
        return getDecoratedMeasuredHeight(child) + params.topMargin
                + params.bottomMargin - child.getMeasuredHeight();
    }


    //计算view的宽度
    public void measureChild(View child, int childWidth, int childHeight) {
        final RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();

        calculateItemDecorationsForChild(child, mDecorInsets);

        final int widthSpec = getChildMeasureSpec(getWidth(), getWidthMode()
                , getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin, childWidth, canScrollHorizontally());
        final int heightSpec = getChildMeasureSpec(getHeight(), getHeightMode()
                , getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin, childHeight, canScrollVertically());
        child.measure(widthSpec, heightSpec);
    }

    @Override
    public boolean canScrollHorizontally() {
        return mOrientation == HORIZONTAL;
    }

    @Override
    public boolean canScrollVertically() {
        return mOrientation == VERTICAL;
    }

    //计算view的宽度
    private int getDecoratedMeasurementHorizontal(View child) {
        final RecyclerView.MarginLayoutParams params = (RecyclerView.LayoutParams)
                child.getLayoutParams();
        return getDecoratedMeasuredWidth(child) + params.leftMargin
                + params.rightMargin;
    }

    //计算view的高度
    private int getDecoratedMeasurementVertical(View view) {
        final RecyclerView.MarginLayoutParams params = (RecyclerView.LayoutParams)
                view.getLayoutParams();
        return getDecoratedMeasuredHeight(view) + params.topMargin
                + params.bottomMargin;
    }

    //获取item宽度
    private int getItemWidth(int position) {
        int itemColumnSize = getItemRowSize(position);
        return itemColumnSize * mSrcItemWidth
                + (itemColumnSize - 1) * (mDecorInsets.left + mDecorInsets.right);
    }

    //获取高度
    private int getItemHeight(int position) {
        int itemRowSize = getItemColumnSize(position);
        return itemRowSize * mSrcItemHeight
                + (itemRowSize - 1) * (mDecorInsets.bottom + mDecorInsets.top);
    }

    public abstract int getItemStartPos(int position);

    public abstract int getItemRowSize(int position);

    public abstract int getItemColumnSize(int position);

}
