package hr.algebra.medicalsystem.controllers;

import hr.algebra.medicalsystem.entities.Examination;
import hr.algebra.medicalsystem.services.ExaminationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/examinations")
public class ExaminationController {

    private final ExaminationService examinationService;

    @Autowired
    public ExaminationController(ExaminationService examinationService) {
        this.examinationService = examinationService;
    }


    @PostMapping
    public ResponseEntity<Examination> createExamination(@Validated @RequestBody Examination examination) {
        Examination createdExamination = examinationService.saveExamination(examination);
        return new ResponseEntity<>(createdExamination, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<Examination>> getAllExaminations() {
        List<Examination> examinations = examinationService.getAllExaminations();
        return new ResponseEntity<>(examinations, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Examination> getExaminationById(@PathVariable Long id) {
        Optional<Examination> examination = examinationService.getExaminationById(id);
        return examination.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Examination>> getExaminationsByPatient(@PathVariable Long patientId) {
        List<Examination> examinations = examinationService.getExaminationsByPatient(patientId);
        return new ResponseEntity<>(examinations, HttpStatus.OK);
    }


    @GetMapping("/type/{type}")
    public ResponseEntity<List<Examination>> getExaminationsByType(@PathVariable String type) {
        List<Examination> examinations = examinationService.getExaminationsByType(type);
        return new ResponseEntity<>(examinations, HttpStatus.OK);
    }


    @GetMapping("/date-range")
    public ResponseEntity<List<Examination>> getExaminationsByDateRange(
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam("endDate") LocalDateTime endDate) {
        List<Examination> examinations = examinationService.getExaminationsByDateRange(startDate, endDate);
        return new ResponseEntity<>(examinations, HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Examination> updateExamination(@PathVariable Long id, @Validated @RequestBody Examination examination) {
        Optional<Examination> updatedExamination = examinationService.updateExamination(id, examination);
        return updatedExamination.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExamination(@PathVariable Long id) {
        boolean isDeleted = examinationService.deleteExamination(id);
        return isDeleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
