package it.aredegalli.commons.config;

import it.aredegalli.commons.model.User;
import it.aredegalli.commons.service.auth.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
public class UserConfig {

    private final UserProvider userProvider;

    @Autowired
    public UserConfig(UserProvider userProvider) {
        this.userProvider = userProvider;
    }

    @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public User getUser() {
        return this.userProvider.getUser();
    }

}
