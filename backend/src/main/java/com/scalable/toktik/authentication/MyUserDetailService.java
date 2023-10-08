package com.scalable.toktik.authentication;

import com.scalable.toktik.model.UserModel;
import com.scalable.toktik.repsitory.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    public MyUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password");
        }
        User.UserBuilder builder = User.withUsername(user.getUsername()).password(user.getPassword());
        if (user.is_staff()) {
            return builder.roles("ADMIN").build();
        } else {
            return builder.roles("USER").build();
        }
    }
}

