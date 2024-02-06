package com.shop.controller;

import com.shop.dto.*;
import com.shop.entity.Item;
import com.shop.service.ItemService;
import com.shop.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Lombok;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.Optional;
@Controller
@RequiredArgsConstructor

public class MainController {
    private final ItemService itemService;
    private final MemberService memberService;
    private final HttpSession httpSession;
    @GetMapping(value = "/")
    public String main(ItemSearchDto itemSearchDto, Optional<Integer> page,
                       Model model, Principal principal, ItemFormDto itemFormDto, Item item,
                       ReservationDto reservationDto,HttpServletRequest request, HttpServletResponse response) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        if(itemSearchDto.getSearchQuery() == null)
        {
            itemSearchDto.setSearchQuery("");
        }

            String name = memberService.loadMemberName(principal,httpSession);
        model.addAttribute("name",name);

        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);
        System.out.println(items.getNumber()+"!!!!!!!!!!");
        System.out.println(items.getTotalPages()+"#########");
        System.out.println(reservationDto.getPrice() + " 예약 Dto 가격");
        model.addAttribute("items", items);
        model.addAttribute("reservationDto",reservationDto);
        model.addAttribute("itemSearchDto",itemSearchDto);
        model.addAttribute("price",reservationDto.getPrice());
        model.addAttribute("maxPage",5);
        return "main";
    }




}
