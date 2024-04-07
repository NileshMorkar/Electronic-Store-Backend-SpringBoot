package com.example.ElectronicStore.service.Impl;

import com.example.ElectronicStore.entity.UserEntity;
import com.example.ElectronicStore.exception.GlobalException;
import com.example.ElectronicStore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username).orElseThrow(() -> new GlobalException("User Not Found!!", HttpStatus.NOT_FOUND));
        return userEntity;
    }
}
