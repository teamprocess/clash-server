package com.process.clash.adapter.web.auth.service;

import com.process.clash.application.user.exception.exception.notfound.UserNotFoundException;
import com.process.clash.application.user.port.out.UserRepositoryPort;
import com.process.clash.infrastructure.principle.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return userRepositoryPort.findByUsername(username)
                    .map(user -> new AuthUser(user.id(), user.role()))
                    .orElseThrow(UserNotFoundException::new);
        } catch (UserNotFoundException e) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다." + username, e);
        }
    }
}