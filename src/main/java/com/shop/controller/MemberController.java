package com.shop.controller;

import com.shop.dto.MemberFormDto;
import com.shop.dto.ReservationDto;
import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import com.shop.service.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @GetMapping(value = "/new")
    public String memberForm(Model model,MemberFormDto memberFormDto){
        model.addAttribute("memberFormDto",new MemberFormDto());
        return "member/memberForm";
    }
    @RequestMapping(value = "/success")
    public String memberSuccess(){return "member/memberSuccess";}

    @PostMapping("/emailCheck")
    @ResponseBody
    public String emailCheck(@RequestParam("email") String email) {

        Member cnt = memberService.emailCheck(email);
        if (cnt == null){
            System.out.println(cnt);
            return null;
        }
        else{
            System.out.println(cnt.getEmail());
            return cnt.getEmail();
        }




    }

    @PostMapping(value = "/new")
    public String memberForm(@Valid MemberFormDto memberFormDto,
                             BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "member/memberForm";
        }
        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        }catch(IllegalStateException e){
            model.addAttribute("errorMessage",e.getMessage());
            return "member/memberForm";
        }

        System.out.println(memberFormDto.getEmail() + " 멤버폼 이메일 컨트롤러");
        System.out.println(memberFormDto.getName() + " 멤버폼 이름 컨트롤러");
        model.addAttribute("name",memberFormDto.getName());
        model.addAttribute("email",memberFormDto.getEmail());
        return "member/memberSuccess";
    }

    @GetMapping(value = "/login")
    public String loginMember(ReservationDto reservationDto){
        System.out.println(reservationDto.getPrice() + " 로그인");
        return "/member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg","아이디 또는 비밀번호를 확인해주세요");
        return "/member/memberLoginForm";
    }


}
