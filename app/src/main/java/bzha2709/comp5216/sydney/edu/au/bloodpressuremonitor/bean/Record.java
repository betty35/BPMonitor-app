package bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;

import java.util.Date;

/**
 * Created by Bingiqng ZHAO on 2017/10/7.
 */

@Entity
public class Record
{
    @Property private int challenge_id;
    @Index(unique = true) private Date finishTime;
    @Generated(hash = 1847154764)
    public Record(int challenge_id, Date finishTime) {
        this.challenge_id = challenge_id;
        this.finishTime = finishTime;
    }
    @Generated(hash = 477726293)
    public Record() {
    }
    public int getChallenge_id() {
        return this.challenge_id;
    }
    public void setChallenge_id(int challenge_id) {
        this.challenge_id = challenge_id;
    }
    public Date getFinishTime() {
        return this.finishTime;
    }
    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }
}
