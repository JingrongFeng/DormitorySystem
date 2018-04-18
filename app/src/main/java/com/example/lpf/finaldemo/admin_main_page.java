package com.example.lpf.finaldemo;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class admin_main_page extends AppCompatActivity {
    private boolean dormAssign = false;
    private String account="";
    private String Admin_name="";
    private String buildingName="";
    private String buildingId="";
    private List<item> repair_list = new ArrayList<item>();//报修list数据
    private ItemAdapter repair_adapter;
    private List<item> water_list = new ArrayList<item>();//订水list数据
    private ItemAdapter water_adapter;
    ImageView btn_update_repair;
    ImageView btn_update_water;
    Button showStudent;
    Button release_notifycation;
    Button adjustDormitory;
    private Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case 0://更新头部状态栏
                    TextView AdminName = (TextView) findViewById(R.id.admistor_name);
                    TextView building = (TextView) findViewById(R.id.Building);
                    AdminName.setText("管理员："+Admin_name);
                    building.setText(buildingName);
                    break;
               case 1://更新报修表
                    repair_adapter.notifyDataSetChanged();
                    break;
                case 2://更新送水表
                    water_adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.administor_main_page);
        Intent intent =getIntent();
        account = intent.getStringExtra("account");
        repair_adapter = new ItemAdapter(repair_list,admin_main_page.this);
        MyListView r_listView = (MyListView) findViewById(R.id.RepairList);
        r_listView.setAdapter(repair_adapter);

        water_adapter = new ItemAdapter(water_list,admin_main_page.this);
        MyListView w_listView = (MyListView) findViewById(R.id.WaterList);
        w_listView.setAdapter(water_adapter);

        update_stateBar();
        update_repairList();
        update_waterList();

        btn_update_repair = (ImageView) findViewById(R.id.btn_update1);
        btn_update_water = (ImageView) findViewById(R.id.btn_update2);
        showStudent = (Button) findViewById(R.id.btn_show_student_list);
        release_notifycation = (Button) findViewById(R.id.btn_release_notification);
        adjustDormitory =(Button) findViewById(R.id.btn_adjust_dormitory);

        setOnClickListener();
    }

    //根据查询管理员账号查询管理员姓名以及对应楼号
    private void update_stateBar()
    {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,String> map = DBUtil.QuerryAdmin(account);
                Admin_name = map.get("AdminName");
                buildingName = map.get("BuildingName");
                buildingId = map.get("Buildingid");
                Message msg = new Message();
                msg.what = 0;
                handler.sendMessage(msg);
            }
        });
        thread.start();
    }

    //根据管理员账号更新报修表
    private void update_repairList()
    {
        final Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                List<item> temp = DBUtil.QuerryRepairByA_account(account);
                repair_list.clear();
                repair_list.addAll(temp);
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
                //repair_adapter.notifyDataSetChanged();
            }
        });
        thread1.start();
    }

    //根据管理员账号更新订水表
    private void update_waterList()
    {
        final Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                List<item> temp = DBUtil.QuerryWaterByA_account(account);
                water_list.clear();
                water_list.addAll(temp);
                Message msg = new Message();
                msg.what = 2;
                handler.sendMessage(msg);
                //repair_adapter.notifyDataSetChanged();
            }
        });
        thread2.start();
    }

    public void setOnClickListener()
    {
        btn_update_repair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_repairList();
            }
        });
        btn_update_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_waterList();
            }
        });
        showStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dormAssign == false){
                    Toast.makeText(admin_main_page.this, "还未分配宿舍", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent =new Intent(admin_main_page.this,DormListActivity.class);
                    intent.putExtra("a_account",account);
                    //需要添加传递的参数
                    startActivity(intent);
                }
//                Intent intent =new Intent(admin_main_page.this,DormListActivity.class);
//                intent.putExtra("a_account",account);
//                //需要添加传递的参数
//                startActivity(intent);
            }
        });
        release_notifycation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(admin_main_page.this,Notification_EditActivity.class);
                intent.putExtra("a_account",account);
                intent.putExtra("building_id",buildingId);
                startActivity(intent);
            }
        });
        adjustDormitory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dormAssign == false){
                    Toast.makeText(admin_main_page.this, "还未分配宿舍", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent =new Intent(admin_main_page.this,RoomUpdateActivity.class);
                    intent.putExtra("a_account",account);
                    startActivity(intent);
                }
            }
        });
    }
}
