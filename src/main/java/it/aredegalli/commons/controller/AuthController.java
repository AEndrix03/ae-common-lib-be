package it.aredegalli.commons.controller;

import it.aredegalli.commons.model.User;
import it.aredegalli.commons.service.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/exists/username")
    public Boolean existsUsername(@RequestParam String username) {
        return this.userService.existsUsername(username);
    }

    @GetMapping("/exists/email")
    public Boolean existsEmail(@RequestParam String email) {
        return this.userService.existsEmail(email);
    }

}
