package it.aredegalli.commons.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface User {

    Long getId();

    String getUsername();

    String getName();

    String getSurname();

    String getEmail();

    Boolean getActive();

    @JsonIgnore()
    String getPassword();

    void setId(Long id);

    void setUsername(String username);

    void setName(String name);

    void setSurname(String surname);

    void setEmail(String email);

    @JsonIgnore()
    void setPassword(String password);

    void setActive(Boolean active);

}
