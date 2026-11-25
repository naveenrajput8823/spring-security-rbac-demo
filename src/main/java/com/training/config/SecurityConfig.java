package com.training.config;

import com.training.service.impl.GenericUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${spring.security.public-urls}")
    private String[] PUBLIC_URLS;
    @Autowired
    private GenericUserServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
//                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(req-> req
                        .requestMatchers(PUBLIC_URLS).permitAll()
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/user/**").hasRole("USER")
                        .requestMatchers("/api/v1/vendor/**").hasRole("VENDOR")
                        .anyRequest()
                        .authenticated())
//                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults())
                .build();
    }

//    @Bean
//    public UserDetailsService userSeeder()
//    {
//        UserDetails user=User.builder().username("naveen").password("1234").roles("USER").build();
////        UserDetails user=User.builder().username("naveen").password(encoder().encode("1234")).roles("USER").build();
//        // 1234 => $2a$10$xEUSk750sCaV2za7ETDO7eq6D3hH5zSRxLhuKs3C5w5HYrl2fNGe.
//        UserDetails vendor=User.builder().username("pritesh").password(encoder().encode("1234")).roles("VENDOR").authorities("SEE_VENDORS").build();
//        UserDetails admin=User.builder().username("amantiwari8861").password(encoder().encode("1234")).roles("ADMIN","VENDOR","USER").build();
//        UserDetails superAdmin=User.builder().username("aman").password(encoder().encode("1234")).roles("ADMIN").authorities("SEE_VENDORS","SEE_ADMINS").build();
//        return new InMemoryUserDetailsManager(user,vendor,admin);
//    }

//    @Bean
//    public NoOpPasswordEncoder encoder()
//    {
//        return (NoOpPasswordEncoder)NoOpPasswordEncoder.getInstance();
//    }
    @Bean
    public PasswordEncoder encoder()
    {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

}
