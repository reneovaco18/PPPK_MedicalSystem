package hr.algebra.medicalsystem.utils;

import hr.algebra.medicalsystem.entities.Examination;
import hr.algebra.medicalsystem.entities.MedicalRecord;
import hr.algebra.medicalsystem.entities.Medication;
import hr.algebra.medicalsystem.entities.Patient;

import java.io.PrintWriter;
import java.util.List;

public class CSVExporter {

    public void writePatientsToCSV(PrintWriter writer, List<Patient> patients) {

        writer.write('\uFEFF');


        writer.println("sep=,");


        writer.println("ID,First Name,Last Name,OIB,Date of Birth,Gender,Medical Records,Examinations,Medications");

        for (Patient patient : patients) {

            StringBuilder medicalRecords = new StringBuilder();
            if (patient.getMedicalRecords() != null) {
                for (MedicalRecord record : patient.getMedicalRecords()) {
                    medicalRecords.append(record.getIllnessName())
                            .append(" (")
                            .append(record.getStartDate())
                            .append(" - ")
                            .append(record.getEndDate() == null ? "Present" : record.getEndDate())
                            .append("); ");
                }
            }


            StringBuilder examinations = new StringBuilder();
            if (patient.getExaminations() != null) {
                for (Examination exam : patient.getExaminations()) {
                    examinations.append(exam.getType())
                            .append(" (")
                            .append(exam.getDateTime())
                            .append("); ");
                }
            }


            StringBuilder meds = new StringBuilder();
            if (patient.getMedications() != null) {
                for (Medication med : patient.getMedications()) {
                    meds.append(med.getName())
                            .append(" (")
                            .append(med.getDosage())
                            .append("); ");
                }
            }


            String id = escapeSpecialCharacters(String.valueOf(patient.getId()));
            String firstName = escapeSpecialCharacters(patient.getFirstName());
            String lastName = escapeSpecialCharacters(patient.getLastName());
            String oib = escapeSpecialCharacters(patient.getOib());
            String dob = escapeSpecialCharacters(String.valueOf(patient.getDateOfBirth()));
            String gender = escapeSpecialCharacters(patient.getGender());
            String records = escapeSpecialCharacters(medicalRecords.toString());
            String exams = escapeSpecialCharacters(examinations.toString());
            String medications = escapeSpecialCharacters(meds.toString());


            writer.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s%n",
                    id, firstName, lastName, oib, dob, gender,
                    records, exams, medications);
        }
    }


    private String escapeSpecialCharacters(String data) {
        if (data == null) {
            return "";
        }
        String escaped = data.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\"") || escaped.contains("\n")) {
            escaped = "\"" + escaped + "\"";
        }
        return escaped;
    }
}
