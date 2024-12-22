package hr.algebra.medicalsystem.services;

import hr.algebra.medicalsystem.entities.Patient;
import hr.algebra.medicalsystem.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    // Save a new patient
    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    // Get all patients
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    // Get a patient by ID
    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    // Get a patient by OIB
    public Optional<Patient> getPatientByOib(String oib) {
        return patientRepository.findByOib(oib);
    }

    // Get patients by last name
    public List<Patient> getPatientsByLastName(String lastName) {
        return patientRepository.findByLastNameContainingIgnoreCase(lastName);
    }

    // Update a patient
    public Optional<Patient> updatePatient(Long id, Patient patientDetails) {
        return patientRepository.findById(id).map(patient -> {
            patient.setFirstName(patientDetails.getFirstName());
            patient.setLastName(patientDetails.getLastName());
            patient.setOib(patientDetails.getOib());
            patient.setDateOfBirth(patientDetails.getDateOfBirth());
            patient.setGender(patientDetails.getGender());
            return patientRepository.save(patient);
        });
    }

    // Delete a patient
    public boolean deletePatient(Long id) {
        if (patientRepository.existsById(id)) {
            patientRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Export patient data to CSV
    public String exportPatientsToCSV() {
        List<Patient> patients = patientRepository.findAll();
        String csvFile = "patients.csv";

        try (FileWriter writer = new FileWriter(csvFile)) {
            writer.append("ID,First Name,Last Name,OIB,Date of Birth,Gender\n");
            for (Patient patient : patients) {
                writer.append(String.valueOf(patient.getId()))
                        .append(',')
                        .append(patient.getFirstName())
                        .append(',')
                        .append(patient.getLastName())
                        .append(',')
                        .append(patient.getOib())
                        .append(',')
                        .append(patient.getDateOfBirth().toString())
                        .append(',')
                        .append(patient.getGender())
                        .append('\n');
            }
            return csvFile;

        } catch (IOException e) {
            throw new RuntimeException("Error while exporting patients to CSV", e);
        }
    }
}
