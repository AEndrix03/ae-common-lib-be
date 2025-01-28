package it.aredegalli.commons.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import it.aredegalli.commons.config.properties.CommonProperties;
import it.aredegalli.commons.config.properties.SecurityProperties;
import it.aredegalli.commons.model.SecUser;
import it.aredegalli.commons.service.auth.UserDetailsDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SecurityProperties securityProperties;
    private final CommonProperties commonProperties;

    @Autowired
    public JwtService(SecurityProperties securityProperties, CommonProperties commonProperties) {
        this.securityProperties = securityProperties;
        this.commonProperties = commonProperties;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(SecUser user) throws JsonProcessingException {
        HashMap<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userObj", objectMapper.writeValueAsString(user));

        return buildToken(extraClaims, user, this.securityProperties.getJwt().getExpirationTime());
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetailsDecorator userDetails) {
        return buildToken(extraClaims, userDetails, this.securityProperties.getJwt().getExpirationTime());
    }

    public SecUser extractUserObj(String token) throws JsonProcessingException {
        String userJson = extractClaim(token, claims -> claims.get("userObj", String.class));
        return objectMapper.readValue(userJson, SecUser.class);
    }

    public long getExpirationTime() {
        return this.securityProperties.getJwt().getExpirationTime();
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetailsDecorator userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getAuthUsername(this.commonProperties.getAuth().getLoginType()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetailsDecorator userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getAuthUsername(this.commonProperties.getAuth().getLoginType()))) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.securityProperties.getJwt().getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
