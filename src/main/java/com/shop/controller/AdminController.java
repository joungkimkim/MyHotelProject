/*package com.shop.controller;

import com.shop.entity.Admin;
import com.shop.repository.AdminRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/adminpage")
@Controller
@RequiredArgsConstructor
public class AdminController {
    private final AdminRepository adminRepository;
    private final AdminService adminService;
    @GetMapping(value = "/login")
    public String loginadmin(){


        return "/admin/adminLogin";
    }
    /*@PostMapping(value = "/login/success")
    public String loginsuccess(Admin admin,Model model) {

        String email = admin.getEmail();
        System.out.println(email + "관리자 입력 이메일");

        Admin email2 = adminRepository.findByEmail(email);
        if (email2 == null) {
            model.addAttribute("loginErrorMsg","이메일 또는 비밀번호를 확인해주세요");
            System.out.println("이메일 또는 비밀번호를 확인해주세요");
            return "/admin/adminLogin";
        } else {

            System.out.println(email2 + "관리자 DB 이메일");
            //if (email != ){
            // System.out.println("이메일 또는 비밀번호를 확인해주세요");
            //return "/adminpage/login/error";
            // }

            return "redirect:/";
        }
    }



}*/
