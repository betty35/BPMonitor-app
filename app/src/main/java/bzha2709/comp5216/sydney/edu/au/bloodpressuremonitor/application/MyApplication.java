package bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.application;

import android.app.Application;
import android.util.Log;

import com.lzy.okgo.OkGo;


public class MyApplication extends Application {
    private static final String TAG="MyApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        OkGo.getInstance().init(this);
        Log.e(TAG,"MyApplication is created");
    }
}
