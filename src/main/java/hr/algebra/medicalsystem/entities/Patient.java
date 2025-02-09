package hr.algebra.medicalsystem.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String lastName;

    @NotBlank
    @Size(min = 11, max = 11)
    @Column(nullable = false, unique = true, length = 11)
    private String oib;

    @NotNull
    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @NotBlank
    @Pattern(regexp = "[MF]", message = "Gender must be 'M' or 'F'")
    @Column(nullable = false, length = 1)
    private String gender;


    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<MedicalRecord> medicalRecords;


    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Examination> examinations;


    @JsonManagedReference
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Medication> medications;


    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Appointment> appointments;

    @Column(name = "patient_number", length = 20)
    private String patientNumber;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
