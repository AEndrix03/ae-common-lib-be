package it.aredegalli.commons.security.jwt;

import it.aredegalli.commons.model.SecUser;
import it.aredegalli.commons.repository.auth.UserRepository;
import it.aredegalli.commons.service.auth.UserProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtRequestInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProvider userProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = extractTokenFromRequest(request);
        if (token != null && !this.jwtService.isTokenExpired(token)) {
            String username = this.jwtService.extractUsername(token);
            SecUser userDetails = this.userRepository.getSecUserByEmail(username);

            if (jwtService.isTokenValid(token, userDetails)) {
                this.userProvider.setUser(this.jwtService.extractUserObj(token));
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        this.userProvider.clear();
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
