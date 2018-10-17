package cn.base.demo.tvrecyclerview;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import cn.base.demo.R;

/**
 * DemoActivity
 */

public class DemoActivity extends Activity {

    TvRecyclerView list;
    int[] startPosArray = new int[]{0, 2, 6, 8, 9, 10, 11, 12, 20};
    int[] itemRowArray = new int[]{2, 2, 2, 1, 1, 1, 1, 4, 1};
    int[] itemColumnArray = new int[]{2, 1, 1, 1, 1, 1, 1, 2, 1};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvrecyclerview);


        list = (TvRecyclerView) findViewById(R.id.list);
        ListAdapter listAdapter = new ListAdapter(this, 9);
        CustomLayoutManager customLayoutManager = new CustomLayoutManager() {

            @Override
            public int getItemStartPos(int position) {
                return startPosArray[position];
            }

            @Override
            public int getItemRowSize(int position) {
                return itemRowArray[position];
            }

            @Override
            public int getItemColumnSize(int position) {
                Log.i("test_view", "position:"+position);
                return itemColumnArray[position];
            }
        };
        customLayoutManager.setOrientation( OrientationHelper.VERTICAL);
        list.addItemDecoration(new SpaceItemDecoration());
        list.setLayoutManager(customLayoutManager);
        list.setAdapter(listAdapter);

        list.post(new Runnable() {
            @Override
            public void run() {
                list.getChildAt(0).requestFocus();
            }
        });
    }

    private class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private int space = 8;

        SpaceItemDecoration() {
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.left = space;
            outRect.bottom = space;
        }
    }
}
