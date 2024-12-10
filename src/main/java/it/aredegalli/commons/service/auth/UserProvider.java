package it.aredegalli.commons.service.auth;

import it.aredegalli.commons.dto.UserDto;
import it.aredegalli.commons.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class UserProvider {

    @Setter
    private User user = new UserDto();

    public void clear() {
        user = new UserDto();
    }

}
