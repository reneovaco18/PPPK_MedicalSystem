-- V3__AddExaminationFileAndMedication.sql

--------------------------------------------------------
-- 1) Add 'patient_number' column to 'patients'
--------------------------------------------------------
ALTER TABLE patients
    ADD COLUMN IF NOT EXISTS patient_number VARCHAR(20);

--------------------------------------------------------
-- 2) Create 'medication' table
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS medication (
                                          id BIGSERIAL PRIMARY KEY,
                                          patient_id BIGINT NOT NULL,
                                          name VARCHAR(255) NOT NULL,
    dosage VARCHAR(255) NOT NULL,
    CONSTRAINT fk_medication_patient
    FOREIGN KEY (patient_id) REFERENCES patients(id)
    ON DELETE CASCADE
    );

--------------------------------------------------------
-- 3) Create 'examination_file' table
--------------------------------------------------------
CREATE TABLE IF NOT EXISTS examination_file (
                                                id BIGSERIAL PRIMARY KEY,
                                                examination_id BIGINT NOT NULL,
                                                file_path VARCHAR(1000),
    CONSTRAINT fk_examination
    FOREIGN KEY (examination_id) REFERENCES examinations(id)
    ON DELETE CASCADE
    );

-- For convenience, add an index if you often query by exam ID:
CREATE INDEX IF NOT EXISTS idx_examinationfile_examination_id
    ON examination_file(examination_id);

-- Done! This script adds the new medication + exam_file structure.
