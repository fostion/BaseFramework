package cn.base.demo.greendaodemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;
import cm.base.framework.bean.Student;
import cm.base.framework.service.DBHelper;
import cn.base.demo.R;

/**
 * 数据库demo
 */

public class GreenDaoActivity extends Activity implements View.OnClickListener {

    private TextView tvText;
    private Button btnAdd,btnUpdate,btnDel;
    public static int i;
    private List<Student> mStudentList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green_dao);

        tvText = (TextView) findViewById(R.id.tvText);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnDel = (Button) findViewById(R.id.btnDel);

        btnAdd.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnDel.setOnClickListener(this);

        update();
    }

    @Override
    public void onClick(View view) {
        if (btnAdd == view) {
            Student student = new Student(null, "小"+i, i, i%2, i%2 == 0 ? "软件工程":"网络工程", "新的备注");
            DBHelper.getInstance().getDaoSession().getStudentDao().insert(student);
            update();
        } else if (btnUpdate == view) {
            update();
        } else if (btnDel == view) {
            if (mStudentList == null || mStudentList.isEmpty()) {
                return;
            }
            DBHelper.getInstance().getDaoSession().getStudentDao().delete(mStudentList.get(0));
            update();
        }
    }

    private void update() {
        mStudentList = DBHelper.getInstance().getDaoSession().getStudentDao().loadAll();
        if (mStudentList == null || mStudentList.isEmpty()) {
            i = 0;
            tvText.setText("");
        } else {
            i = mStudentList.size();
            StringBuilder strBuilder = new StringBuilder();
            for (Student student : mStudentList) {
                if (student == null) {
                    continue;
                }
                strBuilder.append(student.toString()).append("\n");
            }
            tvText.setText(strBuilder.toString());
        }
    }
}
