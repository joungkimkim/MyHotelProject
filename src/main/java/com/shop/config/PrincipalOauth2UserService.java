/*package com.shop.config;
import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import groovy.util.logging.Slf4j;
import jakarta.servlet.http.HttpSession;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Builder
public class PrincipalOauth2UserService  extends DefaultOAuth2UserService {

    private final HttpSession httpSession;

    private final MemberRepository memberRepository;

    public String saveEmail(Oauth2UserInfo oauth2UserInfo){

        String email = oauth2UserInfo.getEmail();
        System.out.println(email + " 이메일 입니다");
        return email;
    }


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(("getAttributes : {}" + oAuth2User.getAttributes()));

        Oauth2UserInfo oAuth2UserInfo = null;

        String provider = userRequest.getClientRegistration().getRegistrationId();

        if (provider.equals("google")) {
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());

        } else if (provider.equals("kakao")) {
            System.out.println(("카카오 로그인 요청"));
            oAuth2UserInfo = new KakaoMemberInfo((Map) oAuth2User.getAttributes());
        } else {
            System.out.println(("네이버 로그인 요청"));
            oAuth2UserInfo = new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));
        }


        Member member;      // new member 삭제
        //String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        String provider2 = provider;
        String nickname = oAuth2UserInfo.getName();
        httpSession.setAttribute("name",nickname);
        //System.out.println(id + "id");
        System.out.println(email + "email");

         member = memberRepository.findByEmail(email);
        ///httpSession.setAttribute("email",email);
        //httpSession.setAttribute("id",id);
        httpSession.setAttribute("user",member);
        if (member == null) {
            member = Member.userMember(oAuth2UserInfo);
           memberRepository.save(member);
            System.out.println(member.getEmail() + " email Test");
            httpSession.setAttribute("user",member);
            httpSession.setAttribute("email",email);
            httpSession.setAttribute("name",nickname);
           // httpSession.setAttribute("id",id);
        }
        httpSession.setAttribute("email",email);
        httpSession.setAttribute("name",nickname);
       // httpSession.setAttribute("id",id);
        System.out.println(member.getEmail() + " email Test");
        httpSession.setAttribute("user",member);
      memberRepository.save(member);
        //httpSession.setAttribute("user",member);
        // return new Principal(member, oAuth2User.getAttributes());
        ///Member user=(Member) httpSession.getAttribute("user");
        //System.out.println(user+ " user 정보");
        return oAuth2User;

    }

}*/
