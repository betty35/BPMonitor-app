package bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;

import java.util.Date;

/**
 * Created by Bingqing ZHAO on 2017/10/7.
 */

@Entity
public class Exercise
{
    @Index(unique = true) private Date time;
    @Property private int steps;
    @Generated(hash = 988658308)
    public Exercise(Date time, int steps) {
        this.time = time;
        this.steps = steps;
    }
    @Generated(hash = 1537691247)
    public Exercise() {
    }
    public Date getTime() {
        return this.time;
    }
    public void setTime(Date time) {
        this.time = time;
    }
    public int getSteps() {
        return this.steps;
    }
    public void setSteps(int steps) {
        this.steps = steps;
    }
}
