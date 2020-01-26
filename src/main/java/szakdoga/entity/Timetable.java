package szakdoga.entity;

import javax.persistence.*;

@Entity
@Table(name = "timetable")
public class Timetable {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name="monday")
    private String monday;

    @Column(name="tuesday")
    private String tuesday;

    @Column(name="wednesday")
    private String wednesday;

    @Column(name="thursday")
    private String thursday;

    @Column(name="friday")
    private String friday;

    public Timetable() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMonday() {
        return monday;
    }

    public void setMonday(String monday) {
        this.monday = monday;
    }

    public String getTuesday() {
        return tuesday;
    }

    public void setTuesday(String tuesday) {
        this.tuesday = tuesday;
    }

    public String getWednesday() {
        return wednesday;
    }

    public void setWednesday(String wednesday) {
        this.wednesday = wednesday;
    }

    public String getThursday() {
        return thursday;
    }

    public void setThursday(String thursday) {
        this.thursday = thursday;
    }

    public String getFriday() {
        return friday;
    }

    public void setFriday(String friday) {
        this.friday = friday;
    }
}
