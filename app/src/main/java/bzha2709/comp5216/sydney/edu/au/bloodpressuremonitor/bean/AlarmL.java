package bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.bean;

import java.io.Serializable;


public class AlarmL implements Serializable
{
    private static final long serialVersionUID = 7111919734178092523L;
    Long id;
    String type;
    String msg;
    int time_h;
    int time_m;
    boolean is_on;

    public AlarmL(Long id, String type, String msg, int time_h, int time_m,
                  boolean is_on) {
        this.id = id;
        this.type = type;
        this.msg = msg;
        this.time_h = time_h;
        this.time_m = time_m;
        this.is_on = is_on;
    }

    public AlarmL() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getMsg() {
        return this.msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public int getTime_h() {
        return this.time_h;
    }
    public void setTime_h(int time_h) {
        this.time_h = time_h;
    }
    public int getTime_m() {
        return this.time_m;
    }
    public void setTime_m(int time_m) {
        this.time_m = time_m;
    }
    public boolean getIs_on() {return is_on;}
    public void setIs_on(boolean is_on) {this.is_on = is_on;}
}
