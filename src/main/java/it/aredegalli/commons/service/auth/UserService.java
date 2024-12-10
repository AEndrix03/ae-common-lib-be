package it.aredegalli.commons.service.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.aredegalli.commons.dto.UserDto;
import it.aredegalli.commons.dto.auth.LoginDto;
import it.aredegalli.commons.dto.auth.RegisterDto;
import it.aredegalli.commons.model.User;

public interface UserService {
    UserDto login(LoginDto loginDto) throws JsonProcessingException;

    UserDto register(RegisterDto registerDto) throws JsonProcessingException;

    Boolean existsUsername(String username);

    Boolean existsEmail(String email);

    User getUser(String login);

    User getUserInfo();

    User getUserById(Long id);

    UserDto refreshToken(String token) throws JsonProcessingException;
}
