package com.bumsoap.petcare.controller;

import com.bumsoap.petcare.model.User;
import com.bumsoap.petcare.service.ServiceUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class ControllerUser {
    private final ServiceUser serviceUser;

    @PostMapping
    public User add(@RequestBody User user) {
        return serviceUser.add(user);
    }
}
