package szakdoga.service;

import szakdoga.entity.Timetable;

import java.util.Optional;

public interface TimetableService {

    //Timetable findByEmail(String email);
    Optional<Timetable> getTimetable(Integer id);
    void save(Timetable timetable);
}
