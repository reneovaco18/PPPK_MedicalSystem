CREATE TABLE patients (
                          id SERIAL PRIMARY KEY,
                          first_name VARCHAR(50) NOT NULL,
                          last_name VARCHAR(50) NOT NULL,
                          oib VARCHAR(11) NOT NULL UNIQUE,
                          date_of_birth DATE NOT NULL,
                          gender CHAR(1) NOT NULL
);

CREATE TABLE medical_records (
                                 id SERIAL PRIMARY KEY,
                                 patient_id INT NOT NULL,
                                 illness_name VARCHAR(255) NOT NULL,
                                 start_date DATE NOT NULL,
                                 end_date DATE,
                                 CONSTRAINT fk_patient_medical_records
                                     FOREIGN KEY (patient_id) REFERENCES patients(id)
);

CREATE TABLE examinations (
                              id SERIAL PRIMARY KEY,
                              patient_id INT NOT NULL,
                              type VARCHAR(20) NOT NULL,
                              date_time TIMESTAMP NOT NULL,
                              file_path VARCHAR(255),
                              CONSTRAINT fk_patient_examinations
                                  FOREIGN KEY (patient_id) REFERENCES patients(id)
);

CREATE TABLE appointments (
                              id SERIAL PRIMARY KEY,
                              patient_id INT NOT NULL,
                              type VARCHAR(20) NOT NULL,
                              date_time TIMESTAMP NOT NULL,
                              CONSTRAINT fk_patient_appointments
                                  FOREIGN KEY (patient_id) REFERENCES patients(id)
);
