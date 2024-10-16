package com.bumsoap.petcare.service.user;

import com.bumsoap.petcare.dto.DtoUser;
import com.bumsoap.petcare.dto.EntityConverter;
import com.bumsoap.petcare.exception.ResourceNotFoundException;
import com.bumsoap.petcare.factory.FactoryUser;
import com.bumsoap.petcare.model.User;
import com.bumsoap.petcare.repository.RepositoryUser;
import com.bumsoap.petcare.request.RegistrationRequest;
import com.bumsoap.petcare.request.UserUpdateRequest;
import com.bumsoap.petcare.utils.FeedbackMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceUser implements IServiceUser {
    private final FactoryUser userFactory;
    private final RepositoryUser repositoryUser;
    private final EntityConverter<User, DtoUser> entityConverter;

    @Override
    public User register(RegistrationRequest request) {
        return userFactory.register(request);
    }

    @Override
    public User update(Long userId, UserUpdateRequest request) {
        User user = findById(userId);

        user.setPhoneNumber(request.getPhoneNumber());
        user.setGender(request.getGender());
        user.setSpecialization(request.getSpecialization());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        return repositoryUser.save(user);
    }

    @Override
    public void deleteById(Long userId) {
        repositoryUser.findById(userId).ifPresentOrElse(repositoryUser::delete, ()
                -> {
            throw new ResourceNotFoundException(FeedbackMessage.NOT_FOUND);
        });
    }

    @Override
    public User findById(Long userId) {
        return repositoryUser.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException(FeedbackMessage.NOT_FOUND));
    }

    @Override
    public List<DtoUser> getAllUsers() {
        return repositoryUser.findAll().stream().map
                        (user -> entityConverter.mapEntityToDto(user, DtoUser.class))
                .collect(Collectors.toList());
    }
}
