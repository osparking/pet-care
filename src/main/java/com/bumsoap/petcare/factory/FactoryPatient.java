package com.bumsoap.petcare.factory;

import com.bumsoap.petcare.model.Patient;
import com.bumsoap.petcare.model.User;
import com.bumsoap.petcare.repository.RepositoryPatient;
import com.bumsoap.petcare.request.RegistrationRequest;
import com.bumsoap.petcare.service.user.UserAttributesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FactoryPatient {
    private final RepositoryPatient repositoryPatient;
    private final UserAttributesMapper userAttributesMapper;

    public Patient createPatient(RegistrationRequest request) {
        Patient patient = new Patient();
        userAttributesMapper.setCommonAttributes(request, patient);
        return repositoryPatient.save(patient);
    }
}
