package szakdoga.service;

import szakdoga.entity.Doctor;

import java.util.List;


public interface DoctorService {

    Doctor findByEmail(String email);
    List<Doctor> getAll();
    void save(Doctor patient);
}
