package com.shop.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shop.entities.ERole;
import com.shop.entities.Role;
import com.shop.entities.User;
import com.shop.repositories.RoleRepository;
import com.shop.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository = null;
    private final RoleRepository roleRepository = null;
    private final PasswordEncoder passwordEncoder = null;
    private final AuthenticationManager authenticationManager = null;

    public String register(User user) {
        // Kiểm tra username đã tồn tại chưa
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username đã tồn tại!");
        }
        
        // Kiểm tra email đã tồn tại chưa
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email đã tồn tại!");
        }

        // Mã hóa mật khẩu trước khi lưu
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Set role mặc định là ROLE_USER
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role USER không tìm thấy."));
        roles.add(userRole);
        user.setRoles(roles);
        
        // Lưu user vào database
        userRepository.save(user);
        
        return "Đăng ký thành công!";
    }

    public String login(User loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), 
                    loginRequest.getPassword()
                )
            );
            
            if (authentication.isAuthenticated()) {
                User user = userRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("User không tồn tại"));
                return "Đăng nhập thành công!";
            } else {
                return "Đăng nhập thất bại!";
            }
        } catch (Exception e) {
            return "Sai tên đăng nhập hoặc mật khẩu!";
        }
    }
} 