package com.shop.controller;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import com.shop.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberControllerTest { // 자 이제 시작이야
    @Autowired
    private MemberService memberService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    PasswordEncoder passwordEncoder;

    /*
    1.MemberFormDto를 객체를 생성해서
      더미 데이터를 넣어줍니다.
    2. Member -> createMember를 호출하는데 매개변수 (MemberFormDto,PasswordEncoder)
       Member 객체를 생성합니다.
    3. memberService.saveMember -> Member를 테이블에 저장한다.
       3-1 validateDuplicateMember -> 이메일을 이용해서 기존 가입 여부 확인
       3-1-1(가입 X) -> memberRepository.save(member) -> MemberRepository를 이용해서 테이블에 저장
          3-1-1-1 테이블에 데이터가 저장이 되면 똑같은 Member를 객체를 반환
       3-1-2(가입 O) ->new IllegalStateException("이미 가입된 회원입니다."); 종료
     */
    public Member createMember(String email, String password){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail(email);
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword(password);
        memberFormDto.setTel("010-010");
        Member member = Member.createMember(memberFormDto, passwordEncoder);
        return memberService.saveMember(member);
    }
    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginSuccessTest() throws Exception{
        String email = "test@email.com"; // 문자열 변수
        String password = "1234"; // 문자열 변수
        this.createMember(email,password); // 위에 있는 createMember 호출 데이터베이스에 데이터가 저장
        // 가상 웹 실행
        //로그인 실행 userParameter ->email
        //로그인 실행 /members/login
        // user -> test@email.com password - > 1234
        mockMvc.perform(formLogin().userParameter("email")
                        .loginProcessingUrl("/members/login")
                        .user(email).password(password))
                //그리고 기대한다. authenticated() 로그인 된 걸로 기대한다.
                // authenticated() 로그인 성공
                .andExpect(SecurityMockMvcResultMatchers.authenticated());
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    public void loginFailTest() throws Exception{
        String email = "test@email.com";
        String password = "1234";
        this.createMember(email,password);
        mockMvc.perform(formLogin().userParameter("email")
                        .loginProcessingUrl("/members/login")
                        .user(email).password("12345"))
                // 로그인 인증 X를 기대한다.(로그인 실패 하기를 원한다.)
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
    }
}