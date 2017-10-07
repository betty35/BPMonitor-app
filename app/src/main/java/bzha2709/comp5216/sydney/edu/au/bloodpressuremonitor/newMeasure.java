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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_measure);
        this.setTitle("Add New Measure");
        timeOfRecord=findViewById(R.id.time_text_datetime);
        selectedTime=new Date();
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
            Measure newMeasure=new Measure(selectedTime,dia,sys,pulse,pos,arm,mood);
            int num=mDAO.queryBuilder().where(MeasureDao.Properties.Time.eq(selectedTime)).list().size();
            if(num==0) mDAO.insert(newMeasure);
            else mDAO.update(newMeasure);
            Toast.makeText(this,"Saved successfully.",Toast.LENGTH_LONG).show();
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
        selectedTime=calendar.getTime();
        datePickerDialog.show();
    }

    private void initGreenDao()
    {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "bp-monitor", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        mDAO=daoSession.getMeasureDao();
    }
}
