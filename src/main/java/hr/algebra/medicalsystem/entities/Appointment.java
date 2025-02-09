package hr.algebra.medicalsystem.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_type", nullable = false)
    private ExaminationType type;

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(nullable = false)
    private LocalDateTime dateTime;
}
