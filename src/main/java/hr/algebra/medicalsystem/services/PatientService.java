package hr.algebra.medicalsystem.services;

import hr.algebra.medicalsystem.entities.Patient;
import hr.algebra.medicalsystem.repositories.PatientRepository;
import hr.algebra.medicalsystem.utils.CSVExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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

    // Export patient data to CSV using the CSVExporter utility.
    public String exportPatientsToCSV() {
        List<Patient> patients = patientRepository.findAll();
        String csvFile = "patients.csv";
        CSVExporter exporter = new CSVExporter();
        try (FileWriter fw = new FileWriter(csvFile);
             PrintWriter writer = new PrintWriter(fw)) {
            exporter.writePatientsToCSV(writer, patients);
            return csvFile;
        } catch (IOException e) {
            throw new RuntimeException("Error while exporting patients to CSV", e);
        }
    }

}
