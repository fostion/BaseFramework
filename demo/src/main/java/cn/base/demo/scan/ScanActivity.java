package cn.base.demo.scan;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import cn.base.demo.R;

/**
 * 端口使用检测
 */

public class ScanActivity extends Activity {

    private final int FLAG_UPDATE = 1;
    private final int FLAG_PORT = 2;
    Button btn;
    TextView content;
    private int startPort = 0;
    private int endPort = 100000;
    private int curPort = 1;
    private List<ResultInfo> list = new ArrayList<>();
    private ExecutorService mCacheThreadPool = Executors.newFixedThreadPool(25);
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == FLAG_UPDATE){
                content.setText("");
                StringBuilder strBuilder = new StringBuilder();
                for (int i=0;i<list.size();i++){
                    ResultInfo info = list.get(i);
                    strBuilder.append("端口：").append(info.getPort()).append("\n");
                }
                content.setText(strBuilder);
                handler.sendEmptyMessageDelayed(FLAG_UPDATE, 5000);
            } else if (msg.what == FLAG_PORT){
                startItemPort();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        btn = (Button)findViewById(R.id.btn);
        content = (TextView)findViewById(R.id.content);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTask();
            }
        });
    }

    private void startTask() {
        for(int i = startPort, size = startPort + 25; i<size;i++){
            curPort = i;
            mCacheThreadPool.execute(new Task(i));
        }
        handler.sendEmptyMessageDelayed(FLAG_UPDATE, 5000);
    }

    private synchronized void startItemPort(){
        if (curPort > endPort){
            return;
        }
        curPort++;
        mCacheThreadPool.execute(new Task(curPort));
    }

    public void addResultInfo(ResultInfo info){
        synchronized (list){
            if (info.isUse){
                list.add(info);
            }
            handler.sendEmptyMessage(FLAG_PORT);
        }
    }

    //任务
    class Task implements Runnable {

        private int port = 1;

        public Task(int port) {
            this.port = port;
        }

        @Override
        public void run() {
            ResultInfo info = new ResultInfo(false, port);
            try {
                Log.i("test_port", "测试端口:"+port);
                Socket socket = new Socket();
                InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", port);
                socket.connect(socketAddress, 3000);
                socket.close();
                info.setUse(true);
                addResultInfo(info);
            } catch (Exception e){
                e.printStackTrace();
                addResultInfo(info);
            }
        }
    }

    class ResultInfo{
        private boolean isUse = false;
        private int port = 0;

        public ResultInfo(boolean isUse, int port) {
            this.isUse = isUse;
            this.port = port;
        }

        public boolean isUse() {
            return isUse;
        }

        public void setUse(boolean use) {
            isUse = use;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }
}
