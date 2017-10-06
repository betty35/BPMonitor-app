package bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;

import java.util.Date;

/**
 * Created by Bingqing ZHAO on 2017/9/25
 */

@Entity
public class Measure
{
    @Index(unique = true) @Property private Date time;
    @Property private short dia;
    @Property private short sys;
    @Property private short pulse;
    @Property private short position;
    @Property private short arm;
    @Property private short mood;

    public Measure() {}

    @Generated(hash = 1103264673)
    public Measure(Date time, short dia, short sys, short pulse, short position,
            short arm, short mood) {
        this.time = time;
        this.dia = dia;
        this.sys = sys;
        this.pulse = pulse;
        this.position = position;
        this.arm = arm;
        this.mood = mood;
    }

    public Date getTime() {
        return this.time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public short getDia() {
        return this.dia;
    }

    public void setDia(short dia) {
        this.dia = dia;
    }

    public short getSys() {
        return this.sys;
    }

    public void setSys(short sys) {
        this.sys = sys;
    }

    public short getPulse() {
        return this.pulse;
    }

    public void setPulse(short pulse) {
        this.pulse = pulse;
    }

    public short getPosition() {
        return this.position;
    }

    public void setPosition(short position) {
        this.position = position;
    }

    public short getArm() {
        return this.arm;
    }

    public void setArm(short arm) {
        this.arm = arm;
    }

    public short getMood() {
        return this.mood;
    }

    public void setMood(short mood) {
        this.mood = mood;
    }
}
