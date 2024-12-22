package hr.algebra.medicalsystem.repositories;

import hr.algebra.medicalsystem.entities.Examination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExaminationRepository extends JpaRepository<Examination, Long> {
    List<Examination> findByPatientId(Long patientId);

    List<Examination> findByType(String type);

    List<Examination> findByDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
}
