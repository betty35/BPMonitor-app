package bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;

import java.util.Date;

/**
 * Created by Bingiqng ZHAO on 2017/10/7.
 */

@Entity
public class Record
{
    @Id(autoincrement = true) private Long id;
    @Property long challenge_id;
    @Index(unique = true) private Date finishTime;
    @Generated(hash = 964109379)
    public Record(Long id, long challenge_id, Date finishTime) {
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
    public Date getFinishTime() {
        return this.finishTime;
    }
    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

}
