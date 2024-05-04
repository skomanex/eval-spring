package com.example.evalspring

import org.springframework.boot.web.servlet.filter.OrderedFormContentFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SpringSecurityConfig {
    @Bean
    fun securityFilterChain(http: HttpSecurity, formContentFilter: OrderedFormContentFilter): SecurityFilterChain {
        http.authorizeHttpRequests { authz ->
            authz
                .requestMatchers("/static/**").permitAll()
                .requestMatchers("/js/**").permitAll()
                .requestMatchers("/css/**").permitAll()
                .requestMatchers("/user/**").permitAll()
                .requestMatchers("/admin/**").permitAll()
                .requestMatchers("/matches").permitAll()
                .requestMatchers("/login").permitAll()
                .requestMatchers("/newMatch").authenticated()
                .requestMatchers("/matchesJson").permitAll()
                .requestMatchers("/home").permitAll()
                .requestMatchers("/accueil").permitAll()
                .requestMatchers("/editMatch/**").authenticated()
                .requestMatchers("/termineMatch/**").authenticated()
                .and()
                .formLogin()
                { form ->
                    form
                        .defaultSuccessUrl("/newMatch")
                }
        }
        return http.build()
    }
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun UserDetailsService(): UserDetailsService {
        return InMemoryUserDetailsManager().apply {
            createUser(
                User.withUsername("root")
                    .password(passwordEncoder().encode("root"))
                    .roles("user")
                    .build()
            )
        }
    }
}