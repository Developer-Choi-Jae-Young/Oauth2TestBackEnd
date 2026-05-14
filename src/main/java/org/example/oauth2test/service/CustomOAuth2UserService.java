package org.example.oauth2test.service;

import lombok.RequiredArgsConstructor;
import org.example.oauth2test.dto.PrincipalDetails;
import org.example.oauth2test.dto.interf.OAuth2SdkRequest;
import org.example.oauth2test.dto.interf.OAuth2UserInfo;
import org.example.oauth2test.entity.MemberEntity;
import org.example.oauth2test.entity.num.ROLE;
import org.example.oauth2test.repository.MemberRepository;
import org.example.oauth2test.util.OAuth2SdkProvider;
import org.example.oauth2test.util.OAuth2UserInfoProvider;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Map<String, Object> oAuth2UserAttributes = super.loadUser(userRequest).getAttributes();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        Class<? extends OAuth2UserInfo> infoClass = OAuth2UserInfoProvider
                .findByRegistrationId(registrationId)
                .getInfoClass();

        OAuth2UserInfo oAuth2UserInfo = objectMapper.convertValue(oAuth2UserAttributes, infoClass);
        MemberEntity member = getOrSave(oAuth2UserInfo);

        return new PrincipalDetails(member, oAuth2UserAttributes, userNameAttributeName);
    }

    private MemberEntity getOrSave(OAuth2UserInfo oAuth2UserInfo) {
        return memberRepository.findByEmail(oAuth2UserInfo.getEmail())
                .map(member -> {
                    return memberRepository.save(member);
                })
                .orElseGet(() -> {
                    MemberEntity newMember = MemberEntity.builder()
                            .email(oAuth2UserInfo.getEmail())
                            .memberId(oAuth2UserInfo.getEmail().split("@")[0])
                            .role(ROLE.USER)
                            .build();
                    return memberRepository.save(newMember);
                });
    }
}
