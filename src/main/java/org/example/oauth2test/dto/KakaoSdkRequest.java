package org.example.oauth2test.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.oauth2test.dto.interf.OAuth2SdkRequest;

@Getter
@NoArgsConstructor
public class KakaoSdkRequest implements OAuth2SdkRequest {
    private String access_token;
    private String refresh_token;

    @Override
    public String getAccessToken() {
        return access_token;
    }
}
