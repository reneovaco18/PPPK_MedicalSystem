package hr.algebra.medicalsystem.services;

import hr.algebra.medicalsystem.entities.MedicalRecord;
import hr.algebra.medicalsystem.repositories.MedicalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;

    @Autowired
    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }


    public MedicalRecord saveMedicalRecord(MedicalRecord medicalRecord) {
        return medicalRecordRepository.save(medicalRecord);
    }


    public List<MedicalRecord> getMedicalRecordsByPatient(Long patientId) {
        return medicalRecordRepository.findByPatientId(patientId);
    }


    public Optional<MedicalRecord> getMedicalRecordById(Long id) {
        return medicalRecordRepository.findById(id);
    }

    public Optional<MedicalRecord> updateMedicalRecord(Long id, MedicalRecord recordDetails) {
        return medicalRecordRepository.findById(id).map(record -> {
            record.setIllnessName(recordDetails.getIllnessName());
            record.setStartDate(recordDetails.getStartDate());
            record.setEndDate(recordDetails.getEndDate());
            return medicalRecordRepository.save(record);
        });
    }


    public boolean deleteMedicalRecord(Long id) {
        if (medicalRecordRepository.existsById(id)) {
            medicalRecordRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
