package com.training.service.impl;

import com.training.entity.GenericUser;
import com.training.repository.GenericUserRepository;
import com.training.security.CustomerUserDetail;
import com.training.service.GenericUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GenericUserServiceImpl implements GenericUserService, UserDetailsService {
    GenericUserRepository genericUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        GenericUser byEmail = genericUserRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("User not found with email "+username));
        return new CustomerUserDetail(byEmail);
    }
}
