package szakdoga.service;

import szakdoga.entity.Doctor;
import szakdoga.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/home/dba")
public class UserResource {

    @Autowired
    DoctorRepository doctorRepository;


    @GetMapping(value="/all")
    public List<Doctor> getAll(){
        return doctorRepository.findAll();
    }

    @PostMapping(value = "/load")
    public List<Doctor> persist(@RequestBody final Doctor users) {
        doctorRepository.save(users);
        return doctorRepository.findAll();
    }
}
