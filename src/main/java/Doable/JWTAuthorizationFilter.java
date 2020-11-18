package Doable;

import Doable.service.JwtTokenService;
import io.jsonwebtoken.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final String HEADER = "Authorization";
    private final String PREFIX = "Bearer ";

    private final JwtTokenService jwtTokenService;

    public JWTAuthorizationFilter(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (!checkJWTToken(request.getHeader(HEADER))) {
            SecurityContextHolder.clearContext();
        } else {
            String token = getToken(request);
            if (jwtTokenService.validateToken(token)) {
                Claims claims = jwtTokenService.getAllClaimsFromToken(getToken(request));
                try {
                    if (jwtTokenService.validateToken(token)) {
                        setUpSpringAuthentication(claims);
                    }
                } catch (IllegalArgumentException | MalformedJwtException | ExpiredJwtException e) {
                    logger.error("Unable to get JWT Token or JWT Token has expired");
                    //UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("anonymous", "anonymous", null);
                    SecurityContextHolder.clearContext();
                }
            } else {
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(request, response);
    }

    /**
     * Authentication method in Spring flow
     *
     * @param claims
     */
    private void setUpSpringAuthentication(Claims claims) {
        List<String> authorities = (List<String>) claims.get("authorities");
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private boolean checkJWTToken(String header) {
        return header != null && header.startsWith(PREFIX);
    }

    private String getToken(HttpServletRequest request) {
        return request.getHeader(HEADER).replace(PREFIX, "");
    }

}
