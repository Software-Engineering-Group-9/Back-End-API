package Doable;

import Doable.service.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @EnableWebSecurity
    @Configuration
    static
    class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private JwtTokenService jwtTokenService;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and()
					.csrf().disable()
                    .authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/api/v1/user/register").permitAll()
                    .antMatchers(HttpMethod.POST, "/api/v1/user/login").permitAll()
                    .anyRequest()
                    .authenticated().and()
                    .addFilter(new JWTAuthorizationFilter(jwtTokenService, authenticationManager()))
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }
    }

}
