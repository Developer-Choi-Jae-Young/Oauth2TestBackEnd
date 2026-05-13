package org.example.oauth2test.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.oauth2test.dto.interf.OAuth2SdkRequest;
import org.example.oauth2test.util.OAuth2Provider;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomOAuth2Filter extends OncePerRequestFilter {
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final DefaultOAuth2UserService oAuth2UserService;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final String LOGIN_PATTERN = "/sdk/oauth2/authorization/{registrationId}";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if (pathMatcher.match(LOGIN_PATTERN, servletPath) && "POST".equalsIgnoreCase(request.getMethod())) {
            try {
                Map<String, String> variables = pathMatcher.extractUriTemplateVariables(LOGIN_PATTERN, servletPath);
                String registrationId = variables.get("registrationId");

                Class<? extends OAuth2SdkRequest> dtoClass = OAuth2Provider
                        .findByRegistrationId(registrationId)
                        .getDtoClass();

                OAuth2SdkRequest sdkRequest = objectMapper.readValue(request.getInputStream(), dtoClass);
                String accessToken = sdkRequest.getAccessToken();

                if (accessToken == null || accessToken.isEmpty()) {
                    throw new IllegalArgumentException("Access Token이 누락되었습니다.");
                }

                ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(registrationId);
                if (clientRegistration == null) {
                    throw new IllegalArgumentException("지원하지 않는 소셜 로그인입니다: " + registrationId);
                }

                OAuth2AccessToken oauth2Token = new OAuth2AccessToken(
                        OAuth2AccessToken.TokenType.BEARER,
                        accessToken,
                        Instant.now(),
                        Instant.now().plusSeconds(3600)
                );

                OAuth2UserRequest userRequest = new OAuth2UserRequest(clientRegistration, oauth2Token);
                OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

                OAuth2LoginAuthenticationToken authentication = new OAuth2LoginAuthenticationToken(
                        clientRegistration,
                        new OAuth2AuthorizationExchange(null, null),
                        oAuth2User,
                        oAuth2User.getAuthorities(),
                        oauth2Token
                );

                var context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증 실패: " + e.getMessage());
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
