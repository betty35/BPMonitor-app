package bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.bean.User;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.db.DaoMaster;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.db.DaoSession;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.db.UserDao;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.tools.PrefUtils;

public class UserSetting extends AppCompatActivity {

    User user;
    Button save,cancel;
    EditText name,psw,phone,email,birth,location,memo;
    TextView start_t,end_t;
    Spinner gender;

    DaoMaster.DevOpenHelper helper;
    SQLiteDatabase db;
    DaoMaster daoMaster;
    DaoSession daoSession;
    UserDao userDao;
    private void initGreenDao()
    {
        helper = new DaoMaster.DevOpenHelper(this, "bp-monitor", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        userDao=daoSession.getUserDao();
    }

    @Override public void onDestroy()
    {
        daoSession.clear();
        daoSession=null;
        db.close();
        helper.close();
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);
        Intent i=getIntent();
        user=(User)i.getSerializableExtra("user");
        save= findViewById(R.id.user_setting_save);
        cancel= findViewById(R.id.user_setting_cancel);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        name= findViewById(R.id.user_setting_nickname);
        psw= findViewById(R.id.user_setting_psd);
        phone= findViewById(R.id.user_setting_phone);
        email= findViewById(R.id.user_setting_email);
        birth= findViewById(R.id.user_setting_birth);
        location= findViewById(R.id.user_setting_area);
        memo= findViewById(R.id.user_setting_memo);
        start_t= findViewById(R.id.user_setting_time_s);
        start_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPick(start_t);
            }
        });
        end_t= findViewById(R.id.user_setting_time_e);
        end_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPick(end_t);
            }
        });
        gender= findViewById(R.id.user_setting_gender);
        initGreenDao();
        if(userDao.queryBuilder().list().size()>0)
        {
            user=userDao.queryBuilder().list().get(0);
            name.setText(user.getNickname()==null?"":user.getNickname());
            phone.setText(user.getPhone()==null?"":user.getPhone());
            email.setText(user.getEmail()==null?"":user.getEmail());
            gender.setSelection(user.getGender());
            String b=""+user.getYear_of_birth();
            birth.setText(b);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            Date startT=new Date(user.getStarttime());
            Date endT=new Date(user.getEndtime());
            start_t.setText(sdf.format(startT));
            end_t.setText(sdf.format(endT));
            location.setText(user.getArea()==null?"":user.getArea());
            memo.setText(user.getMemo()==null?"":user.getMemo());
            psw.setText(user.getPsd()==null?"":user.getPsd());
        }
        else finish();
    }




    private void save()
    {
        user.setArea(location.getText().toString());
        user.setEmail(email.getText().toString());
        user.setPhone(phone.getText().toString());
        user.setGender((short)gender.getSelectedItemPosition());
        //Toast.makeText(this,birth.getText().toString(),Toast.LENGTH_LONG).show();
        user.setYear_of_birth(Integer.parseInt(birth.getText().toString()));
        user.setMemo(memo.getText().toString());
        user.setNickname(name.getText().toString());
        user.setPsd(psw.getText().toString());
        user.setLastupdate(new Date().getTime());
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        long st=0;long ed=0;
        try {
            Date st1=sdf.parse(start_t.getText().toString());
            Date ed1=sdf.parse(end_t.getText().toString());
            st=st1.getTime();
            ed=ed1.getTime();
            if(st>ed)
            {
                long temp=st;
                st=ed;
                ed=temp;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(st!=0&&ed!=0)
        {
            user.setStarttime(st);
            user.setEndtime(ed);
        }
        userDao.save(user);

        if(checkNetState())
        {
            Gson g=new Gson();
            OkGo.<String>post(getResources().getString(R.string.serverURL)+"user/update").upJson(g.toJson(user))
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            Log.i("bp",response.body());
                            Gson g2=new Gson();
                            User u=g2.fromJson(response.body(),User.class);
                            if(u!=null&&u.getId()!=null&&u.getLastupdate()!=null)
                                PrefUtils.putlong(getApplicationContext(),"user_last_updated",u.getLastupdate());
                        }
                    });
            if(PrefUtils.getlong(getApplicationContext(),"user_last_updated",0)>0||
                    ( new Date().getTime()-PrefUtils.getlong(getApplicationContext(),"user_last_updated",0))>1000*60*3)
            Toast.makeText(this,"User info successfully updated",Toast.LENGTH_LONG).show();
            else
            {
                user.setLastupdate(PrefUtils.getlong(getApplicationContext(),"user_last_updated",0));
                userDao.save(user);
                Toast.makeText(this,"Server failed to response, please try again",Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(this,"Updating unsuccessful. Please check your network connection.",Toast.LENGTH_LONG).show();
        }
    }




    public boolean checkNetState(){
        ConnectivityManager connectivityManager= (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.M)
        {
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            Boolean isWifiConn = networkInfo.isConnected();
            networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            Boolean isMobileConn = networkInfo.isConnected();
            return isWifiConn||isMobileConn;
        }
        else
        {
            Network[] networks = connectivityManager.getAllNetworks();
            for (int i=0; i < networks.length; i++){
                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(networks[i]);
                if(networkInfo.isConnected())return true;
            }
            return false;
        }
    }


    private void showDialogPick(final TextView timeText) {
        final StringBuffer time = new StringBuffer();
        final Calendar calendar = Calendar.getInstance();
        final int year_ = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                time.append(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                timeText.setText(time.toString());
            }
        }, year_, month, day);
        datePickerDialog.show();

    }

}
