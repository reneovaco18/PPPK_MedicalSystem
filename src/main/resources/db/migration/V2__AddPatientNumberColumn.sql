-- This migration adds a new column to the "patients" table
ALTER TABLE patients
    ADD COLUMN patient_number VARCHAR(20);