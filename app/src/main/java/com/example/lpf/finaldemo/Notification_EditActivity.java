package com.example.lpf.finaldemo;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import net.lemonsoft.lemonbubble.LemonBubble;
import net.lemonsoft.lemonbubble.enums.LemonBubbleLayoutStyle;
import net.lemonsoft.lemonbubble.enums.LemonBubbleLocationStyle;
import net.lemonsoft.lemonhello.LemonHello;
import net.lemonsoft.lemonhello.LemonHelloAction;
import net.lemonsoft.lemonhello.LemonHelloInfo;
import net.lemonsoft.lemonhello.LemonHelloView;
import net.lemonsoft.lemonhello.interfaces.LemonHelloActionDelegate;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fangqizhan on 2018/1/8.
 */

public class Notification_EditActivity extends AppCompatActivity {
    private String account;
    private int notify_id;
    private Button confirm;
    private EditText content;
    private String objid;
    private int obj_id;
    private CardView c1;
    private CardView c2;
    DBUtil dbUtil;
    private final Notification_EditActivity.MyHandler mHandler = new Notification_EditActivity.MyHandler(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.notification_layout);
        account = getIntent().getStringExtra("a_account");
        objid = getIntent().getStringExtra("building_id");
        obj_id = Integer.parseInt(objid);
        content = (EditText)findViewById(R.id.noti_content);
        confirm = (Button)findViewById(R.id.send);
        c1 = (CardView)findViewById(R.id.notifycardview);
        c2 = (CardView)findViewById(R.id.notifycardview2);
        content.setCursorVisible(false);
        content.setOnClickListener(getClickEvent());
        confirm.setOnClickListener(getClickEvent());
    }

    private View.OnClickListener getClickEvent(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == confirm){
                    if(content.getText().toString().isEmpty()){
                        LemonHello.getErrorHello("错误","请输入通知内容")
                                .addAction(new LemonHelloAction("关闭", new LemonHelloActionDelegate() {
                                    @Override
                                    public void onClick(LemonHelloView lemonHelloView, LemonHelloInfo lemonHelloInfo, LemonHelloAction lemonHelloAction) {
                                        lemonHelloView.hide();
                                    }
                                }))
                                .show(Notification_EditActivity.this);
                    }
                    else onConfirm();
                }else if(view == content){
                    content.setCursorVisible(true);
                }else if (view == c1 || view == c2){
                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(),0);
                }
            }
        };
    }
    private void onConfirm(){
        LemonHello.getInformationHello("确认发布","您是否确认发布通知？")
                .addAction(new LemonHelloAction("取消", new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView lemonHelloView, LemonHelloInfo lemonHelloInfo, LemonHelloAction lemonHelloAction) {
                        lemonHelloView.hide();
                    }
                }))
                .addAction(new LemonHelloAction("发布", Color.RED, new LemonHelloActionDelegate() {
                    @Override
                    public void onClick(LemonHelloView lemonHelloView, LemonHelloInfo lemonHelloInfo, LemonHelloAction lemonHelloAction) {
                        lemonHelloView.hide();
                        LemonBubble.getRoundProgressBubbleInfo()
                                .setLocationStyle(LemonBubbleLocationStyle.CENTER)
                                .setLayoutStyle(LemonBubbleLayoutStyle.ICON_LEFT_TITLE_RIGHT)
                                .setBubbleSize(200,50)
                                .setTitle("正在发布...")
                                .show(Notification_EditActivity.this);
                    uploadInfo();
                    }
                }))
                .show(Notification_EditActivity.this);
    }
    private void uploadInfo(){
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("发布...");
                dbUtil.notification(content.getText().toString(),obj_id);
                System.out.println("发布sucess");
                Message msg = new Message();
                msg.what = 1002;
                mHandler.sendMessage(msg);
            }
        });
        thread.start();
    }
    private class MyHandler extends Handler {
        private final WeakReference<Notification_EditActivity> mActivity;
        public MyHandler(Notification_EditActivity activity){
            mActivity = new WeakReference<Notification_EditActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Notification_EditActivity activity = mActivity.get();
            if(activity != null){
                switch (msg.what){
                    case 1002:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                LemonBubble.showRight(Notification_EditActivity.this,"发布成功",2000);
                            }
                        },1500);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
