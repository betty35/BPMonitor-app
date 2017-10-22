package bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.bean;


import android.graphics.Bitmap;

public class ChallengeItem
{
    private Bitmap img;
    private String des;

    public ChallengeItem(){}

    public ChallengeItem(Bitmap img, String des) {
        this.img = img;
        this.des = des;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
