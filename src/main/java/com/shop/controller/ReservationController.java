package com.shop.controller;


import com.shop.constant.Role;
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
    public String reservationHist(Model model, HttpSession httpSession,
                                  Principal principal,ItemSearchDto itemSearchDto, Optional<Integer> page, ReservationDto reservationDto,Long reservationId,Reservation reservation){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5 );
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);
        String name = memberService.loadMemberName(principal,httpSession);
        model.addAttribute("name",name);
        model.addAttribute("List",reservationService.getmemberIdList(principal,httpSession));
        model.addAttribute("reservationId",reservationId);
        model.addAttribute("items",items);
        model.addAttribute("itemSearchDto",itemSearchDto);
        return "/order/reservationHist";
    }
    @GetMapping( value = {"/adminReservationHist","/adminReservationHist/{page}"})
    public String adminReservationHist(Model model, HttpSession httpSession,
                                  Principal principal,ItemSearchDto itemSearchDto,@PathVariable("page") Optional<Integer> page, ReservationDto reservationDto){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5 );
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
        String name = memberService.loadMemberName(principal,httpSession);
        model.addAttribute("name",name);
        model.addAttribute("List",reservationService.getAll());
        model.addAttribute("ItemSearchDto",new ItemSearchDto());
        model.addAttribute("maxPage", 5);
        model.addAttribute("items",items);
        return "/order/adminReservation";
    }
    @PostMapping( value = "/deleteAll")
    public String deleteAll(){
        reservationService.deletByeAll();
        return "redirect:/";
    }


    @DeleteMapping(value = "/deleteById/{reservationId}")
    public @ResponseBody
    ResponseEntity deleteById(@PathVariable("reservationId")Long reservationId, Principal principal, ItemFormDto itemFormDto, Item item, Reservation reservation, OrderDto orderDto,
                         ReservationDto reservationDto,HttpSession httpSession) throws Exception {

        /*Long memberId = reservationService.deleteByMemberId(principal,httpSession);
        if (memberId == null){
            return new ResponseEntity<String>("삭제할 예약목록을 선택해주세요",HttpStatus.FORBIDDEN);
        }*/
        // String a = "abc" + "def"
        // StringBuilder a;
        // a.append("abc");
        // a.append("def");
        /*if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for(FieldError fieldError : fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }*/
        // 로그인 정보 -> Spring Security
        // principal.getName() (현재 로그인된 정보)
        Long memberId;
        try {
            memberId=reservationService.deleteByMemberId(reservationId);
             //reservationId = reservationService.reservationDelete(reservation);
        }catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(memberId, HttpStatus.OK);
    }

}
