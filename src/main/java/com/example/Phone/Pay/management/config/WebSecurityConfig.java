package com.example.Phone.Pay.management.config;


import com.example.Phone.Pay.management.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;




@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private UserRepo userRepo;


    private final String[] PUBLIC_RESOURCE_AND_URL = {"/",
            "/phone-pay/sign-up",
            "/phone-pay/get-otp/{mobile}",
            "/phone-pay/sign-in",
            "/phone-pay/get-all-git-logs"
    };

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {


        http.csrf()
                .disable()
                .authorizeHttpRequests()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler()).and().addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenUtils, userRepo), BasicAuthenticationFilter.class).
                addFilterBefore(new CustomCORSFilter(), ChannelProcessingFilter.class);

        return http.build();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.debug(true)
                .ignoring()
                .requestMatchers(PUBLIC_RESOURCE_AND_URL);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}
