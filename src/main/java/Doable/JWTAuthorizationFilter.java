package Doable;

import Doable.service.JwtTokenService;
import io.jsonwebtoken.*;
import org.apache.catalina.filters.CorsFilter;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final String HEADER = "authorization";
    private final String PREFIX = "Bearer ";

    private final JwtTokenService jwtTokenService;

    public JWTAuthorizationFilter(JwtTokenService jwtTokenService, AuthenticationManager authMangaer) {
        super(authMangaer);
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "10000");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, Content-Type, origin, Authorization, accept, client-security-token");

        String a = IteratorUtils.toList(request.getHeaderNames().asIterator()).toString();
        response.setStatus(HttpServletResponse.SC_OK);
        System.out.println(a + "\n" + request.getHeader(HEADER) + "\n" + request.getHeader("origin"));

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
