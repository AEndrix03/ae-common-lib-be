package it.aredegalli.commons.service.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.aredegalli.commons.config.properties.CommonProperties;
import it.aredegalli.commons.dto.UserDto;
import it.aredegalli.commons.dto.auth.LoginDto;
import it.aredegalli.commons.dto.auth.RegisterDto;
import it.aredegalli.commons.enums.auth.LoginTypeEnum;
import it.aredegalli.commons.model.SecUser;
import it.aredegalli.commons.model.User;
import it.aredegalli.commons.repository.auth.UserRepository;
import it.aredegalli.commons.security.crypto.AESCrypto;
import it.aredegalli.commons.security.crypto.Hash;
import it.aredegalli.commons.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AESCrypto crypto;
    private final Hash hash;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserProvider userProvider;
    private final UserDetailsService userDetailsService;
    private final LoginTypeEnum loginType;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, AESCrypto crypto, Hash hash, AuthenticationManager authenticationManager, JwtService jwtService,
                           UserProvider userProvider, UserDetailsService userDetailsService, CommonProperties commonProperties) {
        this.userRepository = userRepository;
        this.crypto = crypto;
        this.hash = hash;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userProvider = userProvider;
        this.userDetailsService = userDetailsService;
        this.loginType = commonProperties.getAuth().getLoginType();
    }

    @Override
    public UserDto login(LoginDto loginDto) throws JsonProcessingException {
        SecUser user = this.authenticate(loginDto);
        String token = this.jwtService.generateToken(user);

        return UserDto.builder()
                .token(token)
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .active(user.getActive())
                .build();
    }

    @Override
    public UserDto register(RegisterDto registerDto) throws JsonProcessingException {
        SecUser user = this._getUser(this.getLogin(registerDto));

        if (user != null) {
            throw new IllegalArgumentException("User already exists");
        }

        user = new SecUser();
        user.setEmail(registerDto.getEmail());
        user.setUsername(registerDto.getUsername());
        user.setName(registerDto.getName());
        user.setSurname(registerDto.getSurname());
        user.setPassword(this.hash.hashPassword(registerDto.getPassword()));
        user.setActive(true);
        encryptUserInfo(user);
        this.userRepository.save(user);

        return this.login(registerDto);
    }

    @Override
    public Boolean existsUsername(String username) {
        return this.userRepository.getSecUserByUsername(username) != null;
    }

    @Override
    public Boolean existsEmail(String email) {
        return this.userRepository.getSecUserByEmail(email) != null;
    }

    @Override
    public User getUser(String login) {
        return this._getUser(login);
    }

    @Override
    public User getUserInfo() {
        return this.userProvider.getUser();
    }

    @Override
    public User getUserById(Long id) {
        return this.userRepository.findById(id).orElse(null);
    }

    @Override
    public UserDto refreshToken(String token) throws JsonProcessingException {
        if (this.jwtService.isTokenExpired(token.substring(7))) {
            return null;
        }
        UserDetailsDecorator userDetails = (UserDetailsDecorator) this.userDetailsService.loadUserByUsername(this.jwtService.extractUsername(token.substring(7)));

        if (userDetails == null) {
            return null;
        }
        User user = this.getUser(userDetails.getAuthUsername(this.loginType));
        decryptUserInfo((SecUser) user);
        return UserDto.builder()
                .token(this.jwtService.generateToken((SecUser) user))
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .active(user.getActive())
                .build();
    }

    private SecUser authenticate(LoginDto loginDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        this.getLogin(loginDto),
                        loginDto.getPassword()
                )
        );

        SecUser user = this._getUser(this.getLogin(loginDto));
        this.userProvider.setUser(user);

        return user;
    }

    private String getLogin(LoginDto loginDto) {
        return switch (loginType) {
            case EMAIL -> loginDto.getEmail();
            case USERNAME -> loginDto.getUsername();
        };
    }

    private SecUser _getUser(String login) {
        SecUser user = switch (loginType) {
            case EMAIL -> this.userRepository.getSecUserByEmail(login);
            case USERNAME -> this.userRepository.getSecUserByUsername(login);
        };
        decryptUserInfo(user);
        return user;
    }

    private void decryptUserInfo(SecUser authUser) {
        if (authUser == null) {
            return;
        }

        try {
            authUser.setName(this.crypto.decrypt(authUser.getName()));
            authUser.setSurname(this.crypto.decrypt(authUser.getSurname()));
        } catch (Exception e) {
            throw new InternalError("Error");
        }
    }

    private void encryptUserInfo(SecUser authUser) {
        if (authUser == null) {
            return;
        }

        try {
            authUser.setName(this.crypto.encrypt(authUser.getName()));
            authUser.setSurname(this.crypto.encrypt(authUser.getSurname()));
        } catch (Exception e) {
            throw new InternalError("Error");
        }
    }

}
