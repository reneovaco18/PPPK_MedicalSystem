package hr.algebra.medicalsystem.controllers;

import hr.algebra.medicalsystem.entities.Patient;
import hr.algebra.medicalsystem.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
@Validated
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Patient> createPatient(@Validated @RequestBody Patient patient) {
        Patient created = patientService.savePatient(patient);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // READ all
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    // READ by id
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        Optional<Patient> patient = patientService.getPatientById(id);
        return patient.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // READ by OIB
    @GetMapping("/search/oib")
    public ResponseEntity<Patient> searchPatientByOib(@RequestParam String oib) {
        return patientService.getPatientByOib(oib)
                .map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // READ by last name
    @GetMapping("/search/lastName")
    public ResponseEntity<List<Patient>> searchPatientsByLastName(@RequestParam String lastName) {
        List<Patient> results = patientService.getPatientsByLastName(lastName);
        if (results.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id,
                                                 @Validated @RequestBody Patient details) {
        Optional<Patient> updated = patientService.updatePatient(id, details);
        return updated.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        boolean isDeleted = patientService.deletePatient(id);
        return isDeleted
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // CSV Export
    @GetMapping("/export")
    public ResponseEntity<Resource> exportPatientsToCSV() {
        try {
            String filePath = patientService.exportPatientsToCSV();
            File file = new File(filePath);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=patients.csv")
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .contentLength(file.length())
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
