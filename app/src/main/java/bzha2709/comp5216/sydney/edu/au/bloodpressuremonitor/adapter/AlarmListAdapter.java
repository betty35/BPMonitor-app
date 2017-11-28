package bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.R;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.bean.Alarm;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.bean.AlarmL;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.db.AlarmDao;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.db.DaoMaster;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.db.DaoSession;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.tools.CastUtils;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.tools.clock.AlarmManagerUtil;

public class AlarmListAdapter extends ArrayAdapter<AlarmL> {
    List<AlarmL> alarmList;
    Context context;
    ViewHolder holder=null;
    LayoutInflater inflater;
    DaoMaster daoMaster;
    DaoSession daoSession;
    DaoMaster.DevOpenHelper helper;
    SQLiteDatabase db;
    AlarmDao alarmDao;

    public AlarmListAdapter(@NonNull Context context, @NonNull List<AlarmL> objects) {
        super(context,0,objects);
        this.context=context;
        alarmList=objects;
        inflater=LayoutInflater.from(context);
        Log.i("bpMon","created");
    }



    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final AlarmL a= getItem(position);
        if(view==null)
        {
            view=inflater.inflate(R.layout.alarm_item,parent,false);
            holder=new ViewHolder();
            holder.time= view.findViewById(R.id.alarm_item_time);
            holder.memo= view.findViewById(R.id.alarm_item_memo);
            holder.turn= view.findViewById(R.id.alarm_item_switch);
            holder.alarm_type= view.findViewById(R.id.alarm_item_type);
            view.setTag(holder);
            holder.turn.setChecked(a.getIs_on());
            holder.turn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    a.setIs_on(b);
                    Alarm a2= CastUtils.castAlarmL2Alarm(a);
                    a2.setIs_on(b);
                    initGreenDao();
                    alarmDao.save(a2);
                    closeDB();
                    if(b)
                    {
                        AlarmManagerUtil.setAlarm(context, 1, a2.getTime_h(), a2.getTime_m(), a2.getId().intValue(), 0, a2.getMsg(), 1);
                        //Toast.makeText(context,"Alarm turned on",Toast.LENGTH_SHORT).show();
                    }else
                    {
                        AlarmManagerUtil.cancelAlarm(context, AlarmManagerUtil.ALARM_ACTION, a2.getId().intValue());
                        //Toast.makeText(context,"Alarm turned off",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else holder=(ViewHolder) view.getTag();

        holder.memo.setText(a.getMsg());
        holder.time.setText((a.getTime_h()<10?"0":"")+a.getTime_h()+":"+(a.getTime_m()<10?"0":"")+a.getTime_m());
        holder.turn.setChecked(a.getIs_on());
        holder.alarm_type.setText(a.getType().equals("med")?"MED ":"BP  ");
        return view;
    }


    class ViewHolder{
        TextView time;
        TextView memo;
        TextView alarm_type;
        Switch turn;
    }


    private void initGreenDao()
    {
        helper = new DaoMaster.DevOpenHelper(context, "bp-monitor", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        alarmDao=daoSession.getAlarmDao();
    }

    public void closeDB()
    {
        daoSession.clear();
        daoSession=null;
        db.close();
        helper.close();
    }

}