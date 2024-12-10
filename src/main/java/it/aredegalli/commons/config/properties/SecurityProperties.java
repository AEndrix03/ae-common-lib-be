package it.aredegalli.commons.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    private JwtProperties jwt = new JwtProperties();
    private CryptoProperties crypto = new CryptoProperties();
    private CorsProperties cors = new CorsProperties();

    @Getter
    @Setter
    public static class JwtProperties {
        private String secretKey;
        private long expirationTime;
    }

    @Getter
    @Setter
    public static class CryptoProperties {
        private String aesSecretKey;
    }

    @Getter
    @Setter
    public static class CorsProperties {
        private String allowedOrigins;
        private String allowedMethods;
        private String allowedHeaders;
    }
}

