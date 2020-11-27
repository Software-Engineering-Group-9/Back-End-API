package Doable.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

// todo: Clean up code
// todo: comments

@Component
public class JwtTokenService {

    private static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
    private static final String secret = "mySecretKey";

    /**
     *
     * @param token
     * @return
     */
    public String getSubjectFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     *
     * @param token
     * @return
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     *
     * @param token
     * @return
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     *
     * @param token
     * @param claimsResolver
     * @param <T>
     * @return
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     *
     * @param token
     * @return
     */
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     *
     * @param token
     * @return
     */
    public Boolean validateToken(String token) {
        try {
            final String subject = getSubjectFromToken(token);
            return subject != null && !isTokenExpired(token);
        } catch (ExpiredJwtException e){
            return false;
        }
    }

    /**
     *
     * @param userid
     * @return
     */
    public String generateToken(String userid){
        Claims claims = Jwts.claims().setSubject(userid);

        // Create Role user
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        // Create token based on given information
        return  Jwts.builder()
                .setClaims(claims)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, "mySecretKey")
                .compact();
    }

}
