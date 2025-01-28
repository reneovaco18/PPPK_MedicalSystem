package hr.algebra.medicalsystem.repositories;

import hr.algebra.medicalsystem.entities.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {
    // For searching by patient
    List<Medication> findByPatientId(Long patientId);
}
