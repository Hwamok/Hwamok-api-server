package com.hwamok.security.authenticationProvider;

import com.hwamok.admin.domain.Admin;
import com.hwamok.admin.domain.AdminRepository;
import com.hwamok.core.exception.ExceptionCode;
import com.hwamok.core.exception.HwamokException;
import com.hwamok.security.userDetails.HwamokAdmin;
import com.hwamok.security.userDetails.HwamokUser;
import com.hwamok.user.domain.User;
import com.hwamok.user.domain.UserRepository;
import com.hwamok.utils.Role;
import com.mysema.commons.lang.Pair;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.ListResourceBundle;

@Component
public class DefaultAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Pair<Long, List<Role>> pair = (Pair<Long, List<Role>>) authentication.getPrincipal();

        if(pair.getSecond().contains(Role.USER)) {
            HwamokUser hwamokUser = new HwamokUser(pair.getFirst(), pair.getSecond());

            return new UsernamePasswordAuthenticationToken(hwamokUser, "", hwamokUser.getAuthorities());
        }else {
            HwamokAdmin hwamokAdmin = new HwamokAdmin(pair.getFirst(), pair.getSecond());

            return new UsernamePasswordAuthenticationToken(hwamokAdmin, "", hwamokAdmin.getAuthorities());
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == UsernamePasswordAuthenticationToken.class;
    }
}
