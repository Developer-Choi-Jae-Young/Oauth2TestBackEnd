package org.example.oauth2test.util;

import lombok.Getter;
import org.example.oauth2test.dto.KakaoSdkRequest;
import org.example.oauth2test.dto.interf.OAuth2SdkRequest;

@Getter
public enum OAuth2Provider {
    KAKAO("kakao", KakaoSdkRequest.class);

    private final String registrationId;
    private final Class<? extends OAuth2SdkRequest> dtoClass;

    OAuth2Provider(String registrationId, Class<? extends OAuth2SdkRequest> dtoClass) {
        this.registrationId = registrationId;
        this.dtoClass = dtoClass;
    }

    public static OAuth2Provider findByRegistrationId(String id) {
        return java.util.Arrays.stream(values())
                .filter(provider -> provider.registrationId.equalsIgnoreCase(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 소셜 서비스입니다: " + id));
    }
}
