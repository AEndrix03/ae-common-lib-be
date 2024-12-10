package it.aredegalli.commons.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.aredegalli.commons.dto.UserDto;
import it.aredegalli.commons.service.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/token")
public class TokenController {

    private final UserService userService;

    @Autowired
    public TokenController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public UserDto refreshToken(@RequestHeader("Authorization") String authHeader) throws JsonProcessingException {
        return this.userService.refreshToken(authHeader);
    }

}
