package com.hwamok.security.jwt;

import com.hwamok.core.exception.ExceptionCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtService.resolveToken(request);
        if(token != null){
            if(jwtService.validate(token)){
                response.sendError(HttpStatus.UNAUTHORIZED.value(), ExceptionCode.ACCESS_DENIED.getMessage());
                return;
            }

            // id, role을 pair에 저장
            Pair<Long, String> idAndRole = Pair.of(jwtService.getId(token), jwtService.getRole(token));

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(idAndRole,"", Collections.emptyList());

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }

        filterChain.doFilter(request,response);
    }
}
