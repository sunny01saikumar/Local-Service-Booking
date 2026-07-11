package com.sai.geoLocation.security;

import com.sai.geoLocation.repository.UserRepository;
import com.sai.geoLocation.repository.RoleRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
public class LocalHubUserDetailsService implements UserDetailsService {
    private final UserRepository users;
    private final RoleRepository roles;

    public LocalHubUserDetailsService(UserRepository users, RoleRepository roles) {
        this.users = users;
        this.roles = roles;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = users.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var roleCodes = roles.findRoleCodesByUserId(user.getId());
        var authorities = roleCodes.stream()
                .map(code -> new SimpleGrantedAuthority("ROLE_" + code))
                .collect(Collectors.toList());
        if (authorities.isEmpty()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
        }
        return new User(user.getEmail(), user.getPasswordHash(), authorities);
    }
}
