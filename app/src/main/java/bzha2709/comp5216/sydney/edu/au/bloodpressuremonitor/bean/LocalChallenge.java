package bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by Administrator on 2017/10/7.
 */

@Entity
public class LocalChallenge
{
    @Property @Unique private int id;
    @Property private String name;
    @Property private String description;
    @Property private String img_path;
    @Property private boolean traceable;
    @Generated(hash = 1042295822)
    public LocalChallenge(int id, String name, String description, String img_path,
            boolean traceable) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.img_path = img_path;
        this.traceable = traceable;
    }
    @Generated(hash = 1876915432)
    public LocalChallenge() {
    }
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getImg_path() {
        return this.img_path;
    }
    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }
    public boolean getTraceable() {
        return this.traceable;
    }
    public void setTraceable(boolean traceable) {
        this.traceable = traceable;
    }
}
