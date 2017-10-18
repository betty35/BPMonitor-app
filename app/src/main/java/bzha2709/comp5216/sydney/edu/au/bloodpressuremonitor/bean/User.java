package bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.bean;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by Bingqing Zhao on 2017/9/25.
 */

@Entity
public class User
{
    @Id(autoincrement = true) private Long id;
    @Property private String email;
    @Property private String phone;
    @Property private String nickname;
    @Property private int year_of_birth;
    @Property private short gender;
    @Property private String area;
    @Property private String memo;
    @Property private String auth;
    @Generated(hash = 784755692)
    public User(Long id, String email, String phone, String nickname,
            int year_of_birth, short gender, String area, String memo,
            String auth) {
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.nickname = nickname;
        this.year_of_birth = year_of_birth;
        this.gender = gender;
        this.area = area;
        this.memo = memo;
        this.auth = auth;
    }
    @Generated(hash = 586692638)
    public User() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getNickname() {
        return this.nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public int getYear_of_birth() {
        return this.year_of_birth;
    }
    public void setYear_of_birth(int year_of_birth) {
        this.year_of_birth = year_of_birth;
    }
    public short getGender() {
        return this.gender;
    }
    public void setGender(short gender) {
        this.gender = gender;
    }
    public String getArea() {
        return this.area;
    }
    public void setArea(String area) {
        this.area = area;
    }
    public String getMemo() {
        return this.memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }
    public String getAuth() {
        return this.auth;
    }
    public void setAuth(String auth) {
        this.auth = auth;
    }


}
