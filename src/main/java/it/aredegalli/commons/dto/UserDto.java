package it.aredegalli.commons.dto;

import it.aredegalli.commons.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements User {

    private Long id;
    private String username;
    private String name;
    private String surname;
    private String email;
    private String password;
    private Boolean active;

    private String token;


}
