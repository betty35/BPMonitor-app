package bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.bean.User;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.db.DaoMaster;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.db.DaoSession;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.db.UserDao;

public class DataShare extends AppCompatActivity {

    EditText et_share;
    Button back;


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
        setContentView(R.layout.activity_data_share);
        et_share= findViewById(R.id.data_share_url);
        back= findViewById(R.id.data_share_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initGreenDao();
        User user=userDao.queryBuilder().list().get(0);
        final String link=getResources().getString(R.string.shareURL)+"userid="
                +user.getId()+"&psw="+user.getPsd();
        et_share.setText(link);

    }
}
