package bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;

public class KeepAlive extends Service {
    Notification notify;

    public KeepAlive() {
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        Notification.Builder builder =new Notification.Builder(getApplicationContext());
        //Intent nfIntent = new Intent(this, MainActivity.class);
        builder//.setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, 0))
                .setContentTitle("BP Monitor -- Reminders pending.")
                .setContentText("Please don't kill this process.")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));

        if(android.os.Build.VERSION.SDK_INT>= android.os.Build.VERSION_CODES.LOLLIPOP) {
            builder
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setSmallIcon(R.mipmap.ic_launcher);
        }
        else{
            builder.setSmallIcon(R.mipmap.ic_launcher);
        }
        notify = builder.build();

    }

    @Override
    public IBinder onBind(Intent intent) {
       return new MyBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(110,notify);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        stopSelf();
        super.onDestroy();
    }


    class MyBinder extends Binder {
        public KeepAlive getService(){
            return KeepAlive.this;
        }
    }
}
