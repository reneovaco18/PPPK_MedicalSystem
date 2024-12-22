package hr.algebra.medicalsystem.utils;

import hr.algebra.medicalsystem.entities.Examination;
import hr.algebra.medicalsystem.entities.MedicalRecord;
import hr.algebra.medicalsystem.entities.Patient;

import java.io.PrintWriter;
import java.util.List;

public class CSVExporter {

    public static void writePatientsToCSV(PrintWriter writer, List<Patient> patients) {
        writer.println("ID,First Name,Last Name,OIB,Date of Birth,Gender,Medical Records,Examinations");

        for (Patient patient : patients) {
            StringBuilder medicalRecords = new StringBuilder();
            for (MedicalRecord record : patient.getMedicalRecords()) {
                medicalRecords.append(record.getIllnessName())
                        .append(" (")
                        .append(record.getStartDate())
                        .append(" - ")
                        .append(record.getEndDate() == null ? "Present" : record.getEndDate())
                        .append("); ");
            }

            StringBuilder examinations = new StringBuilder();
            for (Examination examination : patient.getExaminations()) {
                examinations.append(examination.getType())
                        .append(" (")
                        .append(examination.getDateTime())
                        .append("); ");
            }

            writer.printf("%d,%s,%s,%s,%s,%s,\"%s\",\"%s\"%n",
                    patient.getId(),
                    patient.getFirstName(),
                    patient.getLastName(),
                    patient.getOib(),
                    patient.getDateOfBirth(),
                    patient.getGender(),
                    medicalRecords.toString(),
                    examinations.toString()
            );
        }
    }
}
