package com.hwamok.config;

import com.hwamok.security.authenticationProvider.DefaultAuthenticationProvider;
import com.hwamok.security.jwt.JwtService;
import com.hwamok.security.jwt.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtService jwtService;

    private final static String[] POST_PERMIT_MATCHERS = Arrays.asList(
            "/auth/adminLogin", "/auth/userLogin", "/admin", "/user"
    ).toArray(String[]::new);

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.httpBasic(h -> h.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(reqeust ->
                        reqeust.requestMatchers(HttpMethod.POST,POST_PERMIT_MATCHERS).permitAll())
                .authorizeHttpRequests(request ->
                        request.requestMatchers(HttpMethod.GET, "/notice").permitAll())
                .authorizeHttpRequests(request ->
                        request.requestMatchers(HttpMethod.POST, "/notice").hasAnyAuthority("어드민", "슈퍼 어드민"))
                .authorizeHttpRequests(request ->
                        request.requestMatchers(HttpMethod.PATCH, "/notice").hasAnyAuthority("어드민", "슈퍼 어드민"))
                .authorizeHttpRequests(request ->
                        request.requestMatchers(HttpMethod.DELETE, "/notice").hasAnyAuthority("어드민", "슈퍼 어드민"))
                .authorizeHttpRequests(request ->
                        request.requestMatchers(HttpMethod.GET,"/admin").hasAnyAuthority("어드민", "슈퍼 어드민"))
                .authorizeHttpRequests(request ->
                        request.requestMatchers(HttpMethod.PATCH,"/admin").hasAnyAuthority("어드민", "슈퍼 어드민"))
                .authorizeHttpRequests(request ->
                        request.requestMatchers(HttpMethod.DELETE,"/admin").hasAnyAuthority("어드민", "슈퍼 어드민"))
                .authorizeHttpRequests(request ->
                        request.requestMatchers(HttpMethod.GET, "/user").authenticated())
                .authorizeHttpRequests(request ->
                        request.requestMatchers(HttpMethod.PATCH, "/user").authenticated())
                .authorizeHttpRequests(request ->
                        request.requestMatchers(HttpMethod.DELETE, "/user").authenticated())
                .authorizeHttpRequests(request ->
                                request.anyRequest().permitAll())
                .csrf(c -> c.disable())
                .authenticationProvider(new DefaultAuthenticationProvider())
                .addFilterBefore(new JwtTokenFilter(jwtService), UsernamePasswordAuthenticationFilter.class)
                ;
        return http.build();

    }
}
