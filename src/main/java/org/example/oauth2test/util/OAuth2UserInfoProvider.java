package org.example.oauth2test.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.oauth2test.dto.KakaoUserInfo;
import org.example.oauth2test.dto.interf.OAuth2UserInfo;

@AllArgsConstructor
@Getter
public enum OAuth2UserInfoProvider {
    KAKAO("kakao", KakaoUserInfo.class);

    private final String registrationId;
    private final Class<? extends OAuth2UserInfo> infoClass;

    public static OAuth2UserInfoProvider findByRegistrationId(String id) {
        return java.util.Arrays.stream(values())
                .filter(provider -> provider.registrationId.equalsIgnoreCase(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 소셜 서비스입니다: " + id));
    }
}
