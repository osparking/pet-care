package com.bumsoap.petcare.controller;

import com.bumsoap.petcare.exception.ResourceNotFoundException;
import com.bumsoap.petcare.model.Appointment;
import com.bumsoap.petcare.response.ApiResponse;
import com.bumsoap.petcare.service.appointment.ServiceAppointment;
import com.bumsoap.petcare.utils.FeedbackMessage;
import com.bumsoap.petcare.utils.UrlMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(UrlMapping.APPOINTMENT)
@RequiredArgsConstructor
public class ControllerAppointment {
    private final ServiceAppointment serviceAppointment;

    @GetMapping(UrlMapping.APPOINTMENT_BY_ID)
    public ResponseEntity<ApiResponse> getAppointmentById(@PathVariable Long id) {
        try {
            return ResponseEntity.status(FOUND)
                    .body(new ApiResponse(FeedbackMessage.FOUND,
                            serviceAppointment.getAppointmentById(id)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    /**
     * 애완동물 수의사 진료 예약을 신청한다.
     *
     * @param appointment 겸진 예약 요청 정보
     * @param senderId 애완동물 쇼유자 ID
     * @param recipientId 수의사 ID
     * @return ResponseEntity<ApiResponse> 반응 상태 및 예약 요청 객체
     */
    @PostMapping(UrlMapping.CREATE)
    public ResponseEntity<ApiResponse> bookAppointment(@RequestBody Appointment appointment,
                                          @RequestParam Long senderId,
                                          @RequestParam Long recipientId) {
        try {
            Appointment savedAppointment = serviceAppointment.createAppointment(
                    appointment, senderId, recipientId);
            return ResponseEntity.status(CREATED)
                    .body(new ApiResponse(FeedbackMessage.BOOKED, savedAppointment));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping(UrlMapping.GET_ALL)
    public ResponseEntity<ApiResponse> getAllAppointments() {
        try {
            return ResponseEntity.status(FOUND)
                    .body(new ApiResponse(FeedbackMessage.FOUND,
                            serviceAppointment.getAllAppointments()));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

}
