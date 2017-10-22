package bzha2709.comp5216.sydney.edu.au.bloodpressuremonitor.bean;

import java.util.List;

public class MeasurePackage
{
    Long user_id;
    List<Measure> measures;

    public MeasurePackage(){}

    public MeasurePackage(List<Measure> measures,Long user_id)
    {
        this.measures=measures;
        this.user_id=user_id;
    }

    public List<Measure> getMeasures() {
        return measures;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setMeasures(List<Measure> measures) {
        this.measures = measures;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }
}
