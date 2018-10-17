package cn.base.demo.tvrecyclerview;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.base.demo.R;

/**
 * Created by Administrator on 2018/10/12.
 */

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private int mItemCount;

    ListAdapter(Context context, int itemCount) {
        mContext = context;
        mItemCount = itemCount;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(View.inflate(mContext, R.layout.module_item_recyclerview, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.i("test_log", "adapter："+position);
        final RecyclerViewHolder viewHolder = (RecyclerViewHolder) holder;
        viewHolder.mName.setText(ContantUtil.TEST_DATAS[position]);
        GradientDrawable drawable =(GradientDrawable)viewHolder.mFrameLayout.getBackground();
        drawable.setColor(ContextCompat.getColor(mContext, ContantUtil.getRandColor(position)));
    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {

        FrameLayout mFrameLayout;
        TextView mName;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.tv_item_tip);
            mFrameLayout = (FrameLayout) itemView.findViewById(R.id.fl_main_layout);
        }
    }
}
