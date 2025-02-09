package hr.algebra.medicalsystem.services;

import hr.algebra.medicalsystem.entities.Appointment;
import hr.algebra.medicalsystem.repositories.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }


    public Appointment saveAppointment(Appointment appointment) {
        if (appointment.getDateTime() != null) {
            appointment.setAppointmentDate(appointment.getDateTime().toLocalDate());
        } else {
            throw new IllegalArgumentException("dateTime must not be null");
        }
        return appointmentRepository.save(appointment);
    }


    public List<Appointment> getAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }


    public Optional<Appointment> updateAppointment(Long id, Appointment appointmentDetails) {
        return appointmentRepository.findById(id).map(appt -> {
            appt.setPatient(appointmentDetails.getPatient());
            appt.setType(appointmentDetails.getType());
            if (appointmentDetails.getDateTime() == null) {
                throw new IllegalArgumentException("dateTime must not be null");
            }
            appt.setDateTime(appointmentDetails.getDateTime());
            appt.setAppointmentDate(appointmentDetails.getDateTime().toLocalDate());
            return appointmentRepository.save(appt);
        });
    }


    public boolean deleteAppointment(Long id) {
        if (appointmentRepository.existsById(id)) {
            appointmentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
