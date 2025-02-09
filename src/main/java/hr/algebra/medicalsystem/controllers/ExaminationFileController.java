package hr.algebra.medicalsystem.controllers;

import hr.algebra.medicalsystem.entities.ExaminationFile;
import hr.algebra.medicalsystem.services.ExaminationFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/examination-files")
public class ExaminationFileController {

    private final ExaminationFileService fileService;

    @Autowired
    public ExaminationFileController(ExaminationFileService fileService) {
        this.fileService = fileService;
    }


    @PostMapping("/{examId}/upload")
    public ResponseEntity<String> uploadSingleFile(
            @PathVariable Long examId,
            @RequestParam("file") MultipartFile file
    ) {
        Optional<ExaminationFile> savedFile = fileService.storeFileForExamination(examId, file);
        if (savedFile.isPresent()) {
            return new ResponseEntity<>("Uploaded file: " + savedFile.get().getFilePath(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Examination not found", HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/{examId}/upload-multiple")
    public ResponseEntity<String> uploadMultipleFiles(
            @PathVariable Long examId,
            @RequestParam("files") MultipartFile[] files
    ) {
        boolean success = fileService.storeMultipleFilesForExamination(examId, files);
        if (success) {
            return new ResponseEntity<>("All files uploaded successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Examination not found", HttpStatus.NOT_FOUND);
        }
    }
}
