package cn.base.demo.tvrecyclerview;

import java.util.Random;

import cn.base.demo.R;

class ContantUtil {
    private static int[] mColorIds = {R.color.amber, R.color.brown, R.color.cyan,
            R.color.deepPurple, R.color.green, R.color.lightBlue, R.color.lightGreen,
            R.color.lime, R.color.orange, R.color.pink, R.color.cyan, R.color.deepPurple};

    static String[] TEST_DATAS = {"A", "B", "C", "D", "E", "F", "G",
    "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R"};

    static int getRandColor(int pos) {
        int curPos = pos % mColorIds.length;
        return mColorIds[curPos];
    }

}
