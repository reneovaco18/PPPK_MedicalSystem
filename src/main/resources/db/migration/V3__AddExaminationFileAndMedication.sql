

ALTER TABLE patients
    ADD COLUMN IF NOT EXISTS patient_number VARCHAR(20);


CREATE TABLE IF NOT EXISTS medication (
                                          id BIGSERIAL PRIMARY KEY,
                                          patient_id BIGINT NOT NULL,
                                          name VARCHAR(255) NOT NULL,
    dosage VARCHAR(255) NOT NULL,
    CONSTRAINT fk_medication_patient
    FOREIGN KEY (patient_id) REFERENCES patients(id)
    ON DELETE CASCADE
    );


CREATE TABLE IF NOT EXISTS examination_file (
                                                id BIGSERIAL PRIMARY KEY,
                                                examination_id BIGINT NOT NULL,
                                                file_path VARCHAR(1000),
    CONSTRAINT fk_examination
    FOREIGN KEY (examination_id) REFERENCES examinations(id)
    ON DELETE CASCADE
    );


CREATE INDEX IF NOT EXISTS idx_examinationfile_examination_id
    ON examination_file(examination_id);

