package szakdoga.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name ="appointment")
public class Appointment {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name="date")
    private Date date;

    @Column(name="startapp")
    private String startapp;

    @Column(name="endapp")
    private String endapp;

    @Column(name="patient_id")
    private Integer patient_id;

    @Column(name="doctor_id")
    private Integer doctor_id;

    public Appointment() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStartapp() {
        return startapp;
    }

    public void setStartapp(String startapp) {
        this.startapp = startapp;
    }

    public String getEndapp() {
        return endapp;
    }

    public void setEndapp(String endapp) {
        this.endapp = endapp;
    }

    public Integer getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(Integer patient_id) {
        this.patient_id = patient_id;
    }

    public Integer getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(Integer doctor_id) {
        this.doctor_id = doctor_id;
    }
}
