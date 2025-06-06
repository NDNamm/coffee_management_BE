package com.example.demo.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Long id, String username, String password,
                             Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public Long getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    // Bắt buộc override các method này:
    @Override
    public boolean isAccountNonExpired() {
        return true;  // Có thể thay đổi logic tùy ý
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // Có thể thay đổi logic tùy ý
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // Có thể thay đổi logic tùy ý
    }

    @Override
    public boolean isEnabled() {
        return true;  // Có thể thay đổi logic tùy ý
    }
}
