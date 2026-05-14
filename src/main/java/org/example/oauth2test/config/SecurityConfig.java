package org.example.oauth2test.config;

import lombok.RequiredArgsConstructor;
import org.example.oauth2test.filter.CustomOAuth2Filter;
import org.example.oauth2test.filter.TokenAuthenticationFilter;
import org.example.oauth2test.filter.TokenExceptionFilter;
import org.example.oauth2test.handler.OAuth2SuccessHandler;
import org.example.oauth2test.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final TokenAuthenticationFilter tokenAuthenticationFilter;
    private final ClientRegistrationRepository clientRegistrationRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        return httpSecurity.formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .logout(AbstractHttpConfigurer::disable)
                .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(oauth -> oauth.userInfoEndpoint(c -> c.userService(customOAuth2UserService)).successHandler(oAuth2SuccessHandler))
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new TokenExceptionFilter(), tokenAuthenticationFilter.getClass())
                .addFilterBefore(new CustomOAuth2Filter(clientRegistrationRepository, customOAuth2UserService, oAuth2SuccessHandler), OAuth2AuthorizationRequestRedirectFilter.class)
                .authorizeHttpRequests(request -> request.requestMatchers("/h2-console/**","/auth/success","/error","/login/**","/oauth2/**", "/favicon.ico").permitAll().anyRequest().authenticated())
                .build();
    }
}
