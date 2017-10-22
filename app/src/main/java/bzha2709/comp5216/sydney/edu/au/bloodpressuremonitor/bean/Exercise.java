package bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by Bingqing ZHAO on 2017/10/7.
 */

@Entity
public class Exercise
{
    @Id(autoincrement = true) private Long id;
    @Index(unique = true) private Long time;
    @Property private int steps;
    @Generated(hash = 605950893)
    public Exercise(Long id, Long time, int steps) {
        this.id = id;
        this.time = time;
        this.steps = steps;
    }
    @Generated(hash = 1537691247)
    public Exercise() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getTime() {
        return this.time;
    }
    public void setTime(Long time) {
        this.time = time;
    }
    public int getSteps() {
        return this.steps;
    }
    public void setSteps(int steps) {
        this.steps = steps;
    }

}
