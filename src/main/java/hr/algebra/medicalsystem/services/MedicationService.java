package hr.algebra.medicalsystem.services;

import hr.algebra.medicalsystem.entities.Medication;
import hr.algebra.medicalsystem.repositories.MedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicationService {

    private final MedicationRepository medicationRepository;

    @Autowired
    public MedicationService(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    public Medication save(Medication medication) {
        return medicationRepository.save(medication);
    }

    public List<Medication> findAll() {
        return medicationRepository.findAll();
    }

    public Optional<Medication> findById(Long id) {
        return medicationRepository.findById(id);
    }

    public List<Medication> findByPatient(Long patientId) {
        return medicationRepository.findByPatientId(patientId);
    }

    public Optional<Medication> updateMedication(Long id, Medication updatedMedication) {
        return medicationRepository.findById(id).map(m -> {
            m.setName(updatedMedication.getName());
            m.setDosage(updatedMedication.getDosage());
            m.setPatient(updatedMedication.getPatient());
            return medicationRepository.save(m);
        });
    }

    public boolean deleteMedication(Long id) {
        if (medicationRepository.existsById(id)) {
            medicationRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
