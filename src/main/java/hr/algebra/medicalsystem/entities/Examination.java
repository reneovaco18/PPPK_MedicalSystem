package hr.algebra.medicalsystem.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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


    @OneToMany(mappedBy = "examination", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ExaminationFile> files = new ArrayList<>();
}
