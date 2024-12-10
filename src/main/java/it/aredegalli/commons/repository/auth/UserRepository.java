package it.aredegalli.commons.repository.auth;

import it.aredegalli.commons.model.SecUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<SecUser, Long> {

    SecUser getSecUserByEmail(String mail);

    SecUser getSecUserByUsername(String username);

}
