package szakdoga.service;

import szakdoga.entity.Appointment;

import java.util.List;

public interface AppointmentService {

    void save(Appointment appointment);

    List<Appointment> getAll();
}
