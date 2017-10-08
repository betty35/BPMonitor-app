package bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.bean.Measure;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.db.DaoMaster;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.db.DaoSession;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.db.MeasureDao;

public class newMeasure extends AppCompatActivity {

    TextView timeOfRecord;
    EditText sysT;
    EditText diaT;
    EditText pulseT;
    Spinner armS;
    Spinner posS;
    Spinner moodS;

    Date selectedTime;
    short sys,dia,pulse,arm,pos,mood;
    MeasureDao mDAO;

    DaoMaster.DevOpenHelper helper;
    SQLiteDatabase db;
    DaoMaster daoMaster;
    DaoSession daoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_measure);
        this.setTitle("Add New Measure");
        timeOfRecord=findViewById(R.id.time_text_datetime);
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.SECOND,0);
        selectedTime=calendar.getTime();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        timeOfRecord.setText(sdf.format(selectedTime));
        timeOfRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPick((TextView) v);
            }
        });
        sysT=findViewById(R.id.sys_text);
        diaT=findViewById(R.id.dia_text);
        pulseT=findViewById(R.id.pulse_text);
        armS=findViewById(R.id.arm_spin);
        posS=findViewById(R.id.pos_spin);
        moodS=findViewById(R.id.mood_spin);
        initGreenDao();
    }


    public void onSaveMeasure(View view)
    {
        if(checkEntry())
        {
            sys=Short.parseShort(sysT.getText().toString());
            dia=Short.parseShort(diaT.getText().toString());
            pulse=Short.parseShort(pulseT.getText().toString());
            arm=(short)armS.getSelectedItemPosition();
            pos=(short)posS.getSelectedItemPosition();
            mood=(short)moodS.getSelectedItemPosition();
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                selectedTime=sdf.parse(timeOfRecord.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Measure newMeasure=new Measure(selectedTime,dia,sys,pulse,pos,arm,mood);
            List<Measure> ml=mDAO.queryBuilder().where(MeasureDao.Properties.Time.eq(selectedTime)).list();
            if(ml.size()==0)
            {
                mDAO.insert(newMeasure);
                Toast.makeText(this,"Saved successfully. time:"+selectedTime.toString(),Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this,"Data Collision",Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(this,"Please enter correct data.",Toast.LENGTH_LONG).show();
        }
    }


    public void onLeaveMeasure(View view)
    {
        finish();
    }

    public boolean checkEntry()
    {
        if(sysT.length()<2) return false;
        if(diaT.length()<2) return false;
        return pulseT.length() >= 2;
    }

    private void showDialogPick(final TextView timeText) {
        final StringBuffer time = new StringBuffer();
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        final TimePickerDialog timePickerDialog = new TimePickerDialog(newMeasure.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                time.append(" " + hourOfDay + ":" + minute);
                timeText.setText(time);
            }
        }, hour, minute, true);
        DatePickerDialog datePickerDialog = new DatePickerDialog(newMeasure.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                time.append(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                timePickerDialog.show();
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void initGreenDao()
    {
        helper = new DaoMaster.DevOpenHelper(this, "bp-monitor", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        mDAO=daoSession.getMeasureDao();
    }

    @Override public void onDestroy()
    {
        super.onDestroy();
        daoSession.clear();
        daoSession=null;
        db.close();
        helper.close();
    }
}
