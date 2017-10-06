package bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Bingqing ZHAO on 2017/9/25
 */

@Entity
public class Measure
{
    @Id(autoincrement = true)
       private long id;
    @Index(unique = true)
    private Date time;
    private int dia;
    private int sys;
    private int pulse;
    private int position;
    private int arm;
    private int mood;

    public Measure() {}

    @Generated(hash = 1497464404)
    public Measure(long id, Date time, int dia, int sys, int pulse, int position,
            int arm, int mood) {
        this.id = id;
        this.time = time;
        this.dia = dia;
        this.sys = sys;
        this.pulse = pulse;
        this.position = position;
        this.arm = arm;
        this.mood = mood;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPulse() {
        return pulse;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getSys() {
        return sys;
    }

    public void setSys(int sys) {
        this.sys = sys;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getArm() {
        return arm;
    }

    public void setArm(int arm) {
        this.arm = arm;
    }

    public int getMood() {
        return mood;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }
}
