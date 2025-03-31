package com.doc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.csrf(Customizer.withDefaults())
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**")
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/climbing-routes/**")
                    .hasAnyAuthority("ROLE_CLIMBER", "ROLE_ROUTE-SETTER")
                    .requestMatchers(HttpMethod.POST, "/climbing-routes/**")
                    .hasAuthority("ROLE_ROUTE-SETTER")
                    .requestMatchers(HttpMethod.PUT, "/climbing-routes/**")
                    .hasAuthority("ROLE_ROUTE-SETTER")
                    .requestMatchers(HttpMethod.DELETE, "/climbing-routes/**")
                    .hasAuthority("ROLE_ROUTE-SETTER")
                    .anyRequest()
                    .authenticated())
        .oauth2ResourceServer(
            oauth2 ->
                oauth2.jwt(
                    jwt ->
                        jwt.jwtAuthenticationConverter(new CognitoJwtAuthenticationConverter())));
    return http.build();
  }
}
