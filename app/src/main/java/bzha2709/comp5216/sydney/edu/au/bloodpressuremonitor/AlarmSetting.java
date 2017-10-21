package bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;

import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.adapter.AlarmListAdapter;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.bean.Alarm;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.bean.AlarmL;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.db.AlarmDao;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.db.DaoMaster;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.db.DaoSession;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.tools.CastUtils;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.tools.ConsUtils;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.tools.NewAlarm;

public class AlarmSetting extends AppCompatActivity {

    ImageButton add_alarm;
    ListView alarm_list;
    List<Alarm> alarmArray;
    List<AlarmL> alarm_for_show;

    private View onMenu;
    private boolean isMenuOn;


    AlarmListAdapter alarm_list_adapter;
    //db
    DaoMaster daoMaster;
    DaoSession daoSession;
    DaoMaster.DevOpenHelper helper;
    SQLiteDatabase db;
    AlarmDao alarmDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);
        initGreenDao();
        alarm_list= findViewById(R.id.alarm_setting_alarm_list);
        add_alarm= findViewById(R.id.add_alarm_button);


        alarmArray =alarmDao.queryBuilder().list();
        alarm_list.setVisibility(View.VISIBLE);
        alarm_for_show= CastUtils.castAlarmArray2AlarmLArray(alarmArray);
        alarm_list_adapter=new AlarmListAdapter(this, alarm_for_show);
        alarm_list.setAdapter(alarm_list_adapter);


        alarm_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, final int i, long l) {
                if(onMenu!=null&&onMenu==view){toggleMenu(view);}
                else if(onMenu!=null&onMenu!=view){toggleMenu(onMenu);toggleMenu(view);}
                else{toggleMenu(view);}
                final Button bt_delete = view.findViewById(R.id.alarm_setting_item_del);
                final Button bt_update = view.findViewById(R.id.alarm_setting_item_edit);
                bt_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toggleMenu(view);
                        //删除的时候取消掉闹钟
                        //// TODO: 2017/10/20
                        alarmDao.delete(alarmArray.get(i));
                       refreshAlarms();
                    }
                });
                bt_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toggleMenu(view);
                        Intent intent=new Intent(AlarmSetting.this,NewAlarm.class);
                        intent.putExtra("alarm", alarmArray.get(i));
                        startActivityForResult(intent, ConsUtils.NEW_ALARM);
                    }
                });
            }
        });


       add_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AlarmSetting.this, NewAlarm.class);
                intent.putExtra("alarm",new Alarm());
                startActivity(intent);
            }
        });
    }


    private void initGreenDao()
    {
        helper = new DaoMaster.DevOpenHelper(this, "bp-monitor", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        alarmDao=daoSession.getAlarmDao();
    }

    @Override public void onDestroy()
    {
        if(null!=daoSession)daoSession.clear();
        daoSession=null;
        db.close();
        helper.close();
        super.onDestroy();
    }

    @Override public void onResume()
    {
        super.onResume();
        refreshAlarms();
    }



    public void refreshAlarms()
    {
        if(null==daoSession) initGreenDao();
        alarmArray.clear();
        alarmArray =alarmDao.queryBuilder().list();
        alarm_for_show.clear();
        List<AlarmL> cast=CastUtils.castAlarmArray2AlarmLArray(alarmArray);
        alarm_for_show.addAll(cast);
        cast.clear();
        alarm_list_adapter.notifyDataSetChanged();
    }


    private void toggleMenu(View view) {
        RelativeLayout main_panel = view.findViewById(R.id.alarm_setting_item_panel);
        LinearLayout hidden_buttons = view.findViewById(R.id.alarm_setting_item_buttons);
        final Button del_b = view.findViewById(R.id.alarm_setting_item_del);
        final Button edit_b = view.findViewById(R.id.alarm_setting_item_edit);
        final Switch mSwitch= view.findViewById(R.id.alarm_item_switch);

        float back = hidden_buttons.getWidth();
        float front = main_panel.getWidth();
        float width = back / front;
        hidden_buttons.setMinimumHeight(main_panel.getHeight());
        TranslateAnimation ta;
        if(isMenuOn){
            ta= new TranslateAnimation(Animation.RELATIVE_TO_SELF, width, Animation.RELATIVE_TO_SELF,
                    0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
            onMenu=null;
            isMenuOn=false;
        }else{
            ta= new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                    width, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
            onMenu=view;
            isMenuOn=true;
        }
        ta.setDuration(200);
        ta.setFillAfter(true);
        ta.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                if (isMenuOn) {
                    del_b.setClickable(true);
                    edit_b.setClickable(true);
                    mSwitch.setClickable(false);
                } else {
                    del_b.setClickable(false);
                    edit_b.setClickable(false);
                    mSwitch.setClickable(true);
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        main_panel.startAnimation(ta);
    }




    @Override
    protected void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ConsUtils.NEW_ALARM)
        {
            if(requestCode==RESULT_OK)
            {
                Toast.makeText(this,"Edited",Toast.LENGTH_LONG).show();
                refreshAlarms();
            }
        }
    }


}
