package com.juanliz.protecciontest.service;

import com.juanliz.protecciontest.dto.UserGetDto;
import com.juanliz.protecciontest.dto.UserMapper;
import com.juanliz.protecciontest.dto.UserUpdateDto;
import com.juanliz.protecciontest.model.User;
import com.juanliz.protecciontest.repository.UserRepository;
import com.juanliz.protecciontest.auth.AuthRequest;
import com.juanliz.protecciontest.auth.AuthResponse;
import com.juanliz.protecciontest.utils.JwtUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtils jwtUtils,
            UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User getCurrentAuthenticatedUser() {
        return userRepository.findByUsername((String) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal()).orElse(null);
    }

    public AuthResponse login(AuthRequest request) {
        String userName = request.username();
        String password = request.password();
        Authentication authentication = authenticate(userName, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new AuthResponse(userName, jwtUtils.createToken(authentication));
    }

    public Authentication authenticate(String userName, String password) {
        UserDetails user = this.loadUserByUsername(userName);
        if (user == null) throw new BadCredentialsException("Invalid username or password");

        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new BadCredentialsException("Invalid username or password");

        // Get user ID for pass to credentials
        int userId = userRepository.findByUsername(userName).orElseThrow().getId();

        return new UsernamePasswordAuthenticationToken(
                userName,
                userId,
                Set.of(new SimpleGrantedAuthority(user.getAuthorities()
                        .stream().findFirst().orElseThrow().getAuthority()))
        );
    }

    public void updatePassword(AuthRequest request) {
        User user = userRepository.findByUsername(request.username()).orElse(null);
        if (user == null) throw new UsernameNotFoundException("User not found");
        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);
    }

    public UserGetDto getUserById(int id) {
        return userMapper.toGetDto(userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found")));
    }


    public UserGetDto createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User newUser = userRepository.save(user);
        return userMapper.toGetDto(newUser);
    }

    public UserGetDto updateUser(UserUpdateDto user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        userMapper.partialUpdate(user, existingUser);
        return userMapper.toGetDto(existingUser);
    }

    public void deleteUser(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        userRepository.delete(user);
    }

}
