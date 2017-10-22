package bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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


            AlertDialog.Builder builder = new AlertDialog.Builder(newMeasure.this);
            builder.setTitle("Confirm data")
                    .setMessage("SYS:"+sys+", DIA:"+dia+", PULSE:"+pulse+", POS:"+getResources().getStringArray(R.array.select_pos)[pos]+
                    ", ARM:"+getResources().getStringArray(R.array.select_arm)[arm]+", MOOD:"+getResources().getStringArray(R.array.select_mood)[mood]
                    +", Time:"+selectedTime.toString())
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Measure newMeasure=new Measure(null,selectedTime.getTime(),dia,sys,pulse,pos,arm,mood);
                            List<Measure> ml=mDAO.queryBuilder().where(MeasureDao.Properties.Time.eq(selectedTime)).list();
                            if(ml.size()==0)
                            {
                                mDAO.insert(newMeasure);
                                Toast.makeText(newMeasure.this,"Saved successfully.",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                mDAO.insertOrReplace(newMeasure);
                                Toast.makeText(newMeasure.this,"Updated successfully.",Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            builder.create().show();



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
        final int year_ = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int min = calendar.get(Calendar.MINUTE);
        final TimePickerDialog timePickerDialog = new TimePickerDialog(newMeasure.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                time.append(" " + hourOfDay + ":" + minute);
                timeText.setText(time);
            }
        }, hour, min, true);
        DatePickerDialog datePickerDialog = new DatePickerDialog(newMeasure.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if(year>year_)year=year_;
                if(monthOfYear>month)monthOfYear=month;
                if(dayOfMonth>day) dayOfMonth=day;
                time.append(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                timePickerDialog.show();
            }
        }, year_, month, day);
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
        daoSession.clear();
        daoSession=null;
        db.close();
        helper.close();
        super.onDestroy();
    }
}
