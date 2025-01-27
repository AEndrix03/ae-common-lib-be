package it.aredegalli.commons.service.auth;

import it.aredegalli.commons.enums.auth.LoginTypeEnum;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsDecorator extends UserDetails {

    default String getAuthUsername(LoginTypeEnum loginType) {
        return switch (loginType) {
            case USERNAME -> getUsername();
            case EMAIL -> getEmail();
        };
    }

    String getEmail();

    @Override
    String getUsername();

}
