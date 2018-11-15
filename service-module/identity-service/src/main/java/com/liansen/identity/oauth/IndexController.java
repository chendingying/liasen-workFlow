package com.liansen.identity.oauth;

import com.liansen.identity.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by CDZ on 2018/11/10.
 */
@RestController
public class IndexController {
    @Autowired
    private TokenStore tokenStore;
    @GetMapping("/sayHello")
    private String sayHello(@RequestHeader("Authorization") String auth){
        CustomUserDetails userDetails = (CustomUserDetails) tokenStore.readAuthentication(auth.split(" ")[1]).getPrincipal();

        User user = userDetails.getUser();

        return user.getUserName() + ":" + user.getPassword();
    }
}
