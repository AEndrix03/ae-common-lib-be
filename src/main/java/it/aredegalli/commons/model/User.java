package it.aredegalli.commons.model;

import java.time.LocalDateTime;

public interface User {

    Long getId();
    String getUsername();
    String getName();
    String getSurname();
    String getEmail();
    String getPassword();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    LocalDateTime getLastLogin();
    Boolean getActive();
    void setId(Long id);
    void setUsername(String username);
    void setName(String name);
    void setSurname(String surname);
    void setEmail(String email);
    void setPassword(String password);
    void setCreatedAt(LocalDateTime createdAt);
    void setUpdatedAt(LocalDateTime updatedAt);
    void setLastLogin(LocalDateTime lastLogin);
    void setActive(Boolean active);

}
