//package com.liansen.identity.oauth;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//
///**
// * Created by CDZ on 2018/11/10.
// */
//@Configuration
//@EnableResourceServer
//public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
////        http.authorizeRequests().antMatchers("/auths/**").authenticated()
////                .anyRequest().authenticated();
//        // 全部通过
//        http.csrf().disable().authorizeRequests()
//                .anyRequest()
//                .permitAll();
//    }
//}
