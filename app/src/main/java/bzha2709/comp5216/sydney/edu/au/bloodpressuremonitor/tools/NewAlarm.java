package bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.tools;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.R;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.bean.Alarm;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.db.AlarmDao;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.db.DaoMaster;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.db.DaoSession;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.tools.clock.AlarmManagerUtil;

public class NewAlarm extends AppCompatActivity {

    Button confirmB,cancelB;
    RadioGroup type_group;
    RadioButton measure_radio,med_radio;
    EditText memo;
    TextView set_time;
    int h,m;
    String holder;
    String alarm_type;

    Alarm alarm;
    DaoMaster daoMaster;
    DaoSession daoSession;
    DaoMaster.DevOpenHelper helper;
    SQLiteDatabase db;
    AlarmDao alarmDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        alarm=(Alarm)intent.getSerializableExtra("alarm");
        setContentView(R.layout.activity_new_alarm);

        confirmB= findViewById(R.id.alarm_edit_confirm_button);
        cancelB= findViewById(R.id.alarm_edit_cancel_button);
        set_time= findViewById(R.id.alarm_time);
        memo= findViewById(R.id.alarm_memo_edit_text);
        type_group= findViewById(R.id.alarm_type_radio_group);
        measure_radio= findViewById(R.id.radio_bp_measure);
        med_radio= findViewById(R.id.radio_medication);
        h=Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        m=Calendar.getInstance().get(Calendar.MINUTE);

        if(null!=alarm.getId())
        {
            h=alarm.getTime_h();
            m=alarm.getTime_m();
        }
        set_time.setText((h<10?"0":"")+h+":"+(m<10?"0":"")+m);
        set_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogPick(set_time);
            }
        });

        type_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i)
            {
                switch (i){
                    case R.id.radio_bp_measure:
                    {
                        setMeasureMemo();
                        break;
                    }
                    case R.id.radio_medication:
                    {
                        holder="Med:  , Dose:  ";
                        memo.setText(holder);
                        break;
                    }
                }
            }
        });
        measure_radio.setChecked(true);
        if(null!=alarm.getId())
        {
            if(alarm.getType().equals("med"))med_radio.setChecked(true);
            holder=alarm.getMsg();
            memo.setText(holder);
        }

        initGreenDao();

        confirmB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAlarmChange();
                Intent data = new Intent();
                data.putExtra("save","OK");
                setResult(RESULT_OK, data);
                finish();
            }
        });
        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void saveAlarmChange()
    {
        if(alarm.getId()==null)
        {
            Log.i("new alarm","param==null");
            alarm.setIs_on(true);
        }
        if(measure_radio.isChecked()) alarm_type="measure";
        else alarm_type="med";

        alarm.setMsg(memo.getText().toString());
        alarm.setTime_h(h);
        alarm.setTime_m(m);
        alarm.setType(alarm_type);
        alarmDao.save(alarm);
        if(null!=alarm.getId()&&alarm.getIs_on())
        {
            AlarmManagerUtil.cancelAlarm(this,AlarmManagerUtil.ALARM_ACTION,alarm.getId().intValue());
            AlarmManagerUtil.setAlarm(this,1,h,m,alarm.getId().intValue(),0,alarm.getMsg(),1);
        }
    }


    private void showDialogPick(final TextView timeText) {
        final StringBuffer time = new StringBuffer();
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        final TimePickerDialog timePickerDialog = new TimePickerDialog(NewAlarm.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                h=hourOfDay;
                m=minute;
                time.append((hourOfDay<10?"0":"")+h + ":" +(minute<10?"0":"")+ m);
                timeText.setText(time);
                if(measure_radio.isChecked())
                {
                    setMeasureMemo();
                }
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }

    private void setMeasureMemo()
    {
        if(4<h&&h<=10) holder="Morning blood pressure measure";
        else if(h<=14) holder="Noon blood pressure measure";
        else if(h<=16) holder="Afternoon blood pressure measure";
        else holder="Evening blood pressure measure";
        memo.setText(holder);
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
        daoSession.clear();
        daoSession=null;
        db.close();
        helper.close();
        super.onDestroy();
    }
}
