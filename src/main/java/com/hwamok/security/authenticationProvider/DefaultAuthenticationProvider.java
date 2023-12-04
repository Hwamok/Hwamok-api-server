package com.hwamok.security.authenticationProvider;

import com.hwamok.security.userDetails.HwamokAdmin;
import com.hwamok.security.userDetails.HwamokUser;
import com.mysema.commons.lang.Pair;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class DefaultAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Pair<Long, String> idAndRole = (Pair<Long, String>) authentication.getPrincipal();
        if(idAndRole.getSecond().equals("일반회원")){
            return new UsernamePasswordAuthenticationToken(new HwamokUser(idAndRole.getFirst(), idAndRole.getSecond()),null,new ArrayList<>());

        } else {
            return new UsernamePasswordAuthenticationToken(new HwamokAdmin(idAndRole.getFirst(), idAndRole.getSecond()),null,new ArrayList<>());

        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == UsernamePasswordAuthenticationToken.class;
    }
}
