package bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by Bingiqng ZHAO on 2017/10/7.
 */

@Entity
public class Record
{
    @Id(autoincrement = true) private Long id;
    @Property long challenge_id;
    @Index(unique = true) private Long finishTime;
    @Generated(hash = 741182494)
    public Record(Long id, long challenge_id, Long finishTime) {
        this.id = id;
        this.challenge_id = challenge_id;
        this.finishTime = finishTime;
    }
    @Generated(hash = 477726293)
    public Record() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public long getChallenge_id() {
        return this.challenge_id;
    }
    public void setChallenge_id(long challenge_id) {
        this.challenge_id = challenge_id;
    }
    public Long getFinishTime() {
        return this.finishTime;
    }
    public void setFinishTime(Long finishTime) {
        this.finishTime = finishTime;
    }

}
