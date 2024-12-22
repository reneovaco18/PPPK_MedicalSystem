package hr.algebra.medicalsystem.controllers;

import hr.algebra.medicalsystem.entities.MedicalRecord;
import hr.algebra.medicalsystem.services.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @Autowired
    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    // Create a medical record
    @PostMapping
    public ResponseEntity<MedicalRecord> createMedicalRecord(@Validated @RequestBody MedicalRecord medicalRecord) {
        MedicalRecord createdRecord = medicalRecordService.saveMedicalRecord(medicalRecord);
        return new ResponseEntity<>(createdRecord, HttpStatus.CREATED);
    }

    // Get all medical records for a specific patient
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalRecord>> getRecordsByPatient(@PathVariable Long patientId) {
        List<MedicalRecord> records = medicalRecordService.getMedicalRecordsByPatient(patientId);
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    // Get a medical record by ID
    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecord> getMedicalRecordById(@PathVariable Long id) {
        Optional<MedicalRecord> record = medicalRecordService.getMedicalRecordById(id);
        return record.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update a medical record
    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@PathVariable Long id, @Validated @RequestBody MedicalRecord recordDetails) {
        Optional<MedicalRecord> updatedRecord = medicalRecordService.updateMedicalRecord(id, recordDetails);
        return updatedRecord.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Delete a medical record
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable Long id) {
        boolean isDeleted = medicalRecordService.deleteMedicalRecord(id);
        return isDeleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
