package szakdoga.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import szakdoga.entity.Appointment;

import java.util.Optional;


@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Integer> {

    Optional<Appointment> findById(Integer id);
}
