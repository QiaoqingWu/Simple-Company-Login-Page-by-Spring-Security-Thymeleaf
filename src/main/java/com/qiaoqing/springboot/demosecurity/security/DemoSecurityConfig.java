package com.qiaoqing.springboot.demosecurity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class DemoSecurityConfig {

    // add support for JDBC ... no more hardcoded users
    @Bean
    public UserDetailsManager userDetailsManager(DataSource datasource) {

        // tell Spring Security to use JDBC authentication with our datasource(database)
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(datasource);

        // define query to retrieve a user by username
        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "select user_id, pw, active from members where user_id=?"
        );

        // define query to retrieve the authorities/roles by username
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "select user_id, role from roles where user_id=?"
        );

        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // http.authorizeHttpRequests - restrict access based on the HTTP request
        http.authorizeHttpRequests(configurer ->
                // any request to the app must be authenticated (logged in)
                configurer
                        .requestMatchers("/").hasRole("EMPLOYEE")
                        .requestMatchers("/leaders/**").hasRole("MANAGER") // ** means all subdirectories
                        .requestMatchers("/systems/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
            )
                // we are customizing the form login process
                .formLogin(form ->
                        form
                                .loginPage("/showMyLoginPage")
                                .loginProcessingUrl("/authenticateTheUser") // get this for free
                                .permitAll()
                )
                .logout(logout -> logout.permitAll())
                .exceptionHandling(configurer ->
                        configurer.accessDeniedPage("/access-denied"))
        ;

        return http.build();
    }

    /*
        @Bean
        public InMemoryUserDetailsManager userDetailsManager() {

            UserDetails john = User.builder()
                    .username("john")
                    .password("{noop}test123")
                    .roles("EMPLOYEE")
                    .build();

            UserDetails mary = User.builder()
                    .username("mary")
                    .password("{noop}test123")
                    .roles("EMPLOYEE", "MANAGER")
                    .build();

            UserDetails susan = User.builder()
                    .username("susan")
                    .password("{noop}test123")
                    .roles("EMPLOYEE", "MANAGER", "ADMIN")
                    .build();

            // return instance of InMemoryUerDetailsManager
            return new InMemoryUserDetailsManager(john, mary, susan);

        }
     */
}
