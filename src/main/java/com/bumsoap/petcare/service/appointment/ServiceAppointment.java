package com.bumsoap.petcare.service.appointment;

import com.bumsoap.petcare.exception.ResourceNotFoundException;
import com.bumsoap.petcare.model.Appointment;
import com.bumsoap.petcare.model.User;
import com.bumsoap.petcare.repository.RepositoryAppointment;
import com.bumsoap.petcare.repository.RepositoryUser;
import com.bumsoap.petcare.request.AppointmentUpdateRequest;
import com.bumsoap.petcare.utils.FeedbackMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.bumsoap.petcare.utils.StatusAppointment.APPROVE_WAIT;

@Service
@RequiredArgsConstructor
public class ServiceAppointment implements IServiceAppointment {

    private final RepositoryAppointment repositoryAppointment;
    private final RepositoryUser repositoryUser;

    @Override
    /** 두 ID 로 두 관련자를 읽고, 이를 예약 객체에 지정하고,
     * 두 속성 예약번호, 예약 상태도 지정한다. 관련자 중 한 명이라도 없으면, 예외를 던진다.
     * @param appointment - 저장할 예약 객체
     *                    senderId - 환자/pet 주인 ID
     *                    recipientId - 수의사/vet' ID
     * return - 저장된 예약 객체
     * throws - ResourceNotFoundException
     *
     **/
    public Appointment createAppointment(
            Appointment appointment, Long senderId, Long recipientId) {

        Optional<User> patient = repositoryUser.findById(senderId);
        Optional<User> veterinarian = repositoryUser.findById(recipientId);

        if (patient.isPresent() && veterinarian.isPresent()) {
            appointment.addPatient(patient.get());
            appointment.addVeterinarian (veterinarian.get());
            appointment.setAppointmentNo();
            appointment.setStatus(APPROVE_WAIT);
            return repositoryAppointment.save(appointment);
        } else {
            throw new ResourceNotFoundException(
                    FeedbackMessage.PATIENT_OR_VETERINARIAN_NOT_FOUND);
        }
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return repositoryAppointment.findAll();
    }

    @Override
    public Appointment updateAppointment(Long id, AppointmentUpdateRequest request) {
        Appointment oldAppointment = getAppointmentById(id);
        if (!Objects.equals(oldAppointment.getStatus(), APPROVE_WAIT)) {
            throw new IllegalStateException(
                    FeedbackMessage.ILLEGAL_APPOINTMENT_UPDATE);
        }
        oldAppointment.setDate(LocalDate.parse(request.getAppointmentDate()));
        oldAppointment.setTime(LocalTime.parse(request.getAppointmentTime()));
        oldAppointment.setReason(request.getReason());

        return repositoryAppointment.save(oldAppointment);
    }

    @Override
    public void deleteAppointment(Long id) {
        repositoryAppointment
                .findById(id)
                .ifPresentOrElse(repositoryAppointment::delete,
                        () -> {
                    throw new ResourceNotFoundException(
                            FeedbackMessage.NOT_FOUND_USERID);
        });
    }

    @Override
    public Appointment getAppointmentById(Long appointmentId) {
        return repositoryAppointment
                .findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        FeedbackMessage.NOT_FOUND_USERID));
    }

    @Override
    public Appointment getByAppointmentNo(String appointmentNo) {
        return repositoryAppointment.findByAppointmentNo(appointmentNo);
    }
}
