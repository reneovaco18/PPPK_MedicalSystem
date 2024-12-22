package hr.algebra.medicalsystem.repositories;

import hr.algebra.medicalsystem.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByOib(String oib);

    List<Patient> findByLastNameContainingIgnoreCase(String lastName);
}
