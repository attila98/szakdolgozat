package szakdoga.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import szakdoga.service.DoctorService;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Component
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

    @Autowired
    @Transient
    DoctorService doctorService;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public String getFormattedDate(){
        String dateString = String.valueOf(date);
        int space=dateString.indexOf(" ");
        dateString=dateString.substring(0,space);
        return dateString;
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

    public String getDoctorNameById(){
        List<Doctor> allDoctor = doctorService.getAll();
        for(Doctor doctor : allDoctor){
            if (doctor.getId()==this.id){
                return doctor.getFullName();
            }
        }
        return "Hiba";
    }
}
