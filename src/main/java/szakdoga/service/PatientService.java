package szakdoga.service;

import szakdoga.entity.Patient;

public interface PatientService {

    Patient findByEmail(String email);
    void save(Patient patient);
}
