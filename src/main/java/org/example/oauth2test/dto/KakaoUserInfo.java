package org.example.oauth2test.dto;

import org.example.oauth2test.dto.interf.OAuth2UserInfo;

public class KakaoUserInfo implements OAuth2UserInfo {
    private Long id;

    @Override
    public Long getId() {
        return id;
    }
}
