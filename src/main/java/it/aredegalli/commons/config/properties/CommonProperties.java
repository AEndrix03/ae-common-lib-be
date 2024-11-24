package it.aredegalli.commons.config.properties;

import it.aredegalli.commons.enums.auth.LoginTypeEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "common")
public class CommonProperties {

    private AuthProperties auth = new AuthProperties();

    @Getter
    @Setter
    public static class AuthProperties {
        private LoginTypeEnum loginType;
        private DataSourceProperties datasource = new DataSourceProperties();

        @Getter
        @Setter
        @Validated
        public static class DataSourceProperties {
            private String url;
            private String username;
            private String password;
            private String driverClassName;
            private int maxPoolSize;
            private int minIdle;
        }
    }

}
