package com.shop.controller;


import com.shop.constant.ReservationStatus;
import com.shop.constant.Role;
import com.shop.constant.RoomType;
import com.shop.dto.*;
import com.shop.entity.Comment;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.entity.Reservation;
import com.shop.repository.MemberRepository;
import com.shop.service.ItemService;
import com.shop.service.MemberService;
import com.shop.service.ReservationService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/room")
@RequiredArgsConstructor
public class ReservationController {
private final ReservationService reservationService;
private final ItemService itemService;
private final MemberService memberService;
private final MemberRepository memberRepository;
private final HttpSession httpSession;
    @GetMapping( value = "/reservationHist")
    public String reservationHist( Long reservationId,Model model, HttpSession httpSession,
                                  Principal principal,ItemSearchDto itemSearchDto, Optional<Integer> page, ReservationDto reservationDto,Reservation reservation){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5 );
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);
        String name = memberService.loadMemberName(principal,httpSession);
        List<Reservation> reservationList =reservationService.getmemberIdList(principal,httpSession);
        List<Reservation> all = reservationService.getAll();


        model.addAttribute("name",name);
        model.addAttribute("List",reservationList);
        model.addAttribute("items",items);
        model.addAttribute("itemSearchDto",itemSearchDto);
        List<Reservation> reservations = new ArrayList<>();






        return "/order/reservationHist";
    }
    @GetMapping( value = {"/adminReservationHist","/adminReservationHist/{page}"})
    public String adminReservationHist(Model model, HttpSession httpSession,
                                  Principal principal,ItemSearchDto itemSearchDto,@PathVariable("page") Optional<Integer> page, ReservationSearchDto reservationSearchDto){
        if(reservationSearchDto.getSearchQuery() == null || reservationSearchDto.getSearchQuery().isEmpty())
        {
            reservationSearchDto.setSearchQuery("");

        }
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5 );
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
        Page<Reservation> reservations = reservationService.getAdminReservationPage(reservationSearchDto,pageable);
        String name = memberService.loadMemberName(principal,httpSession);
        model.addAttribute("name",name);
        model.addAttribute("List",reservationService.getAll());
        model.addAttribute("reservationSearchDto",reservationSearchDto);
        model.addAttribute("ItemSearchDto",new ItemSearchDto());
        model.addAttribute("reservations",reservations);
        model.addAttribute("maxPage", 5);
        model.addAttribute("items",items);
        return "/order/adminReservation";
    }
    @PostMapping( value = "/deleteAll")
    public String deleteAll(){
        reservationService.deletByeAll();
        return "redirect:/";
    }
    @GetMapping(value = "/reservationDtl/{reservationId}")
    public String reservationDtl(@PathVariable("reservationId")Long reservationId,Principal principal,
                                 HttpSession httpSession,Model model){
            String name =memberService.loadMemberName(principal,httpSession);
        Member email =memberService.findMember(httpSession,principal);
        System.out.println(email.getRole() +" 등급 분기");
        model.addAttribute("role",email.getRole());
            Reservation reservation=reservationService.getReservationId(reservationId);
            model.addAttribute("reservation",reservation);
            model.addAttribute("name",name);
        return "/order/reservationDtl";
    }


    @PostMapping(value = "/deleteById/{reservationId}")
    public @ResponseBody
    ResponseEntity deleteById(@PathVariable("reservationId")Long reservationId,ReservationDto reservationDtos,
                              Principal principal, HttpSession httpSession,ItemFormDto itemFormDto, OrderDto orderDto,ItemSearchDto itemSearchDto, Model model) throws Exception {
        try {
            reservationService.deleteByReservationId(reservationId);
        }catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(reservationId, HttpStatus.OK);
    }

}
