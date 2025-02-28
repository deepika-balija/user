package com.apexon.catchIt.user.config;

import com.apexon.catchIt.user.model.User;
import com.apexon.catchIt.user.repositroy.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch user from the database
       User user= (User) userRepo.findByUserName(username)
                .orElseThrow(()->new UsernameNotFoundException("User Not Found "+username));
        // Convert roles from String to SimpleGrantedAuthority
       List<GrantedAuthority> authorities= user.getRoles().stream()
               .map(role->new SimpleGrantedAuthority("ROLE_"+role))
               .collect(Collectors.toList());
        // Return Spring Security User object
        return new org.springframework.security.core.userdetails.User(user.getUserName(),
                user.getPassword(), authorities);
    }
}
