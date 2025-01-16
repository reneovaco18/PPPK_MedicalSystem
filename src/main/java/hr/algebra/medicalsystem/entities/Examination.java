package hr.algebra.medicalsystem.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Examination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExaminationType type;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime dateTime;

    private String filePath; // Path to the uploaded file
}

