package it.aredegalli.commons.dto.auth;

import lombok.Data;

@Data
public class RegisterDto extends LoginDto {

    private String name;
    private String surname;
}
