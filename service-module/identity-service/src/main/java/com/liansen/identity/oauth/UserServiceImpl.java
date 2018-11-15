package com.liansen.identity.oauth;

import com.liansen.identity.domain.User;
import com.liansen.identity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by CDZ on 2018/11/10.
 */
@Service
public class UserServiceImpl implements UserService  {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(userName);
//        user.setPassword(user.getPassword().replace("$2y","$2a"));
      //  BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return new CustomUserDetails(user);
    }

}
