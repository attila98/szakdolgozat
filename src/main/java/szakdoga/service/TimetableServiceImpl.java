package szakdoga.service;

import szakdoga.entity.Timetable;
import szakdoga.repository.TimetableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TimetableServiceImpl implements TimetableService {

    @Autowired
    TimetableRepository timetableRepository;

    public Iterable<Timetable> getAll(){
        return timetableRepository.findAll();
    }

    @Override
    public Optional<Timetable> getTimetable(Integer id){
        return timetableRepository.findById(id);
    }


    @Override
    public void save(Timetable timetable) { timetableRepository.saveAndFlush(timetable);
    }
}
