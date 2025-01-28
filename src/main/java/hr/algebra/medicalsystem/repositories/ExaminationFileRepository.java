package hr.algebra.medicalsystem.repositories;

import hr.algebra.medicalsystem.entities.ExaminationFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExaminationFileRepository extends JpaRepository<ExaminationFile, Long> {
    // Typically, you might add custom finders if you need them
    // e.g. findByExaminationId(Long examId)
}
