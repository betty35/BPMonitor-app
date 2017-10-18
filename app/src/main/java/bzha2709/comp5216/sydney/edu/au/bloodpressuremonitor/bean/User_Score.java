package bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;

import java.util.Date;

/**
 * Created by Bingqing ZHAO on 2017/10/7.
 */

@Entity
public class User_Score
{
    @Id(autoincrement = true) private Long id;
    @Index(unique = true) private Date time;
    @Property private double score;
    @Generated(hash = 24960308)
    public User_Score(Long id, Date time, double score) {
        this.id = id;
        this.time = time;
        this.score = score;
    }
    @Generated(hash = 1610261971)
    public User_Score() {
    }
    public Date getTime() {
        return this.time;
    }
    public void setTime(Date time) {
        this.time = time;
    }
    public double getScore() {
        return this.score;
    }
    public void setScore(double score) {
        this.score = score;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
