package szakdoga.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import szakdoga.entity.Doctor;
import szakdoga.repository.DoctorRepository;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService{

    @Autowired
    DoctorRepository doctorRepository;

    @Override
    public Doctor findByEmail(String email) {
        return doctorRepository.findByEmail(email);
    }

    @Override
    public List<Doctor> getAll() {
        return doctorRepository.findAll();
    }

    @Override
    public void save(Doctor doctor) {
        doctorRepository.saveAndFlush(doctor);
    }

}
