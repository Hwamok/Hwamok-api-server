package com.hwamok.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.hwamok.utils.PreConditions.notNull;

@Service
@Transactional
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private static final Long EXPIRE_MINUTES = 30L*1000; // 5분
    private static final Long EXPIRE_DATE = 1L*24*60*60*1000; // 24시간

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Override
    public String issue(Long id, String role, JwtType type) {
        Map<String,String> claims = new HashMap<>();
        claims.put("id", String.valueOf(id));
        claims.put("role", role);

        Date now = new Date();
        Date expiration = null;

        switch (type){
            case ACCESS -> expiration = new Date(now.getTime() + EXPIRE_MINUTES);
            case REFRESH -> expiration = new Date(now.getTime() + EXPIRE_DATE);
        }

        return Jwts.builder()
                .issuedAt(now)
                .expiration(expiration)
                .claims(claims)
                .signWith(generateSecretKey())
                .compact();
    }

    @Override
    public String resolveToken(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        if(accessToken != null && accessToken.startsWith("Bearer")){
            String token = accessToken.substring(7);
            return token;
        } else {
            return null;

        }
    }

    @Override
    public boolean validate(String token) {
        try {
            notNull(Jwts.parser().verifyWith(generateSecretKey()).build().parse(token).getPayload());
            return false;
        } catch (ExpiredJwtException | NullPointerException e){
            return true;
        }
    }

    @Override
    public Long getId(String token) {
        return Jwts.parser().verifyWith(generateSecretKey()).build().parseSignedClaims(token).getPayload().get("id", Long.class);
    }

    @Override
    public String getRole(String token) {
        return Jwts.parser().verifyWith(generateSecretKey()).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    private SecretKey generateSecretKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
