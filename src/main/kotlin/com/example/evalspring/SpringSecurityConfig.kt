//package com.example.evalspring
//
//import org.springframework.boot.web.servlet.filter.OrderedFormContentFilter
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
//import org.springframework.security.core.userdetails.User
//import org.springframework.security.core.userdetails.UserDetailsService
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
//import org.springframework.security.provisioning.InMemoryUserDetailsManager
//import org.springframework.security.web.SecurityFilterChain
//
//@Configuration
//@EnableWebSecurity
//class SpringSecurityConfig {
//    @Bean
//    fun securityFilterChain(http: HttpSecurity, formContentFilter: OrderedFormContentFilter): SecurityFilterChain {
//        http.authorizeHttpRequests { authz ->
//            authz
//                .requestMatchers("/static/**").permitAll()
//                .requestMatchers("/js/**").permitAll()
//                .requestMatchers("/css/**").permitAll()
//                .requestMatchers("/user/**").permitAll()
//                .requestMatchers("/admin/**").permitAll()
//                .requestMatchers("/matches").hasAuthority("user")
//                .requestMatchers("/login").permitAll()
//                .requestMatchers("/newMatch").hasAuthority("user")
//                .requestMatchers("/matchesJson").permitAll()
//                .requestMatchers("/home").permitAll()
//                .and()
//                .formLogin()
////                .formLogin { form ->
////                    form
////                        .loginPage("/login")
////                        .defaultSuccessUrl("/matches,true")
////                }
//        }
//        return http.build()
//    }
//
//    @Bean
//    fun UserDetailsService(): UserDetailsService {
//        return InMemoryUserDetailsManager().apply {
//            createUser(
//                User.withUsername("toto")
//                    .password(passwordEncoder().encode("titi"))
//                    .roles("user")
//                    .build()
//            )
//        }
//    }
//
//    @Bean
//    fun passwordEncoder() = BCryptPasswordEncoder()
//}