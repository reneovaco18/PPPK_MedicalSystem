package hr.algebra.medicalsystem.services;

import hr.algebra.medicalsystem.entities.Examination;
import hr.algebra.medicalsystem.repositories.ExaminationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ExaminationService {

    private final ExaminationRepository examinationRepository;

    @Autowired
    public ExaminationService(ExaminationRepository examinationRepository) {
        this.examinationRepository = examinationRepository;
    }

    public Examination saveExamination(Examination examination) {
        return examinationRepository.save(examination);
    }

    public List<Examination> getAllExaminations() {
        return examinationRepository.findAll();
    }

    public Optional<Examination> getExaminationById(Long id) {
        return examinationRepository.findById(id);
    }

    public List<Examination> getExaminationsByPatient(Long patientId) {
        return examinationRepository.findByPatientId(patientId);
    }

    public List<Examination> getExaminationsByType(String type) {
        return examinationRepository.findByType(type);
    }

    public List<Examination> getExaminationsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return examinationRepository.findByDateTimeBetween(startDate, endDate);
    }

    public Optional<Examination> updateExamination(Long id, Examination updatedExamination) {
        return examinationRepository.findById(id).map(examination -> {
            examination.setDateTime(updatedExamination.getDateTime());
            examination.setType(updatedExamination.getType());
            // Do not modify the files list here; assume file uploads are managed separately.
            return examinationRepository.save(examination);
        });
    }

    public boolean deleteExamination(Long id) {
        if (examinationRepository.existsById(id)) {
            examinationRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
