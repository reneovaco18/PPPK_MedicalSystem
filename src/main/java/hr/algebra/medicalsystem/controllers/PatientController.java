package hr.algebra.medicalsystem.controllers;

import hr.algebra.medicalsystem.entities.Patient;
import hr.algebra.medicalsystem.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public ResponseEntity<Patient> createPatient(@Validated @RequestBody Patient patient) {
        Patient createdPatient = patientService.savePatient(patient);
        return new ResponseEntity<>(createdPatient, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Optional<Patient> patient = patientService.getPatientById(id);
        return patient.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/search/oib")
    public ResponseEntity<Patient> searchPatientByOib(@RequestParam String oib) {
        return patientService.getPatientByOib(oib)
                .map(patient -> new ResponseEntity<>(patient, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/search/lastName")
    public ResponseEntity<List<Patient>> searchPatientsByLastName(@RequestParam String lastName) {
        List<Patient> patients = patientService.getPatientsByLastName(lastName);
        return patients.isEmpty() ? new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @Validated @RequestBody Patient patientDetails) {
        Optional<Patient> updatedPatient = patientService.updatePatient(id, patientDetails);
        return updatedPatient.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        boolean isDeleted = patientService.deletePatient(id);
        return isDeleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/export")
    public ResponseEntity<String> exportPatientsToCSV() {
        try {
            String filePath = patientService.exportPatientsToCSV();
            return new ResponseEntity<>("Patients exported successfully. File path: " + filePath, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error exporting patients: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
