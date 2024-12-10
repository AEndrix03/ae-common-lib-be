package it.aredegalli.commons.controller;

import it.aredegalli.commons.model.User;
import it.aredegalli.commons.service.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return this.userService.getUserById(id);
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
