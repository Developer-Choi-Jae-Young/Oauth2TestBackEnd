package org.example.oauth2test.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.oauth2test.dto.interf.OAuth2SdkRequest;

@Getter
@NoArgsConstructor
public class KakaoSdkRequest implements OAuth2SdkRequest {
    @NotBlank(message = "액세스 토큰은 필수입니다.")
    private String accessToken;
    private String refreshToken;

    @Override
    public String getAccessToken() {
        return accessToken;
    }
}
