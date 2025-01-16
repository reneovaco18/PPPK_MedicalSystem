package hr.algebra.medicalsystem.controllers;

import hr.algebra.medicalsystem.services.ExaminationService;
import hr.algebra.medicalsystem.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/examinations")
public class ExaminationFileController {

    private final FileStorageService fileStorageService;
    private final ExaminationService examinationService;

    @Autowired
    public ExaminationFileController(FileStorageService fileStorageService, ExaminationService examinationService) {
        this.fileStorageService = fileStorageService;
        this.examinationService = examinationService;
    }

    @PostMapping("/{id}/upload")
    public ResponseEntity<String> uploadFile(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        String filePath = fileStorageService.storeFile(file);
        boolean isUpdated = examinationService.updateFilePath(id, filePath);

        if (isUpdated) {
            return new ResponseEntity<>("File uploaded successfully: " + filePath, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Examination not found", HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/{id}/file")
    public ResponseEntity<String> getFilePath(@PathVariable Long id) {
        return examinationService.getExaminationById(id)
                .map(exam -> new ResponseEntity<>(exam.getFilePath(), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
