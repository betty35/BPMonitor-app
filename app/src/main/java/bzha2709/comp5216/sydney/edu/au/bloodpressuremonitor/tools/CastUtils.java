package bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.tools;

import java.util.ArrayList;
import java.util.List;

import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.bean.Alarm;
import bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.bean.AlarmL;

/**
 * Created by Administrator on 2017/10/21.
 */

public class CastUtils {

    public static List<AlarmL> castAlarmArray2AlarmLArray(List<Alarm> alarms)
    {
        List<AlarmL> target=new ArrayList<AlarmL>();
        for(int i=0;i<alarms.size();i++)
        {
            Alarm a2=alarms.get(i);
            AlarmL a1=castAlarm2AlarmL(a2);
            target.add(a1);
        }
        return target;
    }

    public static AlarmL castAlarm2AlarmL(Alarm a2)
    {
        AlarmL a1=new AlarmL();
        a1.setId(a2.getId());
        a1.setType(a2.getType());
        a1.setTime_h(a2.getTime_h());
        a1.setTime_m(a2.getTime_m());
        a1.setIs_on(a2.getIs_on());
        a1.setMsg(a2.getMsg());
        return a1;
    }

    public static Alarm castAlarmL2Alarm(AlarmL a2)
    {
        Alarm a1=new Alarm();
        a1.setId(a2.getId());
        a1.setType(a2.getType());
        a1.setTime_h(a2.getTime_h());
        a1.setTime_m(a2.getTime_m());
        a1.setIs_on(a2.getIs_on());
        a1.setMsg(a2.getMsg());
        return a1;
    }
}
