package it.aredegalli.commons.controller;

import it.aredegalli.commons.dto.auth.LoginDto;
import it.aredegalli.commons.dto.auth.RegisterDto;
import it.aredegalli.commons.model.User;
import it.aredegalli.commons.service.auth.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/info")
    public User getUserInfo() {
        return this.userService.getUserInfo();
    }

    @SneakyThrows
    @PostMapping("/login")
    public User login(@RequestBody LoginDto loginDto) {
        return this.userService.login(loginDto);
    }

    @SneakyThrows
    @PostMapping("/register")
    public User register(@RequestBody RegisterDto registerDto) {
        return this.userService.register(registerDto);
    }
}
