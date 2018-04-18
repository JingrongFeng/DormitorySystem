package com.example.lpf.finaldemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.lemonsoft.lemonbubble.LemonBubble;

import java.util.ArrayList;
import java.util.List;

public class student_main_page extends AppCompatActivity {

    private ArrayList<String> stuInfo;//存储学生信息
    private TextView stuName;//学生姓名
    private TextView duildingname;//宿舍楼名
    private TextView dor_no;//宿舍号
    private ImageView profile;
    private byte[] pic;
    private ImageView btn_update_n;//更新通知按钮
    private ImageView btn_update_w;//更新订水按钮

    private static MyListView n_listView;//通知列表
    private static MyListView w_listView;//订水列表
    private List<item> notification_list = new ArrayList<item>();//通知list数据
    private ItemAdapter notification_adapter;
    private List<item> water_list = new ArrayList<item>();//订水list数据
    private ItemAdapter water_adapter;

    private Button show_dorm;
    private Button repair;
    private Button order_water;
    private Button edit_information;

    private String account;
    private String buildingId;
    private String dormId;

    private Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            System.out.println("msg.what:"+Integer.toString(msg.what));
            switch(msg.what)
            {
                case 1:
                    //ArrayList<String>temp = (ArrayList<String>) msg.obj;
                    //String str = msg.obj.toString();
                    stuName.setText(stuInfo.get(0));
                    duildingname.setText(stuInfo.get(12));
                    dor_no.setText(stuInfo.get(13));
                    update_notification();
                    update_orderWater();
                    break;
                case 2:
                    //pic =(byte[]) msg.obj;
                    if (pic!=null) {
                        Bitmap btm = Bytes2Bimap(pic);
                        profile.setImageBitmap(btm);//加载头像
                    }
                    break;
                case 3://更新通知表
                    notification_adapter.notifyDataSetChanged();
                    break;
                case 4://更新订水表
                    water_adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_main_page);
        Intent intent =getIntent();
        account = intent.getStringExtra("account");
        initial();
        loadInfo();
        //w_listView.setAdapter(water_adapter);
        //n_listView.setAdapter(notification_adapter);
        //w_listView.setAdapter(water_adapter);
        setOnClickListener();
    }

    @Override
    protected void onResume( ){
        super.onResume();
        System.out.println("onResume");
        loadInfo();
    }

    private void initial()
    {
        stuName = (TextView) findViewById(R.id.stuName);
        duildingname =(TextView) findViewById(R.id.buildingName);
        dor_no =(TextView) findViewById(R.id.dormName);
        profile=(ImageView) findViewById(R.id.imageView2);
        btn_update_n =(ImageView) findViewById(R.id.btn_update3);
        btn_update_w =(ImageView) findViewById(R.id.btn_update2);
        show_dorm = (Button) findViewById(R.id.btn_show_dorm);
        repair =(Button) findViewById(R.id.btn_repair);
        order_water =(Button) findViewById(R.id.btn_orderWater);
        edit_information = (Button) findViewById(R.id.btn_edit_information);

        n_listView =(MyListView) findViewById(R.id.NotificationList);
        notification_adapter = new ItemAdapter(notification_list,student_main_page.this);
        n_listView.setAdapter(notification_adapter);

        w_listView = (MyListView) findViewById(R.id.WaterList);
        water_adapter = new ItemAdapter(water_list,student_main_page.this);
        w_listView.setAdapter(water_adapter);
    }

    private void loadInfo()//加载已经存在的数据
    {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                stuInfo = DBUtil.QueryStu(account);//学生信息
                buildingId = stuInfo.get(14);
                dormId = stuInfo.get(15);
                System.out.println("宿舍id："+dormId);
                pic = DBUtil.loadImage(account);//图片
                System.out.println(account);
                System.out.println(stuInfo.get(0));
                Message msg1 = new Message();
                msg1.what = 1;//加载学生信息
                //msg.obj = stuInfo;
                handler.sendMessage(msg1);

                Message msg2 = new Message();
                msg2.what=2;//加载头像
                //msg1.obj=pic;
                handler.sendMessage(msg2);
            }
        });
        thread.start();
    }

    private void update_notification()
    {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<item> temp = DBUtil.QuerryNotification(buildingId);
                notification_list.clear();
                notification_list.addAll(temp);
                /*for(int i=0;i<temp.size();i++)
                {
                    item it = temp.get(i);
                    notification_list.add(it);
                    System.out.println(it.getDorm_name()+it.getDate()+it.getDetails());
                }*/
                Message msg = new Message();
                msg.what = 3;
                handler.sendMessage(msg);
            }
        });
        thread.start();
    }

    private void update_orderWater()
    {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<item> temp = DBUtil.QuerryWater(dormId);
                water_list.clear();
                water_list.addAll(temp);
                Message msg = new Message();
                msg.what = 4;
                handler.sendMessage(msg);
            }
        });
        thread.start();
    }

    public Bitmap Bytes2Bimap(byte[] b) {//转化为bitmap
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    private void setOnClickListener()
    {
        btn_update_n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("通知表更新");
                update_notification();
            }
        });
        btn_update_w.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("订水表更新");
                update_orderWater();
            }
        });
        show_dorm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dor_no.length() > 3){
                    Toast.makeText(student_main_page.this, "还未分配宿舍", Toast.LENGTH_SHORT).show();
                } else{
                    Intent intent =new Intent(student_main_page.this,DormInfoActivity.class);
                    intent.putExtra("role","student");
                    intent.putExtra("dorm_id",dormId);
                    startActivity(intent);
                }
            }
        });
        repair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dor_no.length() > 3){
                    Toast.makeText(student_main_page.this, "还未分配宿舍", Toast.LENGTH_SHORT).show();
                } else{
                    Intent intent =new Intent(student_main_page.this,repair_edit.class);
                    intent.putExtra("stu_account",account);
                    intent.putExtra("dorm_id",dormId);
                    startActivity(intent);
                }

            }
        });
        order_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dor_no.length() > 3){
                    Toast.makeText(student_main_page.this, "还未分配宿舍", Toast.LENGTH_SHORT).show();
                } else{
                    Intent intent =new Intent(student_main_page.this,Order_editActivity.class);
                    intent.putExtra("stu_account",account);
                    intent.putExtra("dorm_id",dormId);
                    startActivity(intent);
                }

            }
        });
        edit_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(student_main_page.this,edit_informationActivity.class);
                intent.putExtra("stu_account",account);
                startActivity(intent);
            }
        });


    }
}
