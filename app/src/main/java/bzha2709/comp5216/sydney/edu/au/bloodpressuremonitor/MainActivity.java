package bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.Date;
import java.util.List;

import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.bean.MeasurePackage;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.bean.User;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.db.DaoMaster;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.db.DaoSession;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.db.MeasureDao;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.db.UserDao;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.listener.MyBottomNaviListener;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.tools.ConsUtils;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.tools.NotificationUtils;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.tools.PrefUtils;

public class MainActivity extends AppCompatActivity
{
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
    public static int APP_REQUEST_CODE = 99;
    DaoMaster.DevOpenHelper helper;
    SQLiteDatabase db;
    DaoMaster daoMaster;
    DaoSession daoSession;
    UserDao userDao;
    MeasureDao measureDao;
    User user=null;

    Intent keepAlive;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;

    Context context;
    ConnectivityManager connectivityManager;
    Gson gson = new Gson();

    private void initGreenDao()
    {
        helper = new DaoMaster.DevOpenHelper(this, "bp-monitor", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        userDao=daoSession.getUserDao();
        measureDao=daoSession.getMeasureDao();
    }

    @Override public void onDestroy()
    {
        daoSession.clear();
        daoSession=null;
        db.close();
        helper.close();
        stopService(keepAlive);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!NotificationUtils.isNotificationEnabled(this))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Access Notification")
                    .setMessage("Please enable notification for this app, or some functions may be suppressed.")
                    .setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivityForResult(intent, ConsUtils.NOTIFICATION_PERMISSION_REQUEST);
                                return;
                        }
                    })
                    .setNegativeButton("Refuse", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            builder.create().show();
        }
        initGreenDao();
        context = getApplicationContext();
        connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final List<User> userList=userDao.queryBuilder().list();

        AccessToken accessToken = AccountKit.getCurrentAccessToken();
        if (accessToken != null) {
            if(userList.size()>0) user=userList.get(0);
            else {
                user=new User();
                user.setAuth(accessToken.getAccountId());
                register(user);
            }
        }
        else { phoneLogin();}
        BottomNavigationView navigation = findViewById(R.id.navigation);
        mOnNavigationItemSelectedListener = new MyBottomNaviListener(this,R.id.content);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        drawerLayout= findViewById(R.id.drawer_layout);
        toolbar= findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        toolbar.setTitle(getString(R.string.app_name));
        navigationView= findViewById(R.id.nv_menu_left);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_user_setting:
                    {
                        if(checkNetState()){
                        Intent i=new Intent(MainActivity.this,UserSetting.class);
                        startActivity(i);}
                        else
                            Toast.makeText(MainActivity.this,"Please check your network connection.",Toast.LENGTH_LONG).show();
                        break;
                    }
                    case R.id.menu_data_synchro:
                    {
                        if(checkNetState())
                        {
                            if(null==user||user.getId()==null||user.getId()==0)
                            {
                                Toast.makeText(MainActivity.this,"Please successfully register first.",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Long last_up=PrefUtils.getlong(getApplicationContext(),"data_last_synch",0l);
                                MeasurePackage mp=new MeasurePackage();
                                mp.setUser_id(user.getId());
                                if(null==last_up||last_up==0)
                                {
                                    mp.setMeasures(measureDao.queryBuilder().list());
                                }
                                else
                                {
                                    mp.setMeasures(measureDao.queryBuilder().
                                            where(MeasureDao.Properties.Time.gt(last_up)).list());
                                }
                                if(mp.getMeasures().size()>0)
                                {
                                    Gson g=new Gson();
                                    OkGo.<String>post(getResources().getString(R.string.serverURL)+"measure/upload")
                                            .upJson(g.toJson(mp)).execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(Response<String> response) {
                                            if(response.body().equals("uploaded"))
                                            {
                                                PrefUtils.putlong(getApplicationContext(),"data_last_synch",new Date().getTime());
                                                Toast.makeText(MainActivity.this,"Successfully uploaded.",Toast.LENGTH_LONG).show();
                                            }
                                            else Toast.makeText(MainActivity.this,"Server failed to respond. PLease try again.",Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                                else
                                    Toast.makeText(MainActivity.this,"No new data needed to be synched.",Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        Toast.makeText(MainActivity.this,"Please check your network connection.",Toast.LENGTH_LONG).show();
                        break;
                    }
                    case R.id.menu_data_share:
                    {
                        if(checkNetState())
                        {
                            Long last_up= PrefUtils.getlong(getApplicationContext(),"data_last_synch",0l);
                            Long psw_last=PrefUtils.getlong(getApplicationContext(),"user_last_updated",0l);
                            if(null==user||user.getId()==null||user.getId()==0)
                            {
                                Toast.makeText(MainActivity.this,"Please successfully register first.",Toast.LENGTH_LONG).show();
                            }
                            else if(null==user.getLastupdate()||user.getLastupdate()==0)
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("Notice")
                                        .setMessage("Please set your sharing password and data period first. Select \"User Setting\" from the menu.")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                            }
                                        });
                                builder.create().show();
                            }
                            else if(null==last_up||last_up==0)
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("Notice")
                                        .setMessage("Data needed to be uploaded before being shared. Please select \"Synchro\" from the menu.")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                            }
                                        });
                                builder.create().show();
                            }
                            else if ((new Date().getTime()-last_up)>1000*3600*24*2)
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("Notice")
                                        .setMessage("It has been over 2 days since you've changed your sharing password."+
                                                "For your data safety, please update it."+
                                                " Select \"User Setting\" from the menu.")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
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
                                Intent i=new Intent(MainActivity.this,DataShare.class);
                                startActivity(i);
                            }
                        }
                        else
                            Toast.makeText(MainActivity.this,"Please check your network connection.",Toast.LENGTH_LONG).show();
                        break;
                    }
                }
                return false;
            }
        });

        keepAlive=new Intent(this,KeepAlive.class);
        startService(keepAlive);
    }


    public void phoneLogin() {
        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.TOKEN
        // ... perform additional configuration ...
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }




    @Override
    protected void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage;
            if (loginResult.getError() != null) {
                toastMessage = loginResult.getError().getErrorType().getMessage();
                //showErrorActivity(loginResult.getError());
            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";
            } else {
                if (loginResult.getAccessToken() != null) {
                    toastMessage = "Success:" + loginResult.getAccessToken().getAccountId();
                } else {
                    toastMessage = String.format(
                            "Success:%s...",
                            loginResult.getAuthorizationCode().substring(0,10));
                }
            }
            Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
            if(null==user)
            {
                user=new User();
                user.setAuth(loginResult.getAccessToken().getAccountId());
                register(user);
            }
        }
        else if(requestCode==ConsUtils.NOTIFICATION_PERMISSION_REQUEST)
        {
            startService(keepAlive);
        }
    }
    
    // TODO: 2017/10/8
    public void register(User u)
    {
        OkGo.<String>get(getResources().getString(R.string.serverURL)+"user/register").params("auth",u.getAuth()).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.i("bp",response.body());
                user=gson.fromJson(response.body(),User.class);
                if(user!=null&&user.getId()!=0)
                {
                    PrefUtils.putlong(getApplicationContext(),"user_id",user.getId());
                }
            }
        });
        Long id=PrefUtils.getlong(getApplicationContext(),"user_id",0l);
        if(null!=id&&id!=0){
            Toast.makeText(MainActivity.this,"id:"+id,Toast.LENGTH_LONG).show();
            u.setId(id);
            userDao.insert(u);
        }
        //userDao.save(u);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        List<User> ul=userDao.queryBuilder().list();
        if(ul.size()>0)user=ul.get(0);
    }


    public boolean checkNetState(){
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Notice")
                .setMessage("Please user home key instead. Or the reminders might be nullified.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNegativeButton("stop anyway", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        builder.create().show();
    }

}
